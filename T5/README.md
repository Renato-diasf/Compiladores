# T5 - Compilador

Murilo Eduardo Feijo Ramos - 824389

Renato Dias Ferreira Campos - 821328

Leonardo Shoji Ishiy - 823830

Este projeto contem o compilador do trabalho T5.

## Como compilar com Maven

Na pasta `T5`, execute:

```powershell
mvn package
```

Isso gera o jar executavel em `target/meuCompilador.jar`.

## Como rodar localmente

O compilador recebe dois argumentos:

1. arquivo de entrada
2. arquivo de saida

Exemplo no PowerShell, a partir da raiz do repositorio:

```powershell
java -jar T5\target\meuCompilador.jar 'casos-de-teste\5.casos_teste_t5\1.entrada\1.declaracao_leitura_impressao_inteiro.alg' 'c:\temp\saida.c'
```

Tambem e possivel usar o wrapper:

```powershell
& '.\T5\src\meuCompilador.jar.bat' 'casos-de-teste\5.casos_teste_t5\1.entrada\1.declaracao_leitura_impressao_inteiro.alg' 'c:\temp\saida.c'
```

## Como rodar os casos de teste

Para testar com o corretor automatico, compile com Maven e use o jar gerado:

```powershell
java -jar compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar "java -jar T5\target\meuCompilador.jar" "C:\MinGW\bin\gcc.exe" "c:\temp" "casos-de-teste" "176168, 155551, 187123" "t5"
```


## Observacao

No Windows, o corretor pode chamar o comando `java -jar T5\target\meuCompilador.jar` diretamente. O arquivo `T5\src\meuCompilador.jar.bat` tambem foi mantido como wrapper auxiliar, mas o jar precisa ter sido gerado antes com `mvn package`.
