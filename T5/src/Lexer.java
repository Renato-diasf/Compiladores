import java.util.ArrayList;
import java.util.List;

final class Lexer {
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

    static final class LexicalException extends RuntimeException {
        final int line;
        final String message;

        LexicalException(int line, String message) {
            this.line = line;
            this.message = message;
        }
    }
}
