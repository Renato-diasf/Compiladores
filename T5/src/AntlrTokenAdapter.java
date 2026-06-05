import java.util.List;

final class AntlrTokenAdapter {
    private AntlrTokenAdapter() {
    }

    static List<Token> tokenizar(String fonte) {
        try {
            return new Lexer(fonte).tokenizar();
        } catch (Lexer.LexicalException e) {
            throw new LexicalException(e.line, e.message);
        }
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
