import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
            List<Token> tokens = lexer.tokenizar();
            Parser parser = new Parser(tokens);
            parser.programa();
            return "";
        } catch (Lexer.LexicalException e) {
            return "Linha " + e.line + ": " + e.message + "\nFim da compilacao\n";
        } catch (Parser.SyntaxException e) {
            return "Linha " + e.token.line + ": erro sintatico proximo a " + e.token.lexeme
                + "\nFim da compilacao\n";
        } catch (Parser.SemanticException e) {
            return e.getMessage();
        }
    }
}
