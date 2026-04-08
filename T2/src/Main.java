import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "algoritmo", "fim_algoritmo", "declare", "literal", "inteiro", "real", "logico",
        "leia", "escreva", "se", "entao", "senao", "fim_se", "caso", "seja", "fim_caso",
        "para", "ate", "faca", "fim_para", "enquanto", "fim_enquanto", "registro",
        "fim_registro", "tipo", "procedimento", "fim_procedimento", "funcao", "fim_funcao",
        "var", "constante", "retorne", "nao", "e", "ou", "verdadeiro", "falso"
    ));

    private static final Set<String> DECL_START = new HashSet<>(Arrays.asList(
        "declare", "constante", "tipo", "procedimento", "funcao"
    ));

    private static final Set<String> CMD_START = new HashSet<>(Arrays.asList(
        "leia", "escreva", "se", "caso", "para", "enquanto", "faca", "retorne"
    ));

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            return;
        }

        Path entrada = Paths.get(args[0]);
        Path saida = Paths.get(args[1]);

        String conteudo = Files.readString(entrada, StandardCharsets.UTF_8);
        if (!conteudo.isEmpty() && conteudo.charAt(0) == '\uFEFF') {
            conteudo = conteudo.substring(1);
        }

        String resultado = analisarSintaticamente(conteudo);

        try (BufferedWriter writer = Files.newBufferedWriter(saida, StandardCharsets.UTF_8)) {
            writer.write(resultado);
        }
    }

    private static String analisarSintaticamente(String fonte) {
        try {
            Lexer lexer = new Lexer(fonte);
            List<Token> tokens = lexer.tokenizar();
            Parser parser = new Parser(tokens);
            parser.programa();
            return "";
        } catch (ParseException e) {
            Token t = e.token;
            String proximo = "EOF".equals(t.tipo) ? "EOF" : t.lexema;
            return "Linha " + t.linha + ": erro sintatico proximo a " + proximo + "\n";
        } catch (LexException e) {
            return "Linha " + e.linha + ": " + e.mensagem + "\n";
        }
    }

    private static final class Token {
        final String tipo;
        final String lexema;
        final int linha;

        Token(String tipo, String lexema, int linha) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linha = linha;
        }
    }

    private static final class LexException extends RuntimeException {
        final int linha;
        final String mensagem;

        LexException(int linha, String mensagem) {
            this.linha = linha;
            this.mensagem = mensagem;
        }
    }

    private static final class ParseException extends RuntimeException {
        final Token token;

        ParseException(Token token) {
            this.token = token;
        }
    }

    private static final class Lexer {
        private final String texto;
        private int i;
        private int linha;

        Lexer(String texto) {
            this.texto = texto;
            this.i = 0;
            this.linha = 1;
        }

        List<Token> tokenizar() {
            List<Token> tokens = new ArrayList<>();

            while (i < texto.length()) {
                char c = texto.charAt(i);

                if (c == ' ' || c == '\t' || c == '\r') {
                    i++;
                    continue;
                }

                if (c == '\n') {
                    linha++;
                    i++;
                    continue;
                }

                if (c == '{') {
                    int linhaComentario = linha;
                    i++;
                    boolean fechado = false;
                    while (i < texto.length()) {
                        char cc = texto.charAt(i);
                        if (cc == '}') {
                            fechado = true;
                            i++;
                            break;
                        }
                        if (cc == '\n') {
                            throw new LexException(linhaComentario, "comentario nao fechado");
                        }
                        i++;
                    }
                    if (!fechado) {
                        throw new LexException(linhaComentario, "comentario nao fechado");
                    }
                    continue;
                }

                if (c == '"') {
                    int linhaCadeia = linha;
                    StringBuilder sb = new StringBuilder();
                    sb.append(c);
                    i++;
                    boolean fechada = false;

                    while (i < texto.length()) {
                        char cc = texto.charAt(i);
                        if (cc == '"') {
                            sb.append(cc);
                            i++;
                            fechada = true;
                            break;
                        }
                        if (cc == '\n') {
                            throw new LexException(linhaCadeia, "cadeia literal nao fechada");
                        }
                        sb.append(cc);
                        i++;
                    }

                    if (!fechada) {
                        throw new LexException(linhaCadeia, "cadeia literal nao fechada");
                    }

                    tokens.add(new Token("CADEIA", sb.toString(), linhaCadeia));
                    continue;
                }

                if (Character.isLetter(c) || c == '_') {
                    int ini = i;
                    i++;
                    while (i < texto.length()) {
                        char cc = texto.charAt(i);
                        if (Character.isLetterOrDigit(cc) || cc == '_') {
                            i++;
                        } else {
                            break;
                        }
                    }
                    String lex = texto.substring(ini, i);
                    if (KEYWORDS.contains(lex)) {
                        tokens.add(new Token(lex, lex, linha));
                    } else {
                        tokens.add(new Token("IDENT", lex, linha));
                    }
                    continue;
                }

                if (Character.isDigit(c)) {
                    int ini = i;
                    while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                        i++;
                    }

                    String tipo = "NUM_INT";
                    if (i < texto.length() && texto.charAt(i) == '.') {
                        boolean intervalo = i + 1 < texto.length() && texto.charAt(i + 1) == '.';
                        if (!intervalo && i + 1 < texto.length() && Character.isDigit(texto.charAt(i + 1))) {
                            tipo = "NUM_REAL";
                            i++;
                            while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                                i++;
                            }
                        }
                    }

                    tokens.add(new Token(tipo, texto.substring(ini, i), linha));
                    continue;
                }

                if (c == '<') {
                    if (tem("<=")) {
                        tokens.add(new Token("<=", "<=", linha));
                        i += 2;
                    } else if (tem("<>") ) {
                        tokens.add(new Token("<>", "<>", linha));
                        i += 2;
                    } else if (tem("<-")) {
                        tokens.add(new Token("<-", "<-", linha));
                        i += 2;
                    } else {
                        tokens.add(new Token("<", "<", linha));
                        i++;
                    }
                    continue;
                }

                if (c == '>') {
                    if (tem(">=")) {
                        tokens.add(new Token(">=", ">=", linha));
                        i += 2;
                    } else {
                        tokens.add(new Token(">", ">", linha));
                        i++;
                    }
                    continue;
                }

                if (c == '.') {
                    if (tem("..")) {
                        tokens.add(new Token("..", "..", linha));
                        i += 2;
                    } else {
                        tokens.add(new Token(".", ".", linha));
                        i++;
                    }
                    continue;
                }

                if (":,;()[]^&+-*/=%".indexOf(c) >= 0) {
                    String s = Character.toString(c);
                    tokens.add(new Token(s, s, linha));
                    i++;
                    continue;
                }

                throw new LexException(linha, c + " - simbolo nao identificado");
            }

            tokens.add(new Token("EOF", "EOF", linha));
            return tokens;
        }

        private boolean tem(String s) {
            return i + s.length() <= texto.length() && texto.substring(i, i + s.length()).equals(s);
        }
    }

    private static final class Parser {
        private final List<Token> tokens;
        private int pos;

        Parser(List<Token> tokens) {
            this.tokens = tokens;
            this.pos = 0;
        }

        void programa() {
            while (isDeclaracaoGlobalInicio(atual()) || isDeclaracaoLocalInicio(atual())) {
                declaracaoLocalGlobal();
            }

            expect("algoritmo");

            corpoAlgoritmo();

            expect("fim_algoritmo");
            expect("EOF");
        }

        private void declaracaoLocalGlobal() {
            if (isDeclaracaoGlobalInicio(atual())) {
                declaracaoGlobal();
                return;
            }

            declaracaoLocal();
        }

        private void corpoAlgoritmo() {
            while (isDeclaracaoLocalInicio(atual())) {
                declaracaoLocal();
            }

            while (isComandoInicio(atual())) {
                comando();
            }
        }

        private void declaracaoLocal() {
            if (accept("declare")) {
                variavel();
                return;
            }

            if (accept("constante")) {
                expect("IDENT");
                expect(":");
                tipoEstendido();
                expect("=");
                valorConstante();
                return;
            }

            if (accept("tipo")) {
                expect("IDENT");
                expect(":");
                tipo();
                return;
            }

            erro(atual());
        }

        private void declaracaoGlobal() {
            if (accept("procedimento")) {
                expect("IDENT");
                if (accept("(")) {
                    if (!check(")")) {
                        parametros();
                    }
                    expect(")");
                }

                corpoAlgoritmo();
                expect("fim_procedimento");
                return;
            }

            if (accept("funcao")) {
                expect("IDENT");
                expect("(");
                if (!check(")")) {
                    parametros();
                }
                expect(")");
                expect(":");
                tipoEstendido();

                corpoAlgoritmo();
                expect("fim_funcao");
                return;
            }

            erro(atual());
        }

        private void parametros() {
            parametro();
            while (accept(",")) {
                parametro();
            }
        }

        private void parametro() {
            if (accept("var")) {
                // opcional
            }
            identificadores();
            expect(":");
            tipoEstendido();
        }

        private void variavel() {
            identificadores();
            expect(":");
            tipo();
        }

        private void identificadores() {
            identificador();
            while (accept(",")) {
                identificador();
            }
        }

        private void identificador() {
            while (accept("^")) {
                // ponteiro opcional em acesso
            }

            expect("IDENT");

            while (accept(".")) {
                expect("IDENT");
            }

            if (accept("[")) {
                expressao();
                while (accept(",")) {
                    expressao();
                }
                expect("]");
            }
        }

        private void tipo() {
            if (accept("literal") || accept("inteiro") || accept("real") || accept("logico")) {
                return;
            }

            if (accept("registro")) {
                while (check("IDENT")) {
                    variavel();
                }
                expect("fim_registro");
                return;
            }

            tipoEstendido();
        }

        private void tipoEstendido() {
            while (accept("^")) {
                // marcador de ponteiro
            }

            if (accept("literal") || accept("inteiro") || accept("real") || accept("logico") || accept("IDENT")) {
                return;
            }
            erro(atual());
        }

        private void valorConstante() {
            if (accept("CADEIA") || accept("NUM_INT") || accept("NUM_REAL") || accept("verdadeiro") || accept("falso")) {
                return;
            }
            if (accept("-")) {
                if (accept("NUM_INT") || accept("NUM_REAL")) {
                    return;
                }
            }
            erro(atual());
        }

        private void comando() {
            if (accept("leia")) {
                expect("(");
                identificador();
                while (accept(",")) {
                    identificador();
                }
                expect(")");
                return;
            }

            if (accept("escreva")) {
                expect("(");
                expressao();
                while (accept(",")) {
                    expressao();
                }
                expect(")");
                return;
            }

            if (accept("se")) {
                expressao();
                expect("entao");
                while (isComandoInicio(atual())) {
                    comando();
                }
                if (accept("senao")) {
                    while (isComandoInicio(atual())) {
                        comando();
                    }
                }
                expect("fim_se");
                return;
            }

            if (accept("caso")) {
                expAritmetica();
                expect("seja");
                while (isNumeroIntervaloInicio(atual())) {
                    selecaoItem();
                }
                if (accept("senao")) {
                    while (isComandoInicio(atual())) {
                        comando();
                    }
                }
                expect("fim_caso");
                return;
            }

            if (accept("para")) {
                expect("IDENT");
                expect("<-");
                expAritmetica();
                expect("ate");
                expAritmetica();
                expect("faca");
                while (isComandoInicio(atual())) {
                    comando();
                }
                expect("fim_para");
                return;
            }

            if (accept("enquanto")) {
                expressao();
                expect("faca");
                while (isComandoInicio(atual())) {
                    comando();
                }
                expect("fim_enquanto");
                return;
            }

            if (accept("faca")) {
                while (isComandoInicio(atual())) {
                    comando();
                }
                expect("ate");
                expressao();
                return;
            }

            if (accept("retorne")) {
                expressao();
                return;
            }

            if (check("IDENT") || check("^") ) {
                identificador();
                if (accept("<-") ) {
                    expressao();
                    return;
                }
                if (accept("(")) {
                    if (!check(")")) {
                        expressao();
                        while (accept(",")) {
                            expressao();
                        }
                    }
                    expect(")");
                    return;
                }
            }

            erro(atual());
        }

        private void selecaoItem() {
            numeroIntervalo();
            while (accept(",")) {
                numeroIntervalo();
            }
            expect(":");
            while (isComandoInicio(atual())) {
                comando();
            }
        }

        private void numeroIntervalo() {
            numeroInteiroComSinal();
            if (accept("..")) {
                numeroInteiroComSinal();
            }
        }

        private void numeroInteiroComSinal() {
            if (accept("+") || accept("-")) {
                // sinal opcional
            }
            expect("NUM_INT");
        }

        private void expressao() {
            termoLogico();
            while (accept("ou")) {
                termoLogico();
            }
        }

        private void termoLogico() {
            fatorLogico();
            while (accept("e")) {
                fatorLogico();
            }
        }

        private void fatorLogico() {
            accept("nao");
            parcelaLogica();
        }

        private void parcelaLogica() {
            if (accept("verdadeiro") || accept("falso")) {
                return;
            }
            expRelacional();
        }

        private void expRelacional() {
            expAritmetica();
            if (isOperadorRelacional(atual())) {
                avancar();
                expAritmetica();
            }
        }

        private void expAritmetica() {
            termo();
            while (check("+") || check("-")) {
                avancar();
                termo();
            }
        }

        private void termo() {
            fator();
            while (check("*") || check("/") || check("%")) {
                avancar();
                fator();
            }
        }

        private void fator() {
            parcela();
            while (check("^") ) {
                avancar();
                parcela();
            }
        }

        private void parcela() {
            if (accept("+") || accept("-")) {
                // sinal unario
            }

            if (accept("NUM_INT") || accept("NUM_REAL") || accept("CADEIA")) {
                return;
            }

            if (accept("verdadeiro") || accept("falso")) {
                return;
            }

            if (accept("&")) {
                identificador();
                return;
            }

            if (accept("(")) {
                expressao();
                expect(")");
                return;
            }

            if (check("IDENT") || check("^") ) {
                identificador();
                if (accept("(")) {
                    if (!check(")")) {
                        expressao();
                        while (accept(",")) {
                            expressao();
                        }
                    }
                    expect(")");
                }
                return;
            }

            erro(atual());
        }

        private boolean isOperadorRelacional(Token t) {
            return "=".equals(t.tipo) || "<>".equals(t.tipo) || ">=".equals(t.tipo)
                || "<=".equals(t.tipo) || ">".equals(t.tipo) || "<".equals(t.tipo);
        }

        private boolean isNumeroIntervaloInicio(Token t) {
            return "+".equals(t.tipo) || "-".equals(t.tipo) || "NUM_INT".equals(t.tipo);
        }

        private boolean isDeclaracaoLocalInicio(Token t) {
            return "declare".equals(t.tipo) || "constante".equals(t.tipo) || "tipo".equals(t.tipo);
        }

        private boolean isDeclaracaoGlobalInicio(Token t) {
            return "procedimento".equals(t.tipo) || "funcao".equals(t.tipo);
        }

        private boolean isDeclaracaoInicio(Token t) {
            return DECL_START.contains(t.tipo);
        }

        private boolean isComandoInicio(Token t) {
            return CMD_START.contains(t.tipo) || "IDENT".equals(t.tipo) || "^".equals(t.tipo);
        }

        private Token atual() {
            return tokens.get(pos);
        }

        private void avancar() {
            if (pos < tokens.size() - 1) {
                pos++;
            }
        }

        private boolean check(String tipo) {
            return tipo.equals(atual().tipo);
        }

        private boolean accept(String tipo) {
            if (check(tipo)) {
                avancar();
                return true;
            }
            return false;
        }

        private void expect(String tipo) {
            if (!accept(tipo)) {
                erro(atual());
            }
        }

        private void erro(Token t) {
            throw new ParseException(t);
        }
    }
}
