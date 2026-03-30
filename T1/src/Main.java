import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Analisador Lexico para a Linguagem LA (Linguagem Algorítmica).
 * 
 * Funcionalidade:
 * - Le um arquivo fonte LA
 * - Reconhece tokens (palavras-chave, identificadores, numeros, cadeias, operadores, pontuacao)
 * - Ignora espacos em branco, tabs e comentarios entre chaves {}
 * - Interrompe ao primeiro erro lexico encontrado
 * 
 * Entrada: arquivo de texto com programa LA
 * Saida: arquivo com lista de tokens no formato <'lexema',tipo> ou mensagem de erro
 * 
 * Argumentos de linha de comando:
 *   args[0] = caminho completo do arquivo de entrada
 *   args[1] = caminho completo do arquivo de saida
 */
public class Main {
    /**
     * Conjunto de palavras-chave da linguagem LA.
     * Usado para distinguir entre identificadores e palavras reservadas.
     */
    private static final Set<String> PALAVRAS_CHAVE = new HashSet<>(Arrays.asList(
        "algoritmo", "fim_algoritmo", "declare", "literal", "inteiro", "real", "logico",
        "leia", "escreva", "se", "entao", "senao", "fim_se", "caso", "seja", "fim_caso",
        "para", "ate", "faca", "fim_para", "enquanto", "fim_enquanto", "registro",
        "fim_registro", "tipo", "procedimento", "fim_procedimento", "funcao", "fim_funcao",
        "var", "constante", "retorne", "nao", "e", "ou", "verdadeiro", "falso"
    ));

    /**
     * Metodo principal do analisador lexico.
     * 
     * Funcao: ler arquivo de entrada, remover BOM UTF-8 (se presente),
     * chamar o analisador lexico e salvar resultado em arquivo de saida.
     * 
     * @param args[0] Caminho completo do arquivo de entrada (programa LA)
     * @param args[1] Caminho completo do arquivo de saida (lista de tokens ou erro)
     * @throws Exception Se houver erro ao ler/escrever arquivos
     */
    public static void main(String[] args) throws Exception {
        // Validar presenca dos dois argumentos obrigatorios
        if (args.length < 2) {
            return;
        }

        // Carregar paths dos arquivos de entrada e saida
        Path entrada = Paths.get(args[0]);
        Path saida = Paths.get(args[1]);

        // Ler arquivo de entrada com encoding UTF-8
        String conteudo = Files.readString(entrada, StandardCharsets.UTF_8);
        
        // Remove BOM UTF-8 (byte order mark) que pode aparecer no inicio de arquivos
        // criados no Windows, evitando erro falso de simbolo nao identificado na linha 1
        if (!conteudo.isEmpty() && conteudo.charAt(0) == '\uFEFF') {
            conteudo = conteudo.substring(1);
        }

        // Executar analise lexica e salvar resultado no arquivo de saida
        try (BufferedWriter writer = Files.newBufferedWriter(saida, StandardCharsets.UTF_8)) {
            analisar(conteudo, writer);
        }
    }

