import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

final class Parser {
    private final List<Token> tokens;
    private int current;
    private final SemanticAnalyzer sem;
    private final CodeGenerator gen;
    private final Deque<RoutineKind> routineStack = new ArrayDeque<>();

    private enum RoutineKind {
        GLOBAL,
        ALGORITHM,
        PROCEDURE,
        FUNCTION
    }

    private static final class ExprResult {
        final SemanticAnalyzer.TypeInfo type;
        final String text;
        final boolean assignable;
        final Token anchor;

        ExprResult(SemanticAnalyzer.TypeInfo type, String text, boolean assignable, Token anchor) {
            this.type = type;
            this.text = text;
            this.assignable = assignable;
            this.anchor = anchor;
        }
    }

    private static final class DeclItem {
        final Token name;
        final int pointerDepth;
        final int arrayDepth;
        final List<String> dimensions;

        DeclItem(Token name, int pointerDepth, int arrayDepth, List<String> dimensions) {
            this.name = name;
            this.pointerDepth = pointerDepth;
            this.arrayDepth = arrayDepth;
            this.dimensions = dimensions;
        }
    }

    private static final class RoutineHeader {
        final Token name;
        final List<SemanticAnalyzer.ParamInfo> params;
        final SemanticAnalyzer.TypeInfo returnType;

