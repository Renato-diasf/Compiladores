# Trabalho 2 - Analisador Sintatico da Linguagem LA

Projeto da disciplina de Compiladores com implementacao de analisador sintatico manual (descida recursiva) para LA.

## Escopo implementado

- Le arquivo fonte LA (texto bruto)
- Realiza analise lexica interna para gerar fluxo de tokens
- Realiza analise sintatica com foco na gramatica principal da linguagem
- Interrompe no primeiro erro lexico ou sintatico

## Contrato de execucao

O programa recebe dois argumentos obrigatorios:

1. Caminho do arquivo de entrada LA
2. Caminho do arquivo de saida

### Saida

- Sem erros: arquivo de saida vazio
- Erro sintatico: `Linha N: erro sintatico proximo a X`
- Erro lexico: mesma convencao do T1

## Estrutura

```text
T2/
  entrada.txt
  README.md
  src/
    Main.java
  out/
```

## Como compilar (PowerShell)

```powershell
Set-Location .\T2
New-Item -ItemType Directory -Force -Path .\out | Out-Null
javac -d .\out .\src\Main.java
```

## Como executar (PowerShell)

```powershell
java -cp .\out Main .\entrada.txt .\saida.txt
```

## Regras sintaticas cobertas nesta versao

- Programa com declaracoes globais opcionais antes de `algoritmo`
- Corpo do algoritmo com declaracoes locais e comandos
- Declaracoes locais: `declare`, `constante`, `tipo`
- Declaracoes globais: `procedimento`, `funcao`
- Comandos: `leia`, `escreva`, atribuicao/chamada, `se`, `caso`, `para`, `enquanto`, `faca ... ate`, `retorne`
- Expressoes logicas, relacionais e aritmeticas com precedencia

## Observacoes

- Implementacao feita somente no diretorio T2.
- Se `javac` nao for reconhecido no terminal, configure o JDK no PATH do sistema.