    /**
     * Metodo principal de analise lexica.
     * 
     * Percorre o texto caractere por caractere, reconhecendo tokens e ignorando espacos/comentarios.
     * Ao encontrar erro lexico, interrompe e escreve mensagem de erro formatada.
     * 
     * Regras lexicas implementadas:
     * - Espacos, tabs e quebras de linha sao ignorados (exceto para contar linhas)
     * - Comentarios entre { } sao ignorados se fechados na mesma linha
     * - Cadeias entre " " sao reconhecidas se fechadas na mesma linha
     * - Identificadores: letra ou _ seguida de letras, digitos e _
     * - Numeros: sequencias de digitos, com ponto opcional para reais
     * - Operadores e pontuacao: <=, <>, <-, >=, ., .. e simbolos simples
     * - Palavras-chave: verificadas em conjunto special
     * 
     * @param texto Conteudo do arquivo de entrada ja sem BOM
     * @param writer Escritor para gerar arquivo de saida
     * @throws IOException Se houver erro ao escrever saida
     */
    private static void analisar(String texto, BufferedWriter writer) throws IOException {
        // Indice da posicao atual no texto (percurso sequencial)
        int i = 0;
        // Contador de linhas (necessario para relatar erro na linha correta)
        int linha = 1;

        // Percorre todo o conteudo do texto
        while (i < texto.length()) {
            char c = texto.charAt(i);

            // Ignorar espacos em branco (espaco, tab, retorno de carro)
            if (c == ' ' || c == '\t' || c == '\r') {
                i++;
                continue;
            }

            // Quebra de linha: incrementar contador e continuar
            if (c == '\n') {
                linha++;
                i++;
                continue;
            }

            // Processamento de comentarios
            // Em LA, comentarios iniciam com { e devem fechar com } na mesma linha
            if (c == '{') {
                int linhaComentario = linha;
                i++;
                boolean fechado = false;

                // Buscar fechamento do comentario
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (cc == '}') {
                        fechado = true;
                        i++;
                        break;
                    }

                    // Erro: comentario nao pode atravessar quebra de linha
                    if (cc == '\n') {
                        escreverErroComentario(writer, linhaComentario);
                        return;
                    }

                    i++;
                }

                // Erro: comentario nao foi fechado (EOF encontrado)
                if (!fechado) {
                    escreverErroComentario(writer, linhaComentario);
                    return;
                }

                continue;
            }

            // Processamento de cadeias literais (strings)
            // Iniciam com " e devem fechar com " na mesma linha
            if (c == '"') {
                int linhaCadeia = linha;
                StringBuilder cadeia = new StringBuilder();
                cadeia.append(c);
                i++;

                boolean fechada = false;
                // Buscar fechamento da cadeia (atentar para quebra de linha)
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (cc == '"') {
                        cadeia.append(cc);
                        i++;
                        fechada = true;
                        break;
                    }

                    // Erro: cadeia nao pode atravessar quebra de linha
                    if (cc == '\n') {
                        escreverErroCadeia(writer, linhaCadeia);
                        return;
                    }

                    cadeia.append(cc);
                    i++;
                }

                // Erro: cadeia nao foi fechada (EOF encontrado)
                if (!fechada) {
                    escreverErroCadeia(writer, linhaCadeia);
                    return;
                }

                // Gerar token de categoria CADEIA
                escreverToken(writer, cadeia.toString(), "CADEIA");
                continue;
            }

            // Processamento de identificadores e palavras-chave
            // Começam com letra ou _ seguidos por letras, digitos ou _
            if (Character.isLetter(c) || c == '_') {
                int inicio = i;
                i++;
                
                // Consumir resto do identificador
                while (i < texto.length()) {
                    char cc = texto.charAt(i);
                    if (Character.isLetterOrDigit(cc) || cc == '_') {
                        i++;
                    } else {
                        break;
                    }
                }

                // Extrair lexema e verif. se e palavra-chave
                String lexema = texto.substring(inicio, i);
                if (PALAVRAS_CHAVE.contains(lexema)) {
                    // Token de palavra-chave: <'lexema','lexema'>
                    escreverToken(writer, lexema, lexema);
                } else {
                    // Token de identificador: <'lexema',IDENT>
                    escreverToken(writer, lexema, "IDENT");
                }
                continue;
            }

            // Processamento de numeros (inteiros e reais)
            // Inteiros: sequencia de digitos
            // Reais: digitos + ponto + digitos (ex: 3.14)
            if (Character.isDigit(c)) {
                int inicio = i;
                // Consumir parte inteira
                while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                    i++;
                }

                boolean real = false;
                // Verificar se ha parte decimal valida
                // Cuidado: diferenciacao entre "." (ponto simples) e ".." (intervalo)
                if (i < texto.length() && texto.charAt(i) == '.') {
                    boolean ehIntervalo = (i + 1 < texto.length() && texto.charAt(i + 1) == '.');
                    // Reconhecer como real so se houver digito depois do ponto
                    if (!ehIntervalo && i + 1 < texto.length() && Character.isDigit(texto.charAt(i + 1))) {
                        real = true;
                        i++;
                        // Consumir parte decimal
                        while (i < texto.length() && Character.isDigit(texto.charAt(i))) {
                            i++;
                        }
                    }
                }

