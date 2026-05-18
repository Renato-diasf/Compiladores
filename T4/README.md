# T4

Implementation for the T4 semantic analyzer.

## Build

Compile the sources from the `T4` directory:

```bash
javac src/*.java -d out
```

## Run

The analyzer expects two command-line arguments:

1. input file path
2. output file path

Example:

```bash
java -cp out Main casos-de-teste/4.casos_teste_t4/entrada/1.algoritmo_7-2_apostila_LA.txt /tmp/out.txt
```

## Validation

Use the official test cases under `casos-de-teste/4.casos_teste_t4/`.
The compiler writes semantic diagnostics to the output file and must end with `Fim da compilacao` when errors are present.
