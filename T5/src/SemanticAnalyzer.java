import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class SemanticAnalyzer {
    enum SymbolKind {
        VARIABLE,
        CONSTANT,
        PARAMETER,
        PROCEDURE,
        FUNCTION,
        TYPE
    }

    static final class TypeInfo {
        enum Kind {
            BASIC,
            POINTER,
            ARRAY,
            RECORD,
            ERROR
        }

        final Kind kind;
        final String name;
        final TypeInfo baseType;
        final TypeInfo elementType;
        final Map<String, TypeInfo> fields;

        private TypeInfo(Kind kind, String name, TypeInfo baseType, TypeInfo elementType,
                Map<String, TypeInfo> fields) {
            this.kind = kind;
            this.name = name;
            this.baseType = baseType;
            this.elementType = elementType;
            this.fields = fields;
        }

        static TypeInfo basic(String name) {
            return new TypeInfo(Kind.BASIC, name, null, null, null);
        }

        static TypeInfo pointer(TypeInfo baseType) {
            return new TypeInfo(Kind.POINTER, "^" + baseType.name, baseType, null, null);
        }

        static TypeInfo array(TypeInfo elementType) {
            return new TypeInfo(Kind.ARRAY, elementType.name + "[]", null, elementType, null);
        }

        static TypeInfo record(String name) {
            return new TypeInfo(Kind.RECORD, name, null, null, new LinkedHashMap<>());
        }

        static TypeInfo error() {
            return new TypeInfo(Kind.ERROR, "<erro>", null, null, null);
        }

        boolean isError() {
            return kind == Kind.ERROR;
        }

        boolean isBasic(String expected) {
            return kind == Kind.BASIC && name.equals(expected);
        }

        boolean isNumeric() {
            return isBasic("inteiro") || isBasic("real");
        }

        boolean isLogical() {
            return isBasic("logico");
        }

        boolean isLiteral() {
            return isBasic("literal");
        }

        boolean isPointer() {
            return kind == Kind.POINTER;
        }

        boolean isArray() {
            return kind == Kind.ARRAY;
        }

        boolean isRecord() {
            return kind == Kind.RECORD;
        }
    }

    static final class ParamInfo {
        final String name;
        final TypeInfo type;
        final boolean byRef;
        final int line;

        ParamInfo(String name, TypeInfo type, boolean byRef, int line) {
            this.name = name;
            this.type = type;
            this.byRef = byRef;
            this.line = line;
        }
    }

    static final class Symbol {
        final String name;
        final SymbolKind kind;
        final TypeInfo type;
        final List<ParamInfo> parameters;
        final TypeInfo returnType;
        final int line;

        Symbol(String name, SymbolKind kind, TypeInfo type, List<ParamInfo> parameters,
                TypeInfo returnType, int line) {
            this.name = name;
            this.kind = kind;
            this.type = type;
            this.parameters = parameters == null ? List.of() : List.copyOf(parameters);
            this.returnType = returnType;
            this.line = line;
        }

        boolean isCallable() {
            return kind == SymbolKind.PROCEDURE || kind == SymbolKind.FUNCTION;
        }
    }

    static final class Scope {
        final Scope parent;
        final Map<String, Symbol> symbols = new LinkedHashMap<>();

        Scope(Scope parent) {
            this.parent = parent;
        }
    }

    private final Scope globalScope = new Scope(null);
    private Scope currentScope = globalScope;
    private final List<String> errors = new ArrayList<>();
    private final TypeInfo integerType = TypeInfo.basic("inteiro");
    private final TypeInfo realType = TypeInfo.basic("real");
    private final TypeInfo literalType = TypeInfo.basic("literal");
    private final TypeInfo logicalType = TypeInfo.basic("logico");
    private final TypeInfo errorType = TypeInfo.error();

    void enterScope() {
        currentScope = new Scope(currentScope);
    }

    void exitScope() {
        if (currentScope.parent != null) {
            currentScope = currentScope.parent;
        }
    }

    Symbol declare(String name, SymbolKind kind, TypeInfo type, List<ParamInfo> parameters,
            TypeInfo returnType, int line) {
        if (currentScope.symbols.containsKey(name)) {
            report(line, "identificador " + name + " ja declarado anteriormente");
            return currentScope.symbols.get(name);
        }

        Symbol symbol = new Symbol(name, kind, type, parameters, returnType, line);
        currentScope.symbols.put(name, symbol);
        return symbol;
    }

    Symbol declareVariable(String name, TypeInfo type, int line) {
        return declare(name, SymbolKind.VARIABLE, type, null, null, line);
    }

    Symbol declareConstant(String name, TypeInfo type, int line) {
        return declare(name, SymbolKind.CONSTANT, type, null, null, line);
    }

    Symbol declareParameter(String name, TypeInfo type, boolean byRef, int line) {
        return declare(name, SymbolKind.PARAMETER, type, null, null, line);
    }

    Symbol declareType(String name, TypeInfo type, int line) {
        return declare(name, SymbolKind.TYPE, type, null, null, line);
    }

    Symbol declareRoutine(String name, SymbolKind kind, List<ParamInfo> parameters,
            TypeInfo returnType, int line) {
        return declare(name, kind, null, parameters, returnType, line);
    }

    Symbol lookup(String name) {
        for (Scope scope = currentScope; scope != null; scope = scope.parent) {
            Symbol symbol = scope.symbols.get(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }

    Symbol lookupGlobal(String name) {
        return globalScope.symbols.get(name);
    }

    TypeInfo resolveTypeByName(Token token) {
        return resolveTypeByName(token.lexeme, token.line, token.type);
    }

    TypeInfo resolveTypeByName(String name, int line, TokenType tokenType) {
        if (tokenType == TokenType.LITERAL) {
            return literalType;
        }
        if (tokenType == TokenType.INTEIRO) {
            return integerType;
        }
        if (tokenType == TokenType.REAL) {
            return realType;
        }
        if (tokenType == TokenType.LOGICO) {
            return logicalType;
        }

        if (tokenType == TokenType.IDENT) {
            Symbol symbol = lookup(name);
            if (symbol != null && symbol.kind == SymbolKind.TYPE) {
                return symbol.type;
            }
            report(line, "tipo " + name + " nao declarado");
            return errorType;
        }

        report(line, "tipo " + name + " nao declarado");
        return errorType;
    }

    TypeInfo basicInteger() {
        return integerType;
    }

    TypeInfo basicReal() {
        return realType;
    }

    TypeInfo basicLiteral() {
        return literalType;
    }

    TypeInfo basicLogical() {
        return logicalType;
    }

    TypeInfo errorType() {
        return errorType;
    }

    TypeInfo pointerTo(TypeInfo baseType) {
        if (baseType == null || baseType.isError()) {
            return errorType;
        }
        return TypeInfo.pointer(baseType);
    }

    TypeInfo arrayOf(TypeInfo elementType) {
        if (elementType == null || elementType.isError()) {
            return errorType;
        }
        return TypeInfo.array(elementType);
    }

    boolean isValueSymbol(Symbol symbol) {
        return symbol != null && symbol.kind != SymbolKind.TYPE;
    }

    boolean isCallable(Symbol symbol) {
        return symbol != null && symbol.isCallable();
    }

    boolean isTypeSymbol(Symbol symbol) {
        return symbol != null && symbol.kind == SymbolKind.TYPE;
    }

    void reportUndeclared(Token token, String text) {
        report(token.line, "identificador " + text + " nao declarado");
    }

    void reportAssignmentIncompatible(Token token, String targetText) {
        report(token.line, "atribuicao nao compativel para " + targetText);
    }

    void reportParamIncompatible(Token token, String routineName) {
        report(token.line, "incompatibilidade de parametros na chamada de " + routineName);
    }

    void reportRetorneNotAllowed(Token token) {
        report(token.line, "comando retorne nao permitido nesse escopo");
    }

    void reportTypeNotDeclared(Token token, String name) {
        report(token.line, "tipo " + name + " nao declarado");
    }

    void reportDuplicateField(Token token, String name) {
        report(token.line, "identificador " + name + " ja declarado anteriormente");
    }

    boolean hasErrors() {
        return !errors.isEmpty();
    }

    String output() {
        StringBuilder builder = new StringBuilder();
        for (String error : errors) {
            builder.append(error).append('\n');
        }
        if (!errors.isEmpty()) {
            builder.append("Fim da compilacao\n");
        }
        return builder.toString();
    }

    boolean sameType(TypeInfo left, TypeInfo right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null || left.isError() || right.isError()) {
            return false;
        }
        if (left.kind != right.kind) {
            return false;
        }
        return switch (left.kind) {
            case BASIC -> left.name.equals(right.name);
            case POINTER -> sameType(left.baseType, right.baseType);
            case ARRAY -> sameType(left.elementType, right.elementType);
            case RECORD -> left.name.equals(right.name);
            case ERROR -> false;
        };
    }

    boolean canAssign(TypeInfo target, TypeInfo source) {
        if (sameType(target, source)) {
            return true;
        }
        if (target != null && source != null && target.isNumeric() && source.isNumeric()) {
            return true;
        }
        return false;
    }

    boolean canPassArgument(TypeInfo expected, TypeInfo actual) {
        return sameType(expected, actual);
    }

    TypeInfo dereference(TypeInfo type) {
        if (type != null && type.kind == TypeInfo.Kind.POINTER) {
            return type.baseType;
        }
        return errorType;
    }

    TypeInfo arrayElement(TypeInfo type) {
        if (type != null && type.kind == TypeInfo.Kind.ARRAY) {
            return type.elementType;
        }
        return errorType;
    }

    TypeInfo fieldType(TypeInfo type, String field) {
        if (type != null && type.kind == TypeInfo.Kind.RECORD) {
            return type.fields.get(field);
        }
        return null;
    }

    private void report(int line, String message) {
        errors.add("Linha " + line + ": " + message);
    }
}