                String lexema = texto.substring(inicio, i);
                escreverToken(writer, lexema, real ? "NUM_REAL" : "NUM_INT");
                continue;
            }

            // Processamento de operadores com multiplos caracteres
            // Operadores compostos: <=, <>, <-, >=, ..
            if (c == '<') {
                // Verificar se e operador composto
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
                // Caso contrario, e operador simples <
                escreverToken(writer, "<", "<");
                i++;
                continue;
            }

            // Operador >=
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

            // Ponto: simples "." ou intervalo ".."
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

            // Processamento de simbolos simples (operadores e pontuacao)
            // Dois-pontos, virgula, ponto-virgula, parenteses, colchetes,
            // exponenciacao, ampersand, operadores aritmeticos, atribuicao, modulo
            if (c == ':' || c == ',' || c == ';' || c == '(' || c == ')' || c == '[' || c == ']'
                || c == '^' || c == '&' || c == '+' || c == '-' || c == '*' || c == '/'
                || c == '=' || c == '%') {
                String lexema = Character.toString(c);
                escreverToken(writer, lexema, lexema);
                i++;
                continue;
            }

            // Erro: simbolo nao identificado
            // Caractre nao reconhecido por nenhuma regra lexica
            escreverErroSimbolo(writer, linha, c);
            return;
        }
    }

    /**
     * Escreve um token reconhecido no arquivo de saida.
     * 
     * Formato de saida:
     * - Para categorias (IDENT, NUM_INT, NUM_REAL, CADEIA): <'lexema',TIPO>
     * - Para palavras-chave e operadores: <'lexema','lexema'>
     * 
     * @param writer Escritor do arquivo de saida
     * @param lexema String exata do token (como lida do arquivo)
     * @param tipo Tipo/categoria do token
     * @throws IOException Se houver erro ao escrever
     */
    private static void escreverToken(BufferedWriter writer, String lexema, String tipo) throws IOException {
        // Identificar se tipo e uma categoria (sem aspas no tipo)
        // ou um tipo especifico como palavra-chave (com aspas)
        boolean tipoCategoria = "IDENT".equals(tipo)
            || "NUM_INT".equals(tipo)
            || "NUM_REAL".equals(tipo)
            || "CADEIA".equals(tipo);

        if (tipoCategoria) {
            // Formato para categorias: <'lexema',TIPO>
            writer.write("<'" + lexema + "'," + tipo + ">\n");
        } else {
            // Formato para palavras-chave e operadores: <'lexema','tipo'>
            writer.write("<'" + lexema + "','" + tipo + "'>\n");
        }
    }

    /**
     * Escreve erro: simbolo nao identificado (caractere invalido).
     * Formato: "Linha N: X - simbolo nao identificado"
     * 
     * @param writer Escritor do arquivo de saida
     * @param linha Numero da linha onde o erro foi encontrado
     * @param simbolo Caractere invalido nao reconhecido
     * @throws IOException Se houver erro ao escrever
     */
    private static void escreverErroSimbolo(BufferedWriter writer, int linha, char simbolo) throws IOException {
        writer.write("Linha " + linha + ": " + simbolo + " - simbolo nao identificado\n");
    }

    /**
     * Escreve erro: comentario nao fechado (atravessa quebra de linha).
     * Formato: "Linha N: comentario nao fechado"
     * 
     * @param writer Escritor do arquivo de saida
     * @param linha Numero da linha onde o comentario foi aberto
     * @throws IOException Se houver erro ao escrever
     */
    private static void escreverErroComentario(BufferedWriter writer, int linha) throws IOException {
        writer.write("Linha " + linha + ": comentario nao fechado\n");
    }

    /**
     * Escreve erro: cadeia literal nao fechada (atravessa quebra de linha).
     * Formato: "Linha N: cadeia literal nao fechada"
     * 
     * @param writer Escritor do arquivo de saida
     * @param linha Numero da linha onde a cadeia foi aberta
     * @throws IOException Se houver erro ao escrever
     */
    private static void escreverErroCadeia(BufferedWriter writer, int linha) throws IOException {
        writer.write("Linha " + linha + ": cadeia literal nao fechada\n");
    }
}