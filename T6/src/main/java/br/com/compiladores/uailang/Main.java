package br.com.compiladores.uailang;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            return;
        }

        String fonte = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8);
        if (!fonte.isEmpty() && fonte.charAt(0) == '\uFEFF') {
            fonte = fonte.substring(1);
        }

        String resultado = compilar(fonte);
        Files.writeString(Path.of(args[1]), resultado, StandardCharsets.UTF_8);
    }

    static String compilar(String fonte) {
        SyntaxErrorListener erros = new SyntaxErrorListener();
        UaiLangLexer lexer = new UaiLangLexer(CharStreams.fromString(fonte));
        lexer.removeErrorListeners();
        lexer.addErrorListener(erros);

        UaiLangParser parser = new UaiLangParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(erros);

        UaiLangParser.ProgramaContext arvore = parser.programa();
        if (erros.hasErrors()) {
            return erros.format();
        }

        CompilerVisitor visitor = new CompilerVisitor();
        String codigo = visitor.compile(arvore);
        if (visitor.hasErrors()) {
            return visitor.formatErrors();
        }
        return codigo;
    }
}
