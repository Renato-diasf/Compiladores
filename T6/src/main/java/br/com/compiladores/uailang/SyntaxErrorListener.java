package br.com.compiladores.uailang;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

final class SyntaxErrorListener extends BaseErrorListener {
    private final List<String> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) {
        errors.add("Linha " + line + ": erro sintatico proximo a coluna " + charPositionInLine
            + " (" + msg + ")");
    }

    boolean hasErrors() {
        return !errors.isEmpty();
    }

    String format() {
        StringBuilder out = new StringBuilder();
        for (String error : errors) {
            out.append(error).append('\n');
        }
        out.append("Fim da compilacao\n");
        return out.toString();
    }
}
