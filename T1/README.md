# Trabalho 1 - Analisador Lexico da Linguagem LA

Projeto da disciplina **Construcao de Compiladores** (DC/UFSCar), com implementacao de um analisador lexico para a linguagem LA.

## Integrantes do grupo

- Renato Dias Ferreira Campos - 821328
- Murilo Eduardo Feijo Ramos - 
- Leonardo Shoji Ishjy - 823830

## Requisitos

- Java JDK 17+ (testado com JDK 25)
- Sistema operacional: Windows, Linux ou macOS

Para verificar as versoes instaladas:

```powershell
java -version
javac -version
```

## Estrutura do projeto

```text
T1/
  entrada.txt
  README.md
  out/
  src/
    Main.java
```

## Como compilar

No diretorio `T1`, execute:

```powershell
New-Item -ItemType Directory -Force -Path .\out | Out-Null
javac -d .\out .\src\Main.java
```

## Como executar

O programa deve receber **obrigatoriamente dois argumentos**:

1. Caminho do arquivo de entrada
2. Caminho do arquivo de saida

Exemplo (PowerShell):

```powershell
java -cp .\out Main C:\caminho\completo\entrada.txt C:\caminho\completo\saida.txt
```

Exemplo com arquivos locais no proprio diretorio `T1`:

```powershell
java -cp .\out Main .\entrada.txt .\saida.txt
```

## Formato da saida

Para cada token valido, o analisador grava no arquivo de saida uma linha no formato:

```text
<'lexema','token'>
```

Casos especiais:

- Identificadores: `<'nome',IDENT>`
- Cadeias: `<'"texto"',CADEIA>`
- Numeros inteiros: `<'10',NUM_INT>`
- Numeros reais: `<'10.5',NUM_REAL>`

Espacos em branco e comentarios sao ignorados.

## Erros lexicos tratados

Ao encontrar o primeiro erro lexico, a execucao para e o arquivo de saida recebe apenas a mensagem de erro (apos os tokens ja reconhecidos):

- Simbolo nao identificado:
  - `Linha N: X - simbolo nao identificado`
- Comentario nao fechado na mesma linha:
  - `Linha N: comentario nao fechado`
- Cadeia literal nao fechada na mesma linha:
  - `Linha N: cadeia literal nao fechada`

## Observacoes

- A saida e sempre salva em arquivo (nao imprime tokens no terminal).
- O codigo remove BOM UTF-8 no inicio do arquivo de entrada para evitar erro falso no primeiro caractere.
