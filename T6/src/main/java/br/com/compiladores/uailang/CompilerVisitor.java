package br.com.compiladores.uailang;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

final class CompilerVisitor extends UaiLangBaseVisitor<Expr> {
    private final Map<String, Type> symbols = new LinkedHashMap<>();
    private final List<String> errors = new ArrayList<>();
    private final StringBuilder python = new StringBuilder();
    private final Deque<Integer> indentStack = new ArrayDeque<>();
    private int indent;

    String compile(UaiLangParser.ProgramaContext ctx) {
        python.append("# Codigo gerado pelo compilador UaiLang\n");
        python.append("# Programa: ").append(ctx.IDENT().getText()).append("\n\n");
        for (UaiLangParser.ComandoContext comando : ctx.comando()) {
            emitCommand(comando);
        }
        if (python.toString().endsWith("\n\n")) {
            return python.toString();
        }
        return python.append('\n').toString();
    }

    boolean hasErrors() {
        return !errors.isEmpty();
    }

    String formatErrors() {
        StringBuilder out = new StringBuilder();
        for (String error : errors) {
            out.append(error).append('\n');
        }
        out.append("Fim da compilacao\n");
        return out.toString();
    }

    private void emitCommand(UaiLangParser.ComandoContext ctx) {
        if (ctx.declaracao() != null) {
            emitDeclaration(ctx.declaracao());
        } else if (ctx.atribuicao() != null) {
            emitAssignment(ctx.atribuicao());
        } else if (ctx.leitura() != null) {
            emitRead(ctx.leitura());
        } else if (ctx.escrita() != null) {
            emitWrite(ctx.escrita());
        } else if (ctx.condicional() != null) {
            emitIf(ctx.condicional());
        } else if (ctx.repeticaoEnquanto() != null) {
            emitWhile(ctx.repeticaoEnquanto());
        } else if (ctx.repeticaoPara() != null) {
            emitFor(ctx.repeticaoPara());
        }
    }

    private void emitDeclaration(UaiLangParser.DeclaracaoContext ctx) {
        String name = ctx.IDENT().getText();
        Type declared = typeOf(ctx.tipo());
        if (symbols.containsKey(name)) {
            error(ctx.IDENT().getSymbol(), "trem '" + name + "' ja foi declarado");
        } else {
            symbols.put(name, declared);
        }

        Expr value = null;
        if (ctx.expressao() != null) {
            value = visit(ctx.expressao());
            if (!declared.accepts(value.type())) {
                error(ctx.IDENT().getSymbol(), "nao da para guardar " + label(value.type())
                    + " em '" + name + "', que e " + label(declared));
            }
        }

        if (value == null) {
            emit(name + " = " + defaultValue(declared));
        } else {
            emit(name + " = " + value.code());
        }
    }

    private void emitAssignment(UaiLangParser.AtribuicaoContext ctx) {
        String name = ctx.IDENT().getText();
        Type target = symbols.get(name);
        if (target == null) {
            error(ctx.IDENT().getSymbol(), "trem '" + name + "' nao foi declarado");
            target = Type.ERRO;
        }

        Expr value = visit(ctx.expressao());
        if (!target.accepts(value.type())) {
            error(ctx.IDENT().getSymbol(), "atribuicao incompativel para '" + name + "'");
        }
        emit(name + " = " + value.code());
    }

    private void emitRead(UaiLangParser.LeituraContext ctx) {
        String name = ctx.IDENT().getText();
        Type type = symbols.get(name);
        if (type == null) {
            error(ctx.IDENT().getSymbol(), "nao da para escuitar em '" + name
                + "', porque esse trem nao foi declarado");
            type = Type.ERRO;
        }
        emit(name + " = " + type.pythonReader());
    }

    private void emitWrite(UaiLangParser.EscritaContext ctx) {
        List<String> args = new ArrayList<>();
        for (UaiLangParser.ExpressaoContext exprCtx : ctx.expressao()) {
            args.add(visit(exprCtx).code());
        }
        emit("print(" + String.join(", ", args) + ")");
    }

    private void emitIf(UaiLangParser.CondicionalContext ctx) {
        Expr condition = visit(ctx.expressao());
        requireLogical(ctx.expressao().getStart(), condition, "condicao do se");

        emit("if " + condition.code() + ":");
        enterBlock();
        int index = 0;
        while (index < ctx.comando().size() && !isAfterElse(ctx, ctx.comando(index))) {
            emitCommand(ctx.comando(index));
            index++;
        }
        ensureBlockHasContent();
        exitBlock();

        if (ctx.SENAO() != null) {
            emit("else:");
            enterBlock();
            while (index < ctx.comando().size()) {
                emitCommand(ctx.comando(index));
                index++;
            }
            ensureBlockHasContent();
            exitBlock();
        }
    }

