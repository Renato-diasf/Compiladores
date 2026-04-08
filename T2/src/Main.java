import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
            return "Linha " + e.line + ": " + e.message + "\nFim da compilacao\n";
        } catch (SyntaxException e) {
            Token token = e.token;
            return "Linha " + token.line + ": erro sintatico proximo a " + token.lexeme
                + "\nFim da compilacao\n";
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

    private static final class Parser {
        private final List<Token> tokens;
        private int current;

        Parser(List<Token> tokens) {
            this.tokens = tokens;
        }

        void programa() {
            while (isDeclaracaoLocalGlobalStart(peek())) {
                declaracaoLocalGlobal();
            }

            expect(TokenType.ALGORITMO);
            corpo();
            expect(TokenType.FIM_ALGORITMO);
            expect(TokenType.EOF);
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
                expect(TokenType.IDENT);
                expect(TokenType.DOIS_PONTOS);
                tipoBasico();
                expect(TokenType.IGUAL);
                valorConstante();
                return;
            }

            if (match(TokenType.TIPO)) {
                expect(TokenType.IDENT);
                expect(TokenType.DOIS_PONTOS);
                tipo();
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
            identificador();
            while (match(TokenType.VIRGULA)) {
                identificador();
            }
            expect(TokenType.DOIS_PONTOS);
            tipo();
        }

        private void tipo() {
            if (isTipoBasico(peek())) {
                advance();
                return;
            }

            if (match(TokenType.REGISTRO)) {
                while (check(TokenType.IDENT)) {
                    variavel();
                }
                expect(TokenType.FIM_REGISTRO);
                return;
            }

            tipoEstendido();
        }

        private void tipoBasico() {
            if (match(TokenType.LITERAL) || match(TokenType.INTEIRO)
                || match(TokenType.REAL) || match(TokenType.LOGICO)) {
                return;
            }
            error(peek());
        }

        private void tipoEstendido() {
            while (match(TokenType.CIRCUNFLEXO)) {
                // ponteiro opcional
            }

            if (match(TokenType.LITERAL) || match(TokenType.INTEIRO)
                || match(TokenType.REAL) || match(TokenType.LOGICO)
                || match(TokenType.IDENT)) {
                return;
            }

            error(peek());
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
                // desreferencia/prefixo de ponteiro
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

        private void cmd() {
            if (match(TokenType.LEIA)) {
                expect(TokenType.ABRE_PAR);
                identificador();
                while (match(TokenType.VIRGULA)) {
                    identificador();
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
                identificador();
                if (match(TokenType.ATRIBUICAO)) {
                    expressao();
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

        private void expressao() {
            termoLogico();
            while (match(TokenType.OU)) {
                termoLogico();
            }
        }

        private void termoLogico() {
            fatorLogico();
            while (match(TokenType.E)) {
                fatorLogico();
            }
        }

        private void fatorLogico() {
            match(TokenType.NAO);
            parcelaLogica();
        }

        private void parcelaLogica() {
            if (match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
                return;
            }
            expRelacional();
        }

        private void expRelacional() {
            expAritmetica();
            if (isOperadorRelacional(peek())) {
                advance();
                expAritmetica();
            }
        }

        private void expAritmetica() {
            termo();
            while (match(TokenType.SOMA) || match(TokenType.SUB)) {
                termo();
            }
        }

        private void termo() {
            fator();
            while (match(TokenType.MULT) || match(TokenType.DIV) || match(TokenType.MOD)) {
                fator();
            }
        }

        private void fator() {
            parcela();
            while (match(TokenType.CIRCUNFLEXO)) {
                parcela();
            }
        }

        private void parcela() {
            boolean hasUnary = match(TokenType.SOMA) || match(TokenType.SUB);

            if (match(TokenType.NUM_INT) || match(TokenType.NUM_REAL) || match(TokenType.CADEIA)
                || match(TokenType.VERDADEIRO) || match(TokenType.FALSO)) {
                return;
            }

            if (match(TokenType.E_COMERCIAL)) {
                identificador();
                return;
            }

            if (match(TokenType.ABRE_PAR)) {
                expressao();
                expect(TokenType.FECHA_PAR);
                return;
            }

            if (check(TokenType.IDENT) || check(TokenType.CIRCUNFLEXO)) {
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
                return;
            }

            if (hasUnary) {
                error(peek());
            }
            error(peek());
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
