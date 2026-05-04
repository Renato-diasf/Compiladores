import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            return;
        }

        Path entrada = Path.of(args[0]);
        Path saida = Path.of(args[1]);

        String fonte = Files.readString(entrada, StandardCharsets.UTF_8);
        if (!fonte.isEmpty() && fonte.charAt(0) == '\uFEFF') {
            fonte = fonte.substring(1);
        }

        String resultado = analisar(fonte);
        Files.writeString(saida, resultado, StandardCharsets.UTF_8);
    }

    private static String analisar(String fonte) {
        try {
            Lexer lexer = new Lexer(fonte);
            Parser parser = new Parser(lexer.tokenizar());
            parser.programa();
            return "";
        } catch (LexicalException e) {
            return "Linha " + e.line + ": " + e.message + "\r\nFim da compilacao\r\n";
        } catch (SyntaxException e) {
            Token token = e.token;
            return "Linha " + token.line + ": erro sintatico proximo a " + token.lexeme
                + "\r\nFim da compilacao\r\n";
        } catch (SemanticException e) {
            return e.getMessage();
        }
    }

    private enum TokenType {
        ALGORITMO("algoritmo"),
        FIM_ALGORITMO("fim_algoritmo"),
        DECLARE("declare"),
        LITERAL("literal"),
        INTEIRO("inteiro"),
        REAL("real"),
        LOGICO("logico"),
        LEIA("leia"),
        ESCREVA("escreva"),
        SE("se"),
        ENTAO("entao"),
        SENAO("senao"),
        FIM_SE("fim_se"),
        CASO("caso"),
        SEJA("seja"),
        FIM_CASO("fim_caso"),
        PARA("para"),
        ATE("ate"),
        FACA("faca"),
        FIM_PARA("fim_para"),
        ENQUANTO("enquanto"),
        FIM_ENQUANTO("fim_enquanto"),
        REGISTRO("registro"),
        FIM_REGISTRO("fim_registro"),
        TIPO("tipo"),
        PROCEDIMENTO("procedimento"),
        FIM_PROCEDIMENTO("fim_procedimento"),
        FUNCAO("funcao"),
        FIM_FUNCAO("fim_funcao"),
        VAR("var"),
        CONSTANTE("constante"),
        RETORNE("retorne"),
        NAO("nao"),
        E("e"),
        OU("ou"),
        VERDADEIRO("verdadeiro"),
        FALSO("falso"),
        IDENT("IDENT"),
        NUM_INT("NUM_INT"),
        NUM_REAL("NUM_REAL"),
        CADEIA("CADEIA"),
        ABRE_PAR("("),
        FECHA_PAR(")"),
        ABRE_COL("["),
        FECHA_COL("]"),
        VIRGULA(","),
        DOIS_PONTOS(":"),
        PONTO("."),
        ATRIBUICAO("<-"),
        MENOR("<"),
        MENOR_IGUAL("<="),
        MAIOR(">"),
        MAIOR_IGUAL(">="),
        DIFERENTE("<>"),
        IGUAL("="),
        SOMA("+"),
        SUB("-"),
        MULT("*"),
        DIV("/"),
        MOD("%"),
        CIRCUNFLEXO("^"),
        E_COMERCIAL("&"),
        INTERVALO(".."),
        EOF("EOF");

        final String image;

        TokenType(String image) {
            this.image = image;
        }
    }

    private static final class Token {
        final TokenType type;
        final String lexeme;
        final int line;

        Token(TokenType type, String lexeme, int line) {
            this.type = type;
            this.lexeme = lexeme;
            this.line = line;
        }
    }

    private static final class LexicalException extends RuntimeException {
        final int line;
        final String message;

        LexicalException(int line, String message) {
            this.line = line;
            this.message = message;
        }
    }

    private static final class SyntaxException extends RuntimeException {
        final Token token;

        SyntaxException(Token token) {
            this.token = token;
        }
    }

    private static final class SemanticException extends RuntimeException {
        SemanticException(String message) {
            super(message);
        }
    }

    private static final class Symbol {
        final String name;
        final String type;
        final int line;

        Symbol(String name, String type, int line) {
            this.name = name;
            this.type = type;
            this.line = line;
        }
    }

    private static final class Lexer {
        private final String input;
        private int index;
        private int line;

        Lexer(String input) {
            this.input = input;
            this.index = 0;
            this.line = 1;
        }

        List<Token> tokenizar() {
            List<Token> tokens = new ArrayList<>();

            while (!isAtEnd()) {
                char current = peek();

                if (current == ' ' || current == '\t' || current == '\r') {
                    advance();
                    continue;
                }

                if (current == '\n') {
                    line++;
                    advance();
                    continue;
                }

                if (current == '{') {
                    readComment();
                    continue;
                }

                if (current == '"') {
                    tokens.add(readString());
                    continue;
                }

                if (Character.isLetter(current) || current == '_') {
                    tokens.add(readIdentifier());
                    continue;
                }

                if (Character.isDigit(current)) {
                    tokens.add(readNumber());
                    continue;
                }

                tokens.add(readSymbol());
            }

            tokens.add(new Token(TokenType.EOF, "EOF", line));
            return tokens;
        }

        private void readComment() {
            int startLine = line;
            advance();

            while (!isAtEnd()) {
                char current = peek();
                if (current == '}') {
                    advance();
                    return;
                }
                if (current == '\n') {
                    throw new LexicalException(startLine, "comentario nao fechado");
                }
                advance();
            }

            throw new LexicalException(startLine, "comentario nao fechado");
        }

        private Token readString() {
            int startLine = line;
            StringBuilder builder = new StringBuilder();
            builder.append(advance());

            while (!isAtEnd()) {
                char current = peek();
                if (current == '"') {
                    builder.append(advance());
                    return new Token(TokenType.CADEIA, builder.toString(), startLine);
                }
                if (current == '\n') {
                    throw new LexicalException(startLine, "cadeia literal nao fechada");
                }
                builder.append(advance());
            }

            throw new LexicalException(startLine, "cadeia literal nao fechada");
        }

        private Token readIdentifier() {
            int start = index;
            while (!isAtEnd() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
                advance();
            }

            String lexeme = input.substring(start, index);
            TokenType type = switch (lexeme) {
                case "algoritmo" -> TokenType.ALGORITMO;
                case "fim_algoritmo" -> TokenType.FIM_ALGORITMO;
                case "declare" -> TokenType.DECLARE;
                case "literal" -> TokenType.LITERAL;
                case "inteiro" -> TokenType.INTEIRO;
                case "real" -> TokenType.REAL;
                case "logico" -> TokenType.LOGICO;
                case "leia" -> TokenType.LEIA;
                case "escreva" -> TokenType.ESCREVA;
                case "se" -> TokenType.SE;
                case "entao" -> TokenType.ENTAO;
                case "senao" -> TokenType.SENAO;
                case "fim_se" -> TokenType.FIM_SE;
                case "caso" -> TokenType.CASO;
                case "seja" -> TokenType.SEJA;
                case "fim_caso" -> TokenType.FIM_CASO;
                case "para" -> TokenType.PARA;
                case "ate" -> TokenType.ATE;
                case "faca" -> TokenType.FACA;
                case "fim_para" -> TokenType.FIM_PARA;
                case "enquanto" -> TokenType.ENQUANTO;
                case "fim_enquanto" -> TokenType.FIM_ENQUANTO;
                case "registro" -> TokenType.REGISTRO;
                case "fim_registro" -> TokenType.FIM_REGISTRO;
                case "tipo" -> TokenType.TIPO;
                case "procedimento" -> TokenType.PROCEDIMENTO;
                case "fim_procedimento" -> TokenType.FIM_PROCEDIMENTO;
                case "funcao" -> TokenType.FUNCAO;
                case "fim_funcao" -> TokenType.FIM_FUNCAO;
                case "var" -> TokenType.VAR;
                case "constante" -> TokenType.CONSTANTE;
                case "retorne" -> TokenType.RETORNE;
                case "nao" -> TokenType.NAO;
                case "e" -> TokenType.E;
                case "ou" -> TokenType.OU;
                case "verdadeiro" -> TokenType.VERDADEIRO;
                case "falso" -> TokenType.FALSO;
                default -> TokenType.IDENT;
            };

            return new Token(type, lexeme, line);
        }

        private Token readNumber() {
            int start = index;
            while (!isAtEnd() && Character.isDigit(peek())) {
                advance();
            }

            TokenType type = TokenType.NUM_INT;
            if (!isAtEnd() && peek() == '.' && !nextIs('.')) {
                int dotIndex = index;
                advance();
                if (!isAtEnd() && Character.isDigit(peek())) {
                    type = TokenType.NUM_REAL;
                    while (!isAtEnd() && Character.isDigit(peek())) {
                        advance();
                    }
                } else {
                    index = dotIndex;
                }
            }

            return new Token(type, input.substring(start, index), line);
        }

        private Token readSymbol() {
            int currentLine = line;

            if (match("<-")) {
                return new Token(TokenType.ATRIBUICAO, "<-", currentLine);
            }
            if (match("<=")) {
                return new Token(TokenType.MENOR_IGUAL, "<=", currentLine);
            }
            if (match(">=")) {
                return new Token(TokenType.MAIOR_IGUAL, ">=", currentLine);
            }
            if (match("<>")) {
                return new Token(TokenType.DIFERENTE, "<>", currentLine);
            }
            if (match("..")) {
                return new Token(TokenType.INTERVALO, "..", currentLine);
            }

            char current = advance();
            TokenType type = switch (current) {
                case '(' -> TokenType.ABRE_PAR;
                case ')' -> TokenType.FECHA_PAR;
                case '[' -> TokenType.ABRE_COL;
                case ']' -> TokenType.FECHA_COL;
                case ',' -> TokenType.VIRGULA;
                case ':' -> TokenType.DOIS_PONTOS;
                case '.' -> TokenType.PONTO;
                case '<' -> TokenType.MENOR;
                case '>' -> TokenType.MAIOR;
                case '=' -> TokenType.IGUAL;
                case '+' -> TokenType.SOMA;
                case '-' -> TokenType.SUB;
                case '*' -> TokenType.MULT;
                case '/' -> TokenType.DIV;
                case '%' -> TokenType.MOD;
                case '^' -> TokenType.CIRCUNFLEXO;
                case '&' -> TokenType.E_COMERCIAL;
                default -> null;
            };

            if (type == null) {
                throw new LexicalException(currentLine, current + " - simbolo nao identificado");
            }

            return new Token(type, String.valueOf(current), currentLine);
        }

        private boolean isAtEnd() {
            return index >= input.length();
        }

        private char peek() {
            return input.charAt(index);
        }

        private boolean nextIs(char expected) {
            return index + 1 < input.length() && input.charAt(index + 1) == expected;
        }

        private char advance() {
            return input.charAt(index++);
        }

        private boolean match(String expected) {
            if (input.startsWith(expected, index)) {
                index += expected.length();
                return true;
            }
            return false;
        }
    }

    private static final class SemanticAnalyzer {
        final Map<String, Symbol> symbolTable = new HashMap<>();
        final StringBuilder errors = new StringBuilder();
        int errorCount = 0;

        void addSymbol(String name, String type, int line) {
            if (symbolTable.containsKey(name)) {
                errors.append("Linha ").append(line).append(": identificador ").append(name)
                    .append(" ja declarado anteriormente\r\n");
                errorCount++;
                return;
            }
            
            // Add symbol anyway to avoid cascading errors
            symbolTable.put(name, new Symbol(name, type, line));
            
            // Now check if type is valid
            if ("erro".equals(type)) {
                // Error already reported by tipo()
                return;
            }
            
            if (isBasicType(type)) {
                // OK, basic type
                return;
            }
            
            // User-defined type
            if (!isTypeKnown(type)) {
                errors.append("Linha ").append(line).append(": tipo ").append(type)
                    .append(" nao declarado\r\n");
                errorCount++;
            }
        }

        void declareType(String name, String baseType, int line) {
            if (symbolTable.containsKey(name)) {
                errors.append("Linha ").append(line).append(": identificador ").append(name)
                    .append(" ja declarado anteriormente\r\n");
                errorCount++;
            } else {
                symbolTable.put(name, new Symbol(name, baseType, line));
            }
        }

        String getType(String name, int line) {
            if (!symbolTable.containsKey(name)) {
                errors.append("Linha ").append(line).append(": identificador ").append(name)
                    .append(" nao declarado\r\n");
                errorCount++;
                return "erro";
            }
            return symbolTable.get(name).type;
        }

        void checkTypeCompatibility(String varName, String assignmentType, int line) {
            if (!symbolTable.containsKey(varName)) {
                errors.append("Linha ").append(line).append(": identificador ").append(varName)
                    .append(" nao declarado\r\n");
                errorCount++;
                return;
            }

            String varType = symbolTable.get(varName).type;
            if (!isCompatible(varType, assignmentType)) {
                errors.append("Linha ").append(line).append(": atribuicao nao compativel para ")
                    .append(varName).append("\r\n");
                errorCount++;
            }
        }

        public boolean isBasicType(String type) {
            return "literal".equals(type) || "inteiro".equals(type) || 
                   "real".equals(type) || "logico".equals(type);
        }

        private boolean isTypeKnown(String type) {
            return isBasicType(type) || symbolTable.containsKey(type);
        }

        private boolean isCompatible(String targetType, String sourceType) {
            if ("erro".equals(targetType) || "erro".equals(sourceType)) {
                return false;
            }

            // allow implicit integer -> real promotion
            if ("real".equals(targetType) && "inteiro".equals(sourceType)) {
                return true;
            }

            return targetType.equals(sourceType);
        }

        boolean hasErrors() {
            return errorCount > 0;
        }

        String getErrors() {
            return errors.toString() + (errorCount > 0 ? "Fim da compilacao\r\n" : "");
        }
    }

    private static final class Parser {
        private final List<Token> tokens;
        private int current;
        private final SemanticAnalyzer semanticAnalyzer;

        Parser(List<Token> tokens) {
            this.tokens = tokens;
            this.semanticAnalyzer = new SemanticAnalyzer();
        }

        void programa() {
            while (isDeclaracaoLocalGlobalStart(peek())) {
                declaracaoLocalGlobal();
            }

            expect(TokenType.ALGORITMO);
            corpo();
            expect(TokenType.FIM_ALGORITMO);
            expect(TokenType.EOF);

            if (semanticAnalyzer.hasErrors()) {
                throw new SemanticException(semanticAnalyzer.getErrors());
            }
        }

        private void corpo() {
            while (isDeclaracaoLocalStart(peek())) {
                declaracaoLocal();
            }

            while (isCommandStart(peek())) {
                cmd();
            }
        }

        private void declaracaoLocalGlobal() {
            if (check(TokenType.PROCEDIMENTO) || check(TokenType.FUNCAO)) {
                declaracaoGlobal();
            } else {
                declaracaoLocal();
            }
        }

        private void declaracaoLocal() {
            if (match(TokenType.DECLARE)) {
                variavel();
                return;
            }

            if (match(TokenType.CONSTANTE)) {
                Token name = peek();
                expect(TokenType.IDENT);
                expect(TokenType.DOIS_PONTOS);
                String type = tipoBasico();
                expect(TokenType.IGUAL);
                valorConstante();
                semanticAnalyzer.addSymbol(name.lexeme, type, name.line);
                return;
            }

            if (match(TokenType.TIPO)) {
                Token name = peek();
                expect(TokenType.IDENT);
                expect(TokenType.DOIS_PONTOS);
                String type = tipo();
                semanticAnalyzer.declareType(name.lexeme, type, name.line);
                return;
            }

            error(peek());
        }

        private void declaracaoGlobal() {
            if (match(TokenType.PROCEDIMENTO)) {
                expect(TokenType.IDENT);
                expect(TokenType.ABRE_PAR);
                if (!check(TokenType.FECHA_PAR)) {
                    parametros();
                }
                expect(TokenType.FECHA_PAR);
                corpo();
                expect(TokenType.FIM_PROCEDIMENTO);
                return;
            }

            if (match(TokenType.FUNCAO)) {
                expect(TokenType.IDENT);
                expect(TokenType.ABRE_PAR);
                if (!check(TokenType.FECHA_PAR)) {
                    parametros();
                }
                expect(TokenType.FECHA_PAR);
                expect(TokenType.DOIS_PONTOS);
                tipoEstendido();
                corpo();
                expect(TokenType.FIM_FUNCAO);
                return;
            }

            error(peek());
        }

        private void parametros() {
            parametro();
            while (match(TokenType.VIRGULA)) {
                parametro();
            }
        }

        private void parametro() {
            match(TokenType.VAR);
            identificador();
            while (match(TokenType.VIRGULA)) {
                identificador();
            }
            expect(TokenType.DOIS_PONTOS);
            tipoEstendido();
        }

        private void variavel() {
            List<Token> names = new ArrayList<>();
            Token name = peek();
            identificadorName(name);
            names.add(name);

            while (match(TokenType.VIRGULA)) {
                name = peek();
                identificadorName(name);
                names.add(name);
            }
            expect(TokenType.DOIS_PONTOS);
            int typeLine = peek().line;
            String type = tipo();

            for (Token n : names) {
                // Add symbol even if type is error to avoid cascading errors
                semanticAnalyzer.addSymbol(n.lexeme, type, n.line);
            }
        }

        private void identificadorName(Token token) {
            while (match(TokenType.CIRCUNFLEXO)) {
            }
            expect(TokenType.IDENT);
            while (match(TokenType.PONTO)) {
                expect(TokenType.IDENT);
            }
            while (match(TokenType.ABRE_COL)) {
                expressao();
                while (match(TokenType.VIRGULA)) {
                    expressao();
                }
                expect(TokenType.FECHA_COL);
            }
        }

        private String tipo() {
            if (isTipoBasico(peek())) {
                String type = peek().lexeme;
                advance();
                return type;
            }

            if (match(TokenType.REGISTRO)) {
                while (check(TokenType.IDENT)) {
                    variavel();
                }
                expect(TokenType.FIM_REGISTRO);
                return "registro";
            }

            return tipoEstendido();
        }

        private String tipoBasico() {
            if (match(TokenType.LITERAL)) {
                return "literal";
            }
            if (match(TokenType.INTEIRO)) {
                return "inteiro";
            }
            if (match(TokenType.REAL)) {
                return "real";
            }
            if (match(TokenType.LOGICO)) {
                return "logico";
            }
            error(peek());
            return "erro";
        }

        private String tipoEstendido() {
            while (match(TokenType.CIRCUNFLEXO)) {
            }

            if (match(TokenType.LITERAL)) {
                return "literal";
            }
            if (match(TokenType.INTEIRO)) {
                return "inteiro";
            }
            if (match(TokenType.REAL)) {
                return "real";
            }
            if (match(TokenType.LOGICO)) {
                return "logico";
            }
            if (check(TokenType.IDENT)) {
                Token typeToken = peek();
                String name = typeToken.lexeme;
                int line = typeToken.line;
                advance();
                
                if (semanticAnalyzer.isBasicType(name)) {
                    return name;
                }
                
                if (semanticAnalyzer.symbolTable.containsKey(name)) {
                    return name;
                }
                
                // Type not found
                semanticAnalyzer.errors.append("Linha ").append(line).append(": tipo ").append(name)
                    .append(" nao declarado\r\n");
                semanticAnalyzer.errorCount++;
                return "erro";
            }

            error(peek());
            return "erro";
        }

        private void valorConstante() {
            if (match(TokenType.CADEIA) || match(TokenType.NUM_INT)
                || match(TokenType.NUM_REAL) || match(TokenType.VERDADEIRO)
                || match(TokenType.FALSO)) {
                return;
            }

            if (match(TokenType.SUB)) {
                if (match(TokenType.NUM_INT) || match(TokenType.NUM_REAL)) {
                    return;
                }
            }

            error(peek());
        }

        private void identificador() {
            while (match(TokenType.CIRCUNFLEXO)) {
            }

            expect(TokenType.IDENT);

            while (match(TokenType.PONTO)) {
                expect(TokenType.IDENT);
            }

            while (match(TokenType.ABRE_COL)) {
                expressao();
                while (match(TokenType.VIRGULA)) {
                    expressao();
                }
                expect(TokenType.FECHA_COL);
            }
        }

        private void identificadorWithValidation() {
            while (match(TokenType.CIRCUNFLEXO)) {
            }

            Token idToken = peek();
            expect(TokenType.IDENT);
            semanticAnalyzer.getType(idToken.lexeme, idToken.line);

            while (match(TokenType.PONTO)) {
                expect(TokenType.IDENT);
            }

            while (match(TokenType.ABRE_COL)) {
                expressao();
                while (match(TokenType.VIRGULA)) {
                    expressao();
                }
                expect(TokenType.FECHA_COL);
            }
        }

        private void cmd() {
            if (match(TokenType.LEIA)) {
                expect(TokenType.ABRE_PAR);
                Token id1 = peek();
                identificadorWithValidation();
                while (match(TokenType.VIRGULA)) {
                    Token id = peek();
                    identificadorWithValidation();
                }
                expect(TokenType.FECHA_PAR);
                return;
            }

            if (match(TokenType.ESCREVA)) {
                expect(TokenType.ABRE_PAR);
                expressao();
                while (match(TokenType.VIRGULA)) {
                    expressao();
                }
                expect(TokenType.FECHA_PAR);
                return;
            }

            if (match(TokenType.SE)) {
                expressao();
                expect(TokenType.ENTAO);
                while (isCommandStart(peek())) {
                    cmd();
                }
                if (match(TokenType.SENAO)) {
                    while (isCommandStart(peek())) {
                        cmd();
                    }
                }
                expect(TokenType.FIM_SE);
                return;
            }

            if (match(TokenType.CASO)) {
                expAritmetica();
                expect(TokenType.SEJA);
                while (isNumeroIntervaloStart(peek())) {
                    itemSelecao();
                }
                if (match(TokenType.SENAO)) {
                    while (isCommandStart(peek())) {
                        cmd();
                    }
                }
                expect(TokenType.FIM_CASO);
                return;
            }

            if (match(TokenType.PARA)) {
                expect(TokenType.IDENT);
                expect(TokenType.ATRIBUICAO);
                expAritmetica();
                expect(TokenType.ATE);
                expAritmetica();
                expect(TokenType.FACA);
                while (isCommandStart(peek())) {
                    cmd();
                }
                expect(TokenType.FIM_PARA);
                return;
            }

            if (match(TokenType.ENQUANTO)) {
                expressao();
                expect(TokenType.FACA);
                while (isCommandStart(peek())) {
                    cmd();
                }
                expect(TokenType.FIM_ENQUANTO);
                return;
            }

            if (match(TokenType.FACA)) {
                while (isCommandStart(peek())) {
                    cmd();
                }
                expect(TokenType.ATE);
                expressao();
                return;
            }

            if (match(TokenType.RETORNE)) {
                expressao();
                return;
            }

            if (check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO)) {
                Token varName = peek();
                identificadorWithValidation();
                if (match(TokenType.ATRIBUICAO)) {
                    String type = expressao();
                    semanticAnalyzer.checkTypeCompatibility(varName.lexeme, type, varName.line);
                    return;
                }
                if (match(TokenType.ABRE_PAR)) {
                    if (!check(TokenType.FECHA_PAR)) {
                        expressao();
                        while (match(TokenType.VIRGULA)) {
                            expressao();
                        }
                    }
                    expect(TokenType.FECHA_PAR);
                    return;
                }
            }

            error(peek());
        }

        private void itemSelecao() {
            constantes();
            expect(TokenType.DOIS_PONTOS);
            while (isCommandStart(peek())) {
                cmd();
            }
        }

        private void constantes() {
            numeroIntervalo();
            while (match(TokenType.VIRGULA)) {
                numeroIntervalo();
            }
        }

        private void numeroIntervalo() {
            numeroInteiro();
            if (match(TokenType.INTERVALO)) {
                numeroInteiro();
            }
        }

        private void numeroInteiro() {
            match(TokenType.SOMA);
            match(TokenType.SUB);
            expect(TokenType.NUM_INT);
        }

        private String expressao() {
            String type = termoLogico();
            while (match(TokenType.OU)) {
                termoLogico();
            }
            return type;
        }

        private String termoLogico() {
            String type = fatorLogico();
            while (match(TokenType.E)) {
                fatorLogico();
            }
            return type;
        }

        private String fatorLogico() {
            match(TokenType.NAO);
            return parcelaLogica();
        }

        private String parcelaLogica() {
            if (match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
                return "logico";
            }
            return expRelacional();
        }

        private String expRelacional() {
            String type = expAritmetica();
            if (isOperadorRelacional(peek())) {
                advance();
                expAritmetica();
                return "logico";
            }
            return type;
        }

        private String expAritmetica() {
            String type = termo();
            while (check(TokenType.SOMA) || check(TokenType.SUB)) {
                Token op = advance();
                String right = termo();
                type = combineArithmeticTypes(type, right, op);
            }
            return type;
        }

        private String termo() {
            String type = fator();
            while (check(TokenType.MULT) || check(TokenType.DIV) || check(TokenType.MOD)) {
                Token op = advance();
                String right = fator();
                type = combineArithmeticTypes(type, right, op);
            }
            return type;
        }

        private String fator() {
            String type = parcela();
            while (match(TokenType.CIRCUNFLEXO)) {
                parcela();
            }
            return type;
        }

        private String parcela() {
            boolean hasUnary = match(TokenType.SOMA) || match(TokenType.SUB);

            if (match(TokenType.NUM_INT)) {
                return "inteiro";
            }
            if (match(TokenType.NUM_REAL)) {
                return "real";
            }
            if (match(TokenType.CADEIA)) {
                return "literal";
            }
            if (match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
                return "logico";
            }

            if (match(TokenType.E_COMERCIAL)) {
                identificador();
                return "inteiro";
            }

            if (match(TokenType.ABRE_PAR)) {
                String type = expressao();
                expect(TokenType.FECHA_PAR);
                return type;
            }

            if (check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO)) {
                Token name = peek();
                identificador();
                if (match(TokenType.ABRE_PAR)) {
                    if (!check(TokenType.FECHA_PAR)) {
                        expressao();
                        while (match(TokenType.VIRGULA)) {
                            expressao();
                        }
                    }
                    expect(TokenType.FECHA_PAR);
                }
                return semanticAnalyzer.getType(name.lexeme, name.line);
            }

            if (hasUnary) {
                error(peek());
            }
            error(peek());
            return "erro";
        }

        private boolean isTipoBasico(Token token) {
            return token.type == TokenType.LITERAL || token.type == TokenType.INTEIRO
                || token.type == TokenType.REAL || token.type == TokenType.LOGICO;
        }

        private boolean isOperadorRelacional(Token token) {
            return token.type == TokenType.IGUAL || token.type == TokenType.DIFERENTE
                || token.type == TokenType.MAIOR_IGUAL || token.type == TokenType.MENOR_IGUAL
                || token.type == TokenType.MAIOR || token.type == TokenType.MENOR;
        }

        private boolean isDeclaracaoLocalStart(Token token) {
            return token.type == TokenType.DECLARE || token.type == TokenType.CONSTANTE
                || token.type == TokenType.TIPO;
        }

        private boolean isDeclaracaoLocalGlobalStart(Token token) {
            return isDeclaracaoLocalStart(token) || token.type == TokenType.PROCEDIMENTO
                || token.type == TokenType.FUNCAO;
        }

        private boolean isCommandStart(Token token) {
            return token.type == TokenType.LEIA || token.type == TokenType.ESCREVA
                || token.type == TokenType.SE || token.type == TokenType.CASO
                || token.type == TokenType.PARA || token.type == TokenType.ENQUANTO
                || token.type == TokenType.FACA || token.type == TokenType.RETORNE
                || token.type == TokenType.IDENT || token.type == TokenType.CIRCUNFLEXO;
        }

        private boolean isNumeroIntervaloStart(Token token) {
            return token.type == TokenType.SOMA || token.type == TokenType.SUB
                || token.type == TokenType.NUM_INT;
        }

        private String combineArithmeticTypes(String left, String right, Token op) {
            if ("erro".equals(left) || "erro".equals(right)) {
                return "erro";
            }

            // both literals -> literal concatenation for + only
            if (left.equals("literal") && right.equals("literal")) {
                if (op.type == TokenType.SOMA) {
                    return "literal";
                }
                return "erro";
            }

            // numeric promotion: inteiro + real -> real
            boolean leftNum = left.equals("inteiro") || left.equals("real");
            boolean rightNum = right.equals("inteiro") || right.equals("real");

            if (leftNum && rightNum) {
                if (left.equals("real") || right.equals("real")) {
                    return "real";
                }
                return "inteiro";
            }

            // mixing literal and numeric -> propagate error without reporting here
            return "erro";
        }

        private Token peek() {
            return tokens.get(current);
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

        private void expect(TokenType type) {
            if (!match(type)) {
                error(peek());
            }
        }

        private void error(Token token) {
            throw new SyntaxException(token);
        }
    }
}