    private boolean isAfterElse(UaiLangParser.CondicionalContext ctx, UaiLangParser.ComandoContext comando) {
        return ctx.SENAO() != null && comando.getStart().getTokenIndex() > ctx.SENAO().getSymbol().getTokenIndex();
    }

    private void emitWhile(UaiLangParser.RepeticaoEnquantoContext ctx) {
        Expr condition = visit(ctx.expressao());
        requireLogical(ctx.expressao().getStart(), condition, "condicao do inté");
        emit("while " + condition.code() + ":");
        enterBlock();
        for (UaiLangParser.ComandoContext comando : ctx.comando()) {
            emitCommand(comando);
        }
        ensureBlockHasContent();
        exitBlock();
    }

    private void emitFor(UaiLangParser.RepeticaoParaContext ctx) {
        String name = ctx.IDENT().getText();
        Type varType = symbols.get(name);
        if (varType == null) {
            error(ctx.IDENT().getSymbol(), "contador '" + name + "' nao foi declarado");
        } else if (varType != Type.INTEIRO) {
            error(ctx.IDENT().getSymbol(), "contador do pra precisa ser inteiro");
        }

        Expr start = visit(ctx.expressao(0));
        Expr end = visit(ctx.expressao(1));
        requireInteger(ctx.expressao(0).getStart(), start, "inicio do pra");
        requireInteger(ctx.expressao(1).getStart(), end, "fim do pra");

        emit("for " + name + " in range(" + start.code() + ", " + end.code() + " + 1):");
        enterBlock();
        for (UaiLangParser.ComandoContext comando : ctx.comando()) {
            emitCommand(comando);
        }
        ensureBlockHasContent();
        exitBlock();
    }

