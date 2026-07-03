# UaiLang - T6

UaiLang e uma linguagem pequena, com palavras inspiradas no sotaque e no vocabulario de Minas Gerais, que compila para Python 3.

## Ideia da linguagem

Um programa comeca com `uai NomeDoPrograma` e termina com `cabou`. Variaveis sao `trem`, a escrita usa `mostra`, a leitura usa `escuita`, e repeticoes podem usar `inté` ou `pra`.

Exemplo:

```uailang
uai CalculadoraDeQuitanda

trem pao_de_queijo: inteiro = 6.
trem cafe: real = 2.5.
trem total: real = pao_de_queijo * cafe.
trem fregues: texto = "sô".
trem barato: logico = total < 20.0.

mostra "Uai,", fregues, "deu", total.

se barato entao
    mostra "Conta boa demais da conta".
senao
    mostra "Ficou salgado".
fimse.

cabou
```

## Recursos implementados

- Analise lexica e sintatica com ANTLR4, descrita em `src/main/antlr4/UaiLang.g4`.
- Tipos `inteiro`, `real`, `texto` e `logico`.
- Declaracao, atribuicao, leitura, escrita, `se/senao`, `inté` e `pra`.
- Expressoes aritmeticas, relacionais e logicas.
- Geracao de codigo Python 3.

Exemplo de repeticao com `inté`:

```uailang
inté contador <= 3 faz
    mostra contador.
    contador = contador + 1.
prontim.
```

## Analise semantica

O compilador faz verificacoes que nao sao resolvidas apenas pela gramatica:

- variavel duplicada;
- uso, leitura ou atribuicao de variavel nao declarada;
- incompatibilidade de tipos em declaracao e atribuicao;
- condicoes de `se` e `inté` precisam ser logicas;
- contador e limites do `pra` precisam ser inteiros;
- operadores aritmeticos exigem numeros;
- divisao ou resto por zero literal.

## Como compilar

Na pasta `T6`, execute:

```bash
mvn package
```

O jar executavel sera gerado em `target/uailang.jar`.

## Wrappers

O projeto tem dois wrappers para evitar digitar o comando completo com `java -cp` e o caminho do ANTLR:

- `./uailang`: compila um arquivo `.uai` para um arquivo Python. Ele verifica se `target/uailang.jar` existe; se nao existir, roda `mvn -q package` automaticamente. Depois chama a classe Java principal com o jar do projeto e o runtime do ANTLR no classpath.
- `./uairun`: compila e executa em seguida. Ele recebe um arquivo `.uai`, cria um arquivo Python temporario em `/tmp`, chama `./uailang` para gerar esse Python, e entao roda `python3` nesse arquivo.

## Como usar

Para gerar um arquivo Python, use `./uailang` com dois argumentos: arquivo de entrada UaiLang e arquivo de saida Python.

```bash
./uailang exemplos/calculadora.uai /tmp/calculadora.py
python3 /tmp/calculadora.py
```

Para compilar e executar em um comando so, use `./uairun`:

```bash
./uairun exemplos/calculadora.uai
```

## Casos de teste

Entradas e saidas esperadas ficam em:

- `casos-de-teste/entrada`
- `casos-de-teste/saida`

Exemplo:

```bash
./uailang casos-de-teste/entrada/1.programa-valido.uai /tmp/saida.py
diff -u casos-de-teste/saida/1.programa-valido.py /tmp/saida.py
```