        RoutineHeader(Token name, List<SemanticAnalyzer.ParamInfo> params,
                SemanticAnalyzer.TypeInfo returnType) {
            this.name = name;
            this.params = params;
            this.returnType = returnType;
        }
    }

    Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.sem = new SemanticAnalyzer();
        this.gen = new CodeGenerator();
        this.current = 0;
    }

    String programa() {
        routineStack.push(RoutineKind.GLOBAL);

        while (isGlobalDeclarationStart(peek())) {
            declaracaoGlobal();
        }

        expect(TokenType.ALGORITMO);
        routineStack.push(RoutineKind.ALGORITHM);
        corpo();
        routineStack.pop();

        expect(TokenType.FIM_ALGORITMO);
        expect(TokenType.EOF);

        if (sem.hasErrors()) {
            throw new SemanticException(sem.output());
        }

        return gen.getCode();
    }

    private void corpo() {
        while (isLocalDeclarationStart(peek())) {
            declaracaoLocal();
        }

        while (isCommandStart(peek())) {
            cmd();
        }
    }

    private void declaracaoGlobal() {
        if (check(TokenType.PROCEDIMENTO)) {
            declaracaoProcedimento();
            return;
        }
        if (check(TokenType.FUNCAO)) {
            declaracaoFuncao();
            return;
        }
        declaracaoLocal();
    }

    private void declaracaoLocal() {
        if (match(TokenType.DECLARE)) {
            List<DeclItem> itens = parseDeclItemList();
            expect(TokenType.DOIS_PONTOS);
            SemanticAnalyzer.TypeInfo baseType = parseTypeExpression();
            for (DeclItem item : itens) {
                SemanticAnalyzer.TypeInfo type = wrapType(baseType, item.pointerDepth, item.arrayDepth);
                sem.declareVariable(item.name.lexeme, type, item.name.line);
                gen.declareVariable(item.name.lexeme, type, item.dimensions);
            }
            return;
        }

        if (match(TokenType.CONSTANTE)) {
            Token name = expect(TokenType.IDENT);
            expect(TokenType.DOIS_PONTOS);
            SemanticAnalyzer.TypeInfo type = parseSimpleTypeExpression();
            expect(TokenType.IGUAL);
            int valueStart = current;
            expressao();
            int valueEnd = current;
            sem.declareConstant(name.lexeme, type, name.line);
            gen.declareConstant(name.lexeme, type, extractText(valueStart, valueEnd));
            return;
        }

        if (match(TokenType.TIPO)) {
            Token name = expect(TokenType.IDENT);
            expect(TokenType.DOIS_PONTOS);
            if (check(TokenType.REGISTRO)) {
                TypeRecordBuilder builder = new TypeRecordBuilder(name.lexeme);
                SemanticAnalyzer.TypeInfo recordType = builder.type;
                sem.declareType(name.lexeme, recordType, name.line);
                parseRecordType(builder);
                gen.declareRecordType(name.lexeme, recordType);
            } else {
                SemanticAnalyzer.TypeInfo type = parseTypeExpression();
                sem.declareType(name.lexeme, type, name.line);
            }
            return;
        }

        error(peek());
    }

    private void declaracaoProcedimento() {
        expect(TokenType.PROCEDIMENTO);
        RoutineHeader header = parseRoutineHeader(false);
        sem.declareRoutine(header.name.lexeme, SemanticAnalyzer.SymbolKind.PROCEDURE, header.params,
            null, header.name.line);
        sem.enterScope();
        routineStack.push(RoutineKind.PROCEDURE);
        declareParameters(header.params);
        gen.startRoutine(header.name.lexeme, header.params, null);
        corpo();
        gen.endRoutine();
        routineStack.pop();
        sem.exitScope();
        expect(TokenType.FIM_PROCEDIMENTO);
    }

    private void declaracaoFuncao() {
        expect(TokenType.FUNCAO);
        RoutineHeader header = parseRoutineHeader(true);
        sem.declareRoutine(header.name.lexeme, SemanticAnalyzer.SymbolKind.FUNCTION, header.params,
            header.returnType, header.name.line);
        sem.enterScope();
        routineStack.push(RoutineKind.FUNCTION);
        declareParameters(header.params);
        gen.startRoutine(header.name.lexeme, header.params, header.returnType);
        corpo();
        gen.endRoutine();
        routineStack.pop();
        sem.exitScope();
        expect(TokenType.FIM_FUNCAO);
    }

    private RoutineHeader parseRoutineHeader(boolean hasReturnType) {
        Token name = expect(TokenType.IDENT);
        expect(TokenType.ABRE_PAR);
        List<SemanticAnalyzer.ParamInfo> params = new ArrayList<>();
        if (!check(TokenType.FECHA_PAR)) {
            params = parseParameters();
        }
        expect(TokenType.FECHA_PAR);
        SemanticAnalyzer.TypeInfo returnType = null;
        if (hasReturnType) {
            expect(TokenType.DOIS_PONTOS);
            returnType = parseTypeExpression();
        }
        return new RoutineHeader(name, params, returnType);
    }

    private List<SemanticAnalyzer.ParamInfo> parseParameters() {
        List<SemanticAnalyzer.ParamInfo> params = new ArrayList<>();

        while (true) {
            boolean byRef = match(TokenType.VAR);
            List<DeclItem> itens = parseDeclItemList(false);
            expect(TokenType.DOIS_PONTOS);
            SemanticAnalyzer.TypeInfo baseType = parseTypeExpression();
            for (DeclItem item : itens) {
                SemanticAnalyzer.TypeInfo type = wrapType(baseType, item.pointerDepth, item.arrayDepth);
                params.add(new SemanticAnalyzer.ParamInfo(item.name.lexeme, type, byRef, item.name.line));
            }

            if (!match(TokenType.VIRGULA)) {
                break;
            }
            if (!(check(TokenType.VAR) || check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO))) {
                break;
            }
        }

        return params;
    }

    private void declareParameters(List<SemanticAnalyzer.ParamInfo> params) {
        for (SemanticAnalyzer.ParamInfo param : params) {
            sem.declareParameter(param.name, param.type, param.byRef, param.line);
        }
    }

    private List<DeclItem> parseDeclItemList() {
        return parseDeclItemList(true);
    }

    private List<DeclItem> parseDeclItemList(boolean allowArraySuffix) {
        List<DeclItem> itens = new ArrayList<>();
        itens.add(parseDeclItem(allowArraySuffix));
        while (match(TokenType.VIRGULA)) {
            itens.add(parseDeclItem(allowArraySuffix));
        }
        return itens;
    }

    private DeclItem parseDeclItem(boolean allowArraySuffix) {
        int pointerDepth = 0;
        while (match(TokenType.CIRCUNFLEXO)) {
            pointerDepth++;
        }

        Token name = expect(TokenType.IDENT);
        int arrayDepth = 0;
        List<String> dimensions = new ArrayList<>();
        if (allowArraySuffix) {
            while (match(TokenType.ABRE_COL)) {
                int sizeStart = current;
                expressao();
                int sizeEnd = current;
                expect(TokenType.FECHA_COL);
                dimensions.add(extractText(sizeStart, sizeEnd));
                arrayDepth++;
            }
        }

        return new DeclItem(name, pointerDepth, arrayDepth, dimensions);
    }

    private SemanticAnalyzer.TypeInfo parseSimpleTypeExpression() {
        if (match(TokenType.LITERAL)) {
            return sem.basicLiteral();
        }
        if (match(TokenType.INTEIRO)) {
            return sem.basicInteger();
        }
        if (match(TokenType.REAL)) {
            return sem.basicReal();
        }
        if (match(TokenType.LOGICO)) {
            return sem.basicLogical();
        }
        Token token = peek();
        if (token.type == TokenType.IDENT) {
            advance();
            return sem.resolveTypeByName(token);
        }
        error(token);
        return sem.errorType();
    }

    private SemanticAnalyzer.TypeInfo parseTypeExpression() {
        int pointerDepth = 0;
        while (match(TokenType.CIRCUNFLEXO)) {
            pointerDepth++;
        }

        SemanticAnalyzer.TypeInfo baseType;
        if (match(TokenType.LITERAL)) {
            baseType = sem.basicLiteral();
        } else if (match(TokenType.INTEIRO)) {
            baseType = sem.basicInteger();
        } else if (match(TokenType.REAL)) {
            baseType = sem.basicReal();
        } else if (match(TokenType.LOGICO)) {
            baseType = sem.basicLogical();
        } else if (check(TokenType.IDENT)) {
            Token token = advance();
            baseType = sem.resolveTypeByName(token);
        } else if (check(TokenType.REGISTRO)) {
            TypeRecordBuilder builder = new TypeRecordBuilder("<anon>");
            baseType = builder.type;
            parseRecordType(builder);
        } else {
            error(peek());
            return sem.errorType();
        }

        return wrapType(baseType, pointerDepth, 0);
    }

    private void parseRecordType(TypeRecordBuilder builder) {
        expect(TokenType.REGISTRO);
        while (isRecordFieldStart(peek())) {
            List<DeclItem> itens = parseDeclItemList();
            expect(TokenType.DOIS_PONTOS);
            SemanticAnalyzer.TypeInfo baseType = parseTypeExpression();
            for (DeclItem item : itens) {
                SemanticAnalyzer.TypeInfo type = wrapType(baseType, item.pointerDepth, item.arrayDepth);
                if (builder.type.fields.containsKey(item.name.lexeme)) {
                    sem.reportDuplicateField(item.name, item.name.lexeme);
                } else {
                    builder.type.fields.put(item.name.lexeme, type);
                }
            }
        }
        expect(TokenType.FIM_REGISTRO);
    }

    private SemanticAnalyzer.TypeInfo wrapType(SemanticAnalyzer.TypeInfo baseType, int pointerDepth,
            int arrayDepth) {
        SemanticAnalyzer.TypeInfo type = baseType;
        for (int i = 0; i < pointerDepth; i++) {
            type = sem.pointerTo(type);
        }
        for (int i = 0; i < arrayDepth; i++) {
            type = sem.arrayOf(type);
        }
        return type;
    }

    private void cmd() {
        if (match(TokenType.LEIA)) {
            expect(TokenType.ABRE_PAR);
            ExprResult r = parseDesignatorForRead();
            gen.emitRead(r.text, r.type);
            while (match(TokenType.VIRGULA)) {
                ExprResult r2 = parseDesignatorForRead();
                gen.emitRead(r2.text, r2.type);
            }
            expect(TokenType.FECHA_PAR);
            return;
        }

        if (match(TokenType.ESCREVA)) {
            expect(TokenType.ABRE_PAR);
            while (!check(TokenType.FECHA_PAR)) {
                int start = current;
                SemanticAnalyzer.TypeInfo t = expressao();
                int end = current; // exclusive
                String text = extractText(start, end);
                gen.emitWrite(text, t);
                if (!match(TokenType.VIRGULA)) break;
            }
            expect(TokenType.FECHA_PAR);
            return;
        }

        if (match(TokenType.SE)) {
            int exprStart = current;
            SemanticAnalyzer.TypeInfo condType = expressao();
            int exprEnd = current;
            String condText = extractText(exprStart, exprEnd);
            expect(TokenType.ENTAO);
            gen.emitIfStart(condText);
            while (isCommandStart(peek()) && !check(TokenType.SENAO) && !check(TokenType.FIM_SE)) {
                cmd();
            }
            if (match(TokenType.SENAO)) {
                gen.emitElseStart();
                while (isCommandStart(peek()) && !check(TokenType.FIM_SE)) {
                    cmd();
                }
            }
            expect(TokenType.FIM_SE);
            gen.exitBlock();
            return;
        }

        if (match(TokenType.CASO)) {
            int switchStart = current;
            expressao();
            int switchEnd = current;
            expect(TokenType.SEJA);
            gen.emitSwitchStart(extractText(switchStart, switchEnd));
            while (isNumeroIntervaloStart(peek())) {
                itemSelecao();
            }
            if (match(TokenType.SENAO)) {
                gen.emitDefaultLabel();
                while (isCommandStart(peek()) && !check(TokenType.FIM_CASO)) {
                    cmd();
                }
                gen.emitBreakAndCloseCase();
            }
            expect(TokenType.FIM_CASO);
            gen.exitBlock();
            return;
        }

        if (match(TokenType.PARA)) {
            ExprResult target = parseDesignatorForRead();
            expect(TokenType.ATRIBUICAO);
            int startExprStart = current;
            SemanticAnalyzer.TypeInfo startType = expressao();
            int startExprEnd = current;
            String startText = extractText(startExprStart, startExprEnd);
            expect(TokenType.ATE);
            int endExprStart = current;
            SemanticAnalyzer.TypeInfo endType = expressao();
            int endExprEnd = current;
            String endText = extractText(endExprStart, endExprEnd);
            expect(TokenType.FACA);
            gen.emitFor(target.text, startText, endText);
            while (isCommandStart(peek()) && !check(TokenType.FIM_PARA)) {
                cmd();
            }
            expect(TokenType.FIM_PARA);
            gen.exitBlock();
            return;
        }

        if (match(TokenType.ENQUANTO)) {
            int exprStart2 = current;
            SemanticAnalyzer.TypeInfo condType2 = expressao();
            int exprEnd2 = current;
            String condText2 = extractText(exprStart2, exprEnd2);
            expect(TokenType.FACA);
            gen.emitWhileStart(condText2);
            while (isCommandStart(peek()) && !check(TokenType.FIM_ENQUANTO)) {
                cmd();
            }
            expect(TokenType.FIM_ENQUANTO);
            gen.exitBlock();
            return;
        }

        if (match(TokenType.FACA)) {
            // do { ... } while (expr);
            statementsDoLoop:
            {
                // open do block
                gen.enterBlockWithHeader("do");
                while (isCommandStart(peek()) && !check(TokenType.ATE)) {
                    cmd();
                }
                expect(TokenType.ATE);
                int condStart = current;
                SemanticAnalyzer.TypeInfo condT = expressao();
                int condEnd = current;
                String condTxt = extractText(condStart, condEnd);
                gen.exitBlock();
                gen.emitDoWhile(condTxt);
            }
            return;
        }

        if (match(TokenType.RETORNE)) {
            Token retorneToken = previous();
            if (routineStack.peek() != RoutineKind.FUNCTION) {
                sem.reportRetorneNotAllowed(retorneToken);
            }
            if (!check(TokenType.FIM_FUNCAO) && !check(TokenType.FIM_PROCEDIMENTO)
                    && !check(TokenType.FIM_ALGORITMO) && !check(TokenType.SENAO)
                    && !check(TokenType.FIM_SE) && !check(TokenType.FIM_PARA)
                    && !check(TokenType.FIM_ENQUANTO) && !check(TokenType.FIM_CASO)
                    && peek().type != TokenType.EOF) {
                int rStart = current;
                SemanticAnalyzer.TypeInfo rType = expressao();
                int rEnd = current;
                String rText = extractText(rStart, rEnd);
                gen.emitReturn(rText);
            }
            return;
        }

        if (check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO) || check(TokenType.E_COMERCIAL)) {
            commandStartingWithDesignator();
            return;
        }

        error(peek());
    }

    private void commandStartingWithDesignator() {
        ExprResult target = parseDesignator(false);
        if (match(TokenType.ATRIBUICAO)) {
            int start = current;
            SemanticAnalyzer.TypeInfo rhs = expressao();
            int end = current;
            String rhsText = extractText(start, end);

            if (target.assignable && !target.type.isError() && !rhs.isError()
                    && !sem.canAssign(target.type, rhs)) {
                sem.reportAssignmentIncompatible(target.anchor, target.text);
            }

            gen.emitAssignment(target.text, rhsText, target.type);
            return;
        }

        if (match(TokenType.ABRE_PAR)) {
            int argsStart = current;
            parseCallArguments(target);
            int argsEnd = current - 1;
            gen.emitProcedureCall(target.text, extractText(argsStart, argsEnd));
            return;
        }

        error(peek());
    }

    private ExprResult parseDesignatorForRead() {
        return parseDesignator(false);
    }

    private String extractText(int startIndexInclusive, int endIndexExclusive) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndexInclusive; i < endIndexExclusive; i++) {
            Token t = tokens.get(i);
            if (i > startIndexInclusive) sb.append(' ');
            sb.append(tokenLexemeAsC(t));
        }
        return sb.toString();
    }

    private String tokenLexemeAsC(Token t) {
        switch (t.type) {
            case E: return "&&";
            case OU: return "||";
            case NAO: return "!";
            case IGUAL: return "==";
            case DIFERENTE: return "!=";
            case VERDADEIRO: return "1";
            case FALSO: return "0";
            case CIRCUNFLEXO: return "*";
            case E_COMERCIAL: return "&";
            default: return t.lexeme;
        }
    }

    private void parseCallArguments(ExprResult callee) {
        List<SemanticAnalyzer.TypeInfo> actuals = new ArrayList<>();
        if (!check(TokenType.FECHA_PAR)) {
            actuals.add(expressao());
            while (match(TokenType.VIRGULA)) {
                actuals.add(expressao());
            }
        }
        expect(TokenType.FECHA_PAR);

        SemanticAnalyzer.Symbol symbol = sem.lookup(callee.anchor.lexeme);
        if (symbol == null || !symbol.isCallable()) {
            return;
        }

        List<SemanticAnalyzer.ParamInfo> params = symbol.parameters;
        boolean incompatible = params.size() != actuals.size();
        int limit = Math.min(params.size(), actuals.size());
        for (int i = 0; i < limit && !incompatible; i++) {
            if (!sem.canPassArgument(params.get(i).type, actuals.get(i))) {
                incompatible = true;
            }
        }

        if (incompatible) {
            sem.reportParamIncompatible(callee.anchor, callee.anchor.lexeme);
        }
    }

    private void itemSelecao() {
        List<String> labels = constantes();
        expect(TokenType.DOIS_PONTOS);
        for (String label : labels) {
            gen.emitCaseLabel(label);
        }
        while (isCommandStart(peek()) && !check(TokenType.FIM_CASO) && !check(TokenType.SENAO)) {
            cmd();
        }
        for (int i = 0; i < labels.size(); i++) {
            gen.emitBreakAndCloseCase();
        }
    }

    private List<String> constantes() {
        List<String> labels = new ArrayList<>();
        labels.addAll(numeroIntervalo());
        while (match(TokenType.VIRGULA)) {
            labels.addAll(numeroIntervalo());
        }
        return labels;
    }

    private List<String> numeroIntervalo() {
        String first = numeroInteiro();
        List<String> labels = new ArrayList<>();
        if (match(TokenType.INTERVALO)) {
            String last = numeroInteiro();
            int start = Integer.parseInt(first);
            int end = Integer.parseInt(last);
            for (int value = start; value <= end; value++) {
                labels.add(String.valueOf(value));
            }
        } else {
            labels.add(first);
        }
        return labels;
    }

    private String numeroInteiro() {
        boolean plus = match(TokenType.SOMA);
        boolean minus = match(TokenType.SUB);
        Token number = expect(TokenType.NUM_INT);
        return (minus ? "-" : "") + number.lexeme;
    }

    private SemanticAnalyzer.TypeInfo expressao() {
        SemanticAnalyzer.TypeInfo type = termoLogico();
        while (match(TokenType.OU)) {
            SemanticAnalyzer.TypeInfo right = termoLogico();
            if (!type.isError() && !right.isError() && !type.isLogical()) {
                type = sem.errorType();
            } else if (!type.isError() && !right.isError() && !right.isLogical()) {
                type = sem.errorType();
            } else {
                type = sem.basicLogical();
            }
        }
        return type;
    }

    private SemanticAnalyzer.TypeInfo termoLogico() {
        SemanticAnalyzer.TypeInfo type = fatorLogico();
        while (match(TokenType.E)) {
            SemanticAnalyzer.TypeInfo right = fatorLogico();
            if (!type.isError() && !right.isError() && (!type.isLogical() || !right.isLogical())) {
                type = sem.errorType();
            } else {
                type = sem.basicLogical();
            }
        }
        return type;
    }

    private SemanticAnalyzer.TypeInfo fatorLogico() {
        boolean not = match(TokenType.NAO);
        SemanticAnalyzer.TypeInfo type = parcelaLogica();
        if (not && !type.isError() && !type.isLogical()) {
            return sem.errorType();
        }
        return type;
    }

    private SemanticAnalyzer.TypeInfo parcelaLogica() {
        if (match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
            return sem.basicLogical();
        }
        return expRelacional();
    }

    private SemanticAnalyzer.TypeInfo expRelacional() {
        SemanticAnalyzer.TypeInfo left = expAritmetica();
        if (isOperadorRelacional(peek())) {
            advance();
            SemanticAnalyzer.TypeInfo right = expAritmetica();
            if (left.isError() || right.isError()) {
                return sem.errorType();
            }
            return sem.basicLogical();
        }
        return left;
    }

    private SemanticAnalyzer.TypeInfo expAritmetica() {
        SemanticAnalyzer.TypeInfo type = termo();
        while (check(TokenType.SOMA) || check(TokenType.SUB)) {
            Token op = advance();
            SemanticAnalyzer.TypeInfo right = termo();
            type = combineArithmetic(type, right, op);
        }
        return type;
    }

    private SemanticAnalyzer.TypeInfo termo() {
        SemanticAnalyzer.TypeInfo type = fator();
        while (check(TokenType.MULT) || check(TokenType.DIV) || check(TokenType.MOD)) {
            Token op = advance();
            SemanticAnalyzer.TypeInfo right = fator();
            type = combineArithmetic(type, right, op);
        }
        return type;
    }

    private SemanticAnalyzer.TypeInfo fator() {
        return parcela();
    }

    private SemanticAnalyzer.TypeInfo parcela() {
        boolean unaryPlus = match(TokenType.SOMA);
        boolean unaryMinus = match(TokenType.SUB);

        if (match(TokenType.NUM_INT)) {
            return sem.basicInteger();
        }
        if (match(TokenType.NUM_REAL)) {
            return sem.basicReal();
        }
        if (match(TokenType.CADEIA)) {
            return sem.basicLiteral();
        }
        if (match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
            return sem.basicLogical();
        }

        if (match(TokenType.E_COMERCIAL)) {
            ExprResult result = parseDesignator(false);
            if (!result.assignable) {
                return sem.errorType();
            }
            return sem.pointerTo(result.type);
        }

        if (match(TokenType.ABRE_PAR)) {
            SemanticAnalyzer.TypeInfo type = expressao();
            expect(TokenType.FECHA_PAR);
            return type;
        }

        if (check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO)) {
            ExprResult result = parseDesignator(true);
            return result.type;
        }

        if (unaryPlus || unaryMinus) {
            SemanticAnalyzer.TypeInfo type = parcela();
            if (!type.isError() && !type.isNumeric()) {
                return sem.errorType();
            }
            return type;
        }

        error(peek());
        return sem.errorType();
    }

    private ExprResult parseDesignator(boolean allowCall) {
        StringBuilder text = new StringBuilder();
        int derefCount = 0;
        while (match(TokenType.CIRCUNFLEXO)) {
            text.append('*');
            derefCount++;
        }

        Token base = expect(TokenType.IDENT);
        text.append(base.lexeme);
        SemanticAnalyzer.Symbol symbol = sem.lookup(base.lexeme);

        if (symbol == null || sem.isTypeSymbol(symbol)) {
            skipSuffixes(text);
            sem.reportUndeclared(base, text.toString());
            return new ExprResult(sem.errorType(), text.toString(), false, base);
        }

        SemanticAnalyzer.TypeInfo type = symbol.type;
        boolean assignable = symbol.kind == SemanticAnalyzer.SymbolKind.VARIABLE
            || symbol.kind == SemanticAnalyzer.SymbolKind.PARAMETER;

        if (allowCall && match(TokenType.ABRE_PAR)) {
            List<SemanticAnalyzer.TypeInfo> actuals = new ArrayList<>();
            int argsStart = current;
            if (!check(TokenType.FECHA_PAR)) {
                actuals.add(expressao());
                while (match(TokenType.VIRGULA)) {
                    actuals.add(expressao());
                }
            }
            int argsEnd = current;
            expect(TokenType.FECHA_PAR);
            text.append('(').append(extractText(argsStart, argsEnd)).append(')');

            if (!symbol.isCallable()) {
                return new ExprResult(sem.errorType(), text.toString(), false, base);
            }

            boolean incompatible = symbol.parameters.size() != actuals.size();
            int limit = Math.min(symbol.parameters.size(), actuals.size());
            for (int i = 0; i < limit && !incompatible; i++) {
                if (!sem.canPassArgument(symbol.parameters.get(i).type, actuals.get(i))) {
                    incompatible = true;
                }
            }
            if (incompatible) {
                sem.reportParamIncompatible(base, base.lexeme);
            }

            if (symbol.kind == SemanticAnalyzer.SymbolKind.PROCEDURE) {
                return new ExprResult(sem.errorType(), text.toString(), false, base);
            }
            return new ExprResult(symbol.returnType == null ? sem.errorType() : symbol.returnType,
                text.toString(), false, base);
        }

        for (int i = 0; i < derefCount; i++) {
            type = sem.dereference(type);
            if (type.isError()) {
                break;
            }
        }

        while (true) {
            if (match(TokenType.PONTO)) {
                Token field = expect(TokenType.IDENT);
                text.append('.').append(field.lexeme);
                if (!type.isRecord()) {
                    sem.reportUndeclared(base, text.toString());
                    return new ExprResult(sem.errorType(), text.toString(), false, base);
                }
                SemanticAnalyzer.TypeInfo fieldType = sem.fieldType(type, field.lexeme);
                if (fieldType == null) {
                    sem.reportUndeclared(base, text.toString());
                    return new ExprResult(sem.errorType(), text.toString(), false, base);
                }
                type = fieldType;
                continue;
            }

            if (match(TokenType.ABRE_COL)) {
                text.append('[');
                int indexStart = current;
                SemanticAnalyzer.TypeInfo indexType = expressao();
                int indexEnd = current;
                expect(TokenType.FECHA_COL);
                text.append(extractText(indexStart, indexEnd));
                text.append(']');
                if (!indexType.isError() && !indexType.isBasic("inteiro")) {
                    type = sem.errorType();
                }
                if (!type.isArray()) {
                    sem.reportUndeclared(base, text.toString());
                    return new ExprResult(sem.errorType(), text.toString(), false, base);
                }
                type = sem.arrayElement(type);
                continue;
            }

            break;
        }

        if (derefCount > 0 && type.isError()) {
            return new ExprResult(type, text.toString(), false, base);
        }

        return new ExprResult(type, text.toString(), assignable, base);
    }

    private void skipSuffixes(StringBuilder text) {
        int depth = 0;
        while (true) {
            if (match(TokenType.PONTO)) {
                Token field = expect(TokenType.IDENT);
                text.append('.').append(field.lexeme);
                continue;
            }
            if (match(TokenType.ABRE_COL)) {
                text.append('[');
                depth++;
                while (depth > 0 && !check(TokenType.FECHA_COL) && peek().type != TokenType.EOF) {
                    advance();
                }
                if (match(TokenType.FECHA_COL)) {
                    text.append(']');
                }
                continue;
            }
            break;
        }
    }

    private SemanticAnalyzer.TypeInfo combineArithmetic(SemanticAnalyzer.TypeInfo left,
            SemanticAnalyzer.TypeInfo right, Token op) {
        if (left.isError() || right.isError()) {
            return sem.errorType();
        }
        if (left.isLiteral() && right.isLiteral()) {
            return op.type == TokenType.SOMA ? sem.basicLiteral() : sem.errorType();
        }
        if (left.isNumeric() && right.isNumeric()) {
            if (left.isBasic("real") || right.isBasic("real")) {
                return sem.basicReal();
            }
            return sem.basicInteger();
        }
        return sem.errorType();
    }

    private boolean isOperadorRelacional(Token token) {
        return token.type == TokenType.IGUAL || token.type == TokenType.DIFERENTE
            || token.type == TokenType.MAIOR || token.type == TokenType.MAIOR_IGUAL
            || token.type == TokenType.MENOR || token.type == TokenType.MENOR_IGUAL;
    }

    private boolean isRecordFieldStart(Token token) {
        return token.type == TokenType.IDENT || token.type == TokenType.CIRCUNFLEXO;
    }

    private boolean isLocalDeclarationStart(Token token) {
        return token.type == TokenType.DECLARE || token.type == TokenType.CONSTANTE
            || token.type == TokenType.TIPO;
    }

    private boolean isGlobalDeclarationStart(Token token) {
        return isLocalDeclarationStart(token) || token.type == TokenType.PROCEDIMENTO
            || token.type == TokenType.FUNCAO;
    }

    private boolean isCommandStart(Token token) {
        return token.type == TokenType.LEIA || token.type == TokenType.ESCREVA
            || token.type == TokenType.SE || token.type == TokenType.CASO
            || token.type == TokenType.PARA || token.type == TokenType.ENQUANTO
            || token.type == TokenType.FACA || token.type == TokenType.RETORNE
            || token.type == TokenType.IDENT || token.type == TokenType.CIRCUNFLEXO
            || token.type == TokenType.E_COMERCIAL;
    }

    private boolean isNumeroIntervaloStart(Token token) {
        return token.type == TokenType.SOMA || token.type == TokenType.SUB
            || token.type == TokenType.NUM_INT;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token advance() {
        if (current < tokens.size() - 1) {
            current++;
        }
        return tokens.get(current - 1);
    }

    private boolean check(TokenType type) {
        return peek().type == type;
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private Token expect(TokenType type) {
        if (!match(type)) {
            error(peek());
        }
        return previous();
    }

    private void error(Token token) {
        throw new SyntaxException(token);
    }

    private static final class TypeRecordBuilder {
        final SemanticAnalyzer.TypeInfo type;

        TypeRecordBuilder(String name) {
            this.type = SemanticAnalyzer.TypeInfo.record(name);
        }
    }

    static final class SyntaxException extends RuntimeException {
        final Token token;

        SyntaxException(Token token) {
            this.token = token;
        }
    }

    static final class SemanticException extends RuntimeException {
        SemanticException(String message) {
            super(message);
        }
    }
}