    @Override
    public Expr visitOuExpr(UaiLangParser.OuExprContext ctx) {
        Expr left = visit(ctx.eExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.eExpr().size(); i++) {
            Expr right = visit(ctx.eExpr(i));
            requireLogical(ctx.eExpr(i - 1).getStart(), left, "operador ou");
            requireLogical(ctx.eExpr(i).getStart(), right, "operador ou");
            code = "(" + code + " or " + right.code() + ")";
            type = Type.LOGICO;
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitEExpr(UaiLangParser.EExprContext ctx) {
        Expr left = visit(ctx.igualdadeExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.igualdadeExpr().size(); i++) {
            Expr right = visit(ctx.igualdadeExpr(i));
            requireLogical(ctx.igualdadeExpr(i - 1).getStart(), left, "operador e");
            requireLogical(ctx.igualdadeExpr(i).getStart(), right, "operador e");
            code = "(" + code + " and " + right.code() + ")";
            type = Type.LOGICO;
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitIgualdadeExpr(UaiLangParser.IgualdadeExprContext ctx) {
        Expr left = visit(ctx.relExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.relExpr().size(); i++) {
            Expr right = visit(ctx.relExpr(i));
            if (!left.type().accepts(right.type()) && !right.type().accepts(left.type())) {
                error(ctx.relExpr(i).getStart(), "comparacao entre tipos incompativeis");
            }
            String op = ctx.getChild(2 * i - 1).getText().equals("==") ? "==" : "!=";
            code = "(" + code + " " + op + " " + right.code() + ")";
            type = Type.LOGICO;
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitRelExpr(UaiLangParser.RelExprContext ctx) {
        Expr left = visit(ctx.adExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.adExpr().size(); i++) {
            Expr right = visit(ctx.adExpr(i));
            requireNumeric(ctx.adExpr(i - 1).getStart(), left, "comparacao");
            requireNumeric(ctx.adExpr(i).getStart(), right, "comparacao");
            code = "(" + code + " " + ctx.getChild(2 * i - 1).getText() + " " + right.code() + ")";
            type = Type.LOGICO;
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitAdExpr(UaiLangParser.AdExprContext ctx) {
        Expr left = visit(ctx.multExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.multExpr().size(); i++) {
            Expr right = visit(ctx.multExpr(i));
            String op = ctx.getChild(2 * i - 1).getText();
            if ("+".equals(op) && (left.type() == Type.TEXTO || right.type() == Type.TEXTO)) {
                if (left.type() != Type.TEXTO || right.type() != Type.TEXTO) {
                    error(ctx.multExpr(i).getStart(), "texto so soma com texto");
                }
                type = Type.TEXTO;
            } else {
                requireNumeric(ctx.multExpr(i - 1).getStart(), left, "operador " + op);
                requireNumeric(ctx.multExpr(i).getStart(), right, "operador " + op);
                type = left.type() == Type.REAL || right.type() == Type.REAL ? Type.REAL : Type.INTEIRO;
            }
            code = "(" + code + " " + op + " " + right.code() + ")";
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitMultExpr(UaiLangParser.MultExprContext ctx) {
        Expr left = visit(ctx.unExpr(0));
        String code = left.code();
        Type type = left.type();
        for (int i = 1; i < ctx.unExpr().size(); i++) {
            Expr right = visit(ctx.unExpr(i));
            String op = ctx.getChild(2 * i - 1).getText();
            requireNumeric(ctx.unExpr(i - 1).getStart(), left, "operador " + op);
            requireNumeric(ctx.unExpr(i).getStart(), right, "operador " + op);
            if (("/".equals(op) || "%".equals(op)) && right.constantZero()) {
                error(ctx.unExpr(i).getStart(), "divisao por zero detectada");
            }
            type = "/".equals(op) || left.type() == Type.REAL || right.type() == Type.REAL
                ? Type.REAL : Type.INTEIRO;
            code = "(" + code + " " + op + " " + right.code() + ")";
            left = new Expr(type, code, false);
        }
        return new Expr(type, code, false);
    }

    @Override
    public Expr visitUnExpr(UaiLangParser.UnExprContext ctx) {
        if (ctx.primario() != null) {
            return visit(ctx.primario());
        }
        Expr value = visit(ctx.unExpr());
        String op = ctx.getChild(0).getText();
        if ("nao".equals(op)) {
            requireLogical(ctx.getStart(), value, "operador nao");
            return new Expr(Type.LOGICO, "(not " + value.code() + ")", false);
        }
        requireNumeric(ctx.getStart(), value, "menos unario");
        return new Expr(value.type(), "(-" + value.code() + ")", value.constantZero());
    }

    @Override
    public Expr visitPrimario(UaiLangParser.PrimarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return new Expr(Type.INTEIRO, ctx.NUM_INT().getText(), "0".equals(ctx.NUM_INT().getText()));
        }
        if (ctx.NUM_REAL() != null) {
            return new Expr(Type.REAL, ctx.NUM_REAL().getText(), Double.parseDouble(ctx.NUM_REAL().getText()) == 0.0);
        }
        if (ctx.TEXTO_LITERAL() != null) {
            return new Expr(Type.TEXTO, ctx.TEXTO_LITERAL().getText(), false);
        }
        if (ctx.VERDADE() != null) {
            return new Expr(Type.LOGICO, "True", false);
        }
        if (ctx.MENTIRA() != null) {
            return new Expr(Type.LOGICO, "False", true);
        }
        if (ctx.IDENT() != null) {
            String name = ctx.IDENT().getText();
            Type type = symbols.get(name);
            if (type == null) {
                error(ctx.IDENT().getSymbol(), "trem '" + name + "' nao foi declarado");
                type = Type.ERRO;
            }
            return new Expr(type, name, false);
        }
        return visit(ctx.expressao());
    }

    private Type typeOf(UaiLangParser.TipoContext ctx) {
        if (ctx.INTEIRO() != null) {
            return Type.INTEIRO;
        }
        if (ctx.REAL() != null) {
            return Type.REAL;
        }
        if (ctx.TEXTO() != null) {
            return Type.TEXTO;
        }
        return Type.LOGICO;
    }

    private void requireNumeric(Token token, Expr expr, String place) {
        if (!expr.type().isNumeric() && expr.type() != Type.ERRO) {
            error(token, place + " precisa de numero");
        }
    }

    private void requireInteger(Token token, Expr expr, String place) {
        if (expr.type() != Type.INTEIRO && expr.type() != Type.ERRO) {
            error(token, place + " precisa ser inteiro");
        }
    }

    private void requireLogical(Token token, Expr expr, String place) {
        if (expr.type() != Type.LOGICO && expr.type() != Type.ERRO) {
            error(token, place + " precisa ser logico");
        }
    }

    private String defaultValue(Type type) {
        return switch (type) {
            case INTEIRO -> "0";
            case REAL -> "0.0";
            case TEXTO -> "\"\"";
            case LOGICO -> "False";
            case ERRO -> "None";
        };
    }

    private String label(Type type) {
        return switch (type) {
            case INTEIRO -> "inteiro";
            case REAL -> "real";
            case TEXTO -> "texto";
            case LOGICO -> "logico";
            case ERRO -> "erro";
        };
    }

    private void emit(String line) {
        python.append("    ".repeat(Math.max(0, indent))).append(line).append('\n');
    }

    private void enterBlock() {
        indentStack.push(indent);
        indent++;
    }

    private void exitBlock() {
        indent = indentStack.pop();
    }

    private void ensureBlockHasContent() {
        String current = python.toString();
        if (current.endsWith(":\n")) {
            emit("pass");
        }
    }

    private void error(Token token, String message) {
        errors.add("Linha " + token.getLine() + ": " + message);
    }
}
