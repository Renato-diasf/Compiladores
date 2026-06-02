import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

final class AntlrTokenAdapter {
    private AntlrTokenAdapter() {
    }

    static List<Token> tokenizar(String fonte) {
        LALexer lexer = new LALexer(CharStreams.fromString(fonte));
        lexer.removeErrorListeners();

        CommonTokenStream stream = new CommonTokenStream(lexer);
        stream.fill();

        List<Token> tokens = new ArrayList<>();
        for (org.antlr.v4.runtime.Token antlrToken : stream.getTokens()) {
            if (antlrToken.getType() == org.antlr.v4.runtime.Token.EOF) {
                tokens.add(new Token(TokenType.EOF, "EOF", antlrToken.getLine()));
                continue;
            }

            Token token = convert(antlrToken);
            tokens.add(token);
        }
        return tokens;
    }

    private static Token convert(org.antlr.v4.runtime.Token token) {
        int type = token.getType();
        String text = token.getText();
        int line = token.getLine();

        if (type == LALexer.ERRO_COMENTARIO) {
            throw new LexicalException(line, "comentario nao fechado");
        }
        if (type == LALexer.ERRO_CADEIA) {
            throw new LexicalException(line, "cadeia literal nao fechada");
        }
        if (type == LALexer.ERRO_SIMBOLO) {
            throw new LexicalException(line, text + " - simbolo nao identificado");
        }

        return new Token(TokenType.valueOf(LALexer.VOCABULARY.getSymbolicName(type)), text, line);
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
