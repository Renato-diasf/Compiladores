package br.com.compiladores.uailang;

enum Type {
    INTEIRO,
    REAL,
    TEXTO,
    LOGICO,
    ERRO;

    boolean isNumeric() {
        return this == INTEIRO || this == REAL;
    }

    boolean accepts(Type other) {
        if (this == ERRO || other == ERRO) {
            return true;
        }
        if (this == REAL && other == INTEIRO) {
            return true;
        }
        return this == other;
    }

    String pythonReader() {
        return switch (this) {
            case INTEIRO -> "int(input())";
            case REAL -> "float(input())";
            case TEXTO -> "input()";
            case LOGICO -> "input().strip().lower() in ('verdade', 'true', '1', 'sim')";
            case ERRO -> "input()";
        };
    }
}
