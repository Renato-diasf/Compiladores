# T4

Murilo Eduardo Feijo Ramos - 824389

Renato Dias Ferreira Campos - 821328

Leonardo Shoji Ishiy - 823830

Implementation for the T4 semantic analyzer.

## Build

Build from the `T4` directory with Maven. The ANTLR lexer is generated automatically:

```bash
mvn package
```

## Run

The analyzer expects two command-line arguments:

1. input file path
2. output file path

Example:

```bash
java -jar ../compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar \
  ./meuCompilador \
  /usr/bin/gcc \
  /tmp/compiladores-corretor \
  ../casos-de-teste \
  "000000" \
  "t4"
```

## Validation

Use the official test cases under `casos-de-teste/4.casos_teste_t4/`.
The compiler writes semantic diagnostics to the output file and must end with `Fim da compilacao` when errors are present.
