import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final Set<String> PALAVRAS_CHAVE = new HashSet<>(Arrays.asList(
        "algoritmo", "fim_algoritmo", "declare", "literal", "inteiro", "real", "logico",
        "leia", "escreva", "se", "entao", "senao", "fim_se", "caso", "seja", "fim_caso",
        "para", "ate", "faca", "fim_para", "enquanto", "fim_enquanto", "registro",
        "fim_registro", "tipo", "procedimento", "fim_procedimento", "funcao", "fim_funcao",
        "var", "constante", "retorne", "nao", "e", "ou", "verdadeiro", "falso"
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

        try (BufferedWriter writer = Files.newBufferedWriter(saida, StandardCharsets.UTF_8)) {
            analisar(conteudo, writer);
        }
    }

    private static void analisar(String texto, BufferedWriter writer) throws IOException {
        int i = 0;
        int linha = 1;

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

                // Em LA, comentario deve ser fechado na mesma linha.
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (cc == '}') {
                        fechado = true;
                        i++;
                        break;
                    }

                    if (cc == '\n') {
                        escreverErroComentario(writer, linhaComentario);
                        return;
                    }

                    i++;
                }

                if (!fechado) {
                    escreverErroComentario(writer, linhaComentario);
                    return;
                }

                continue;
            }

            if (c == '"') {
                int linhaCadeia = linha;
                StringBuilder cadeia = new StringBuilder();
                cadeia.append(c);
                i++;

                boolean fechada = false;
                // Cadeia literal nao pode atravessar quebra de linha.
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (cc == '"') {
                        cadeia.append(cc);
                        i++;
                        fechada = true;
                        break;
                    }

                    if (cc == '\n') {
                        escreverErroCadeia(writer, linhaCadeia);
                        return;
                    }

                    cadeia.append(cc);
                    i++;
                }

                if (!fechada) {
                    escreverErroCadeia(writer, linhaCadeia);
                    return;
                }

                escreverToken(writer, cadeia.toString(), "CADEIA");
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int inicio = i;
                i++;
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (Character.isLetterOrDigit(cc) || cc == '_') {
                        i++;
                    } else {
                        break;
                    }
                }

                String lexema = texto.substring(inicio, i);
                if (PALAVRAS_CHAVE.contains(lexema)) {
                    escreverToken(writer, lexema, lexema);
                } else {
                    escreverToken(writer, lexema, "IDENT");
                }
                continue;
            }

            if (Character.isDigit(c)) {
                int inicio = i;
                while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                    i++;
                }

                boolean real = false;
                // So reconhece real quando houver digitos antes e depois do ponto.
                if (i < texto.length() && texto.charAt(i) == '.') {
                    boolean ehIntervalo = (i + 1 < texto.length() && texto.charAt(i + 1) == '.');
                    if (!ehIntervalo && i + 1 < texto.length() && Character.isDigit(texto.charAt(i + 1))) {
                        real = true;
                        i++;
                        while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                            i++;
                        }
                    }
                }

                String lexema = texto.substring(inicio, i);
                escreverToken(writer, lexema, real ? "NUM_REAL" : "NUM_INT");
                continue;
            }

            if (c == '<') {
                if (i + 1 < texto.length()) {
                    char prox = texto.charAt(i + 1);
                    if (prox == '=') {
                        escreverToken(writer, "<=", "<=");
                        i += 2;
                        continue;
                    }
                    if (prox == '>') {
                        escreverToken(writer, "<>", "<>");
                        i += 2;
                        continue;
                    }
                    if (prox == '-') {
                        escreverToken(writer, "<-", "<-");
                        i += 2;
                        continue;
                    }
                }
                escreverToken(writer, "<", "<");
                i++;
                continue;
            }

            if (c == '>') {
                if (i + 1 < texto.length() && texto.charAt(i + 1) == '=') {
                    escreverToken(writer, ">=", ">=");
                    i += 2;
                } else {
                    escreverToken(writer, ">", ">");
                    i++;
                }
                continue;
            }

            if (c == '.') {
                if (i + 1 < texto.length() && texto.charAt(i + 1) == '.') {
                    escreverToken(writer, "..", "..");
                    i += 2;
                } else {
                    escreverToken(writer, ".", ".");
                    i++;
                }
                continue;
            }

            if (c == ':' || c == ',' || c == ';' || c == '(' || c == ')' || c == '[' || c == ']'
                || c == '^' || c == '&' || c == '+' || c == '-' || c == '*' || c == '/'
                || c == '=' || c == '%') {
                String lexema = Character.toString(c);
                escreverToken(writer, lexema, lexema);
                i++;
                continue;
            }

            escreverErroSimbolo(writer, linha, c);
            return;
        }
    }

    private static void escreverToken(BufferedWriter writer, String lexema, String tipo) throws IOException {
        boolean tipoCategoria = "IDENT".equals(tipo)
            || "NUM_INT".equals(tipo)
            || "NUM_REAL".equals(tipo)
            || "CADEIA".equals(tipo);

        if (tipoCategoria) {
            writer.write("<'" + lexema + "'," + tipo + ">\n");
        } else {
            writer.write("<'" + lexema + "','" + tipo + "'>\n");
        }
    }

    private static void escreverErroSimbolo(BufferedWriter writer, int linha, char simbolo) throws IOException {
        writer.write("Linha " + linha + ": " + simbolo + " - simbolo nao identificado\n");
    }

    private static void escreverErroComentario(BufferedWriter writer, int linha) throws IOException {
        writer.write("Linha " + linha + ": comentario nao fechado\n");
    }

    private static void escreverErroCadeia(BufferedWriter writer, int linha) throws IOException {
        writer.write("Linha " + linha + ": cadeia literal nao fechada\n");
    }
}