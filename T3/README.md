# T3 - Compilador

Este projeto contém o compilador do trabalho T3.

## Como compilar com Maven

Na pasta `T3`, execute:

```powershell
mvn package
```

Isso gera o jar executável em `target/meuCompilador.jar`.

## Como rodar localmente

O compilador recebe dois argumentos:

1. arquivo de entrada
2. arquivo de saída

Exemplo no PowerShell:

```powershell
& 'C:\Users\renat\OneDrive\Documentos\Compiladores_LA\T3\src\meuCompilador.jar.bat' 'C:\temp\entrada.txt' 'C:\temp\saida.txt'
```

## Como rodar os casos de teste

Para testar com o corretor automático, compile com Maven e use o wrapper `.bat` do compilador:

```powershell
java -jar 'C:\Users\renat\OneDrive\Documentos\VS\Compiladores\compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar' 'C:\Users\renat\OneDrive\Documentos\Compiladores_LA\T3\src\meuCompilador.jar.bat' gcc c:\temp 'C:\Users\renat\OneDrive\Documentos\Compiladores_LA\casos-de-teste' '176168, 155551, 187123' t3
```

## Observação

No Windows, o corretor deve chamar o arquivo `meuCompilador.jar.bat` em vez de executar o `.jar` diretamente.
