import java.util.ArrayList;
import java.util.List;

final class CodeGenerator {
    private static final class Context {
        final List<String> declarations = new ArrayList<>();
        final List<String> statements = new ArrayList<>();
        int indent;

        Context(int indent) {
            this.indent = indent;
        }
    }

    private final List<String> typeDeclarations = new ArrayList<>();
    private final List<String> routines = new ArrayList<>();
    private final Context main = new Context(1);
    private Context current = main;
    private String currentRoutineHeader;
    private int indent = 1;

    private String indentStr() {
        return "    ".repeat(Math.max(0, current.indent));
    }

    private String cType(SemanticAnalyzer.TypeInfo type) {
        if (type == null) {
            return "int";
        }
        if (type.isBasic("literal")) {
            return "char";
        }
        if (type.isBasic("inteiro") || type.isBasic("logico")) {
            return "int";
        }
        if (type.isBasic("real")) {
            return "double";
        }
        if (type.isPointer()) {
            return cType(type.baseType) + "*";
        }
        if (type.isRecord()) {
            return type.name;
        }
        if (type.isArray()) {
            return cType(type.elementType);
        }
        return "int";
    }

    private String declarationFor(String name, SemanticAnalyzer.TypeInfo type, List<String> dimensions) {
        if (type != null && type.isRecord() && type.name.startsWith("<anon>")) {
            StringBuilder b = new StringBuilder();
            b.append("struct {\n");
            for (var field : type.fields.entrySet()) {
                b.append("        ").append(declarationFor(field.getKey(), field.getValue(), List.of())).append('\n');
            }
            b.append("    } ").append(name).append(';');
            return b.toString();
        }

        if (type != null && type.isBasic("literal")) {
            return "char " + name + "[80];";
        }

        StringBuilder b = new StringBuilder();
        b.append(cType(type)).append(' ').append(name);
        for (String dimension : dimensions) {
            b.append('[').append(dimension).append(']');
        }
        b.append(';');
        return b.toString();
    }

    void declareVariable(String name, SemanticAnalyzer.TypeInfo type, List<String> dimensions) {
        current.declarations.add(declarationFor(name, type, dimensions));
    }

    void declareConstant(String name, SemanticAnalyzer.TypeInfo type, String value) {
        current.declarations.add("const " + cType(type) + " " + name + " = " + value + ";");
    }

    void declareRecordType(String name, SemanticAnalyzer.TypeInfo type) {
        StringBuilder b = new StringBuilder();
        b.append("typedef struct {\n");
        for (var field : type.fields.entrySet()) {
            b.append("    ").append(declarationFor(field.getKey(), field.getValue(), List.of())).append('\n');
        }
        b.append("} ").append(name).append(';');
        typeDeclarations.add(b.toString());
    }

    private String paramDeclaration(SemanticAnalyzer.ParamInfo param) {
        if (param.type != null && param.type.isBasic("literal")) {
            return "char* " + param.name;
        }
        return cType(param.type) + " " + param.name;
    }

    void startRoutine(String name, List<SemanticAnalyzer.ParamInfo> params, SemanticAnalyzer.TypeInfo returnType) {
        List<String> renderedParams = new ArrayList<>();
        for (SemanticAnalyzer.ParamInfo param : params) {
            renderedParams.add(paramDeclaration(param));
        }
        String returnText = returnType == null ? "void" : cType(returnType);
        currentRoutineHeader = returnText + " " + name + "(" + String.join(", ", renderedParams) + ")";
        current = new Context(1);
    }

    void endRoutine() {
        StringBuilder b = new StringBuilder();
        b.append(currentRoutineHeader).append(" {\n");
        for (String d : current.declarations) {
            b.append("    ").append(d).append('\n');
        }
        for (String s : current.statements) {
            b.append(s).append('\n');
        }
        b.append("}\n");
        routines.add(b.toString());
        current = main;
        currentRoutineHeader = null;
    }

    void emitRead(String designatorText, SemanticAnalyzer.TypeInfo type) {
        if (type != null && type.isBasic("literal")) {
            current.statements.add(indentStr() + "gets(" + designatorText + ");");
        } else if (type != null && type.isBasic("inteiro")) {
            current.statements.add(indentStr() + "scanf(\"%d\", &" + designatorText + ");");
        } else if (type != null && type.isBasic("real")) {
            current.statements.add(indentStr() + "scanf(\"%lf\", &" + designatorText + ");");
        } else if (type != null && type.isBasic("logico")) {
            current.statements.add(indentStr() + "scanf(\"%d\", &" + designatorText + ");");
        } else {
            current.statements.add(indentStr() + "/* read */ ;");
        }
    }

    void emitWrite(String exprText, SemanticAnalyzer.TypeInfo type) {
        String fmt;
        if (type != null && type.isBasic("literal")) {
            fmt = "printf(\"%s\"," + exprText + ");";
        } else if (type != null && type.isBasic("inteiro")) {
            fmt = "printf(\"%d\"," + exprText + ");";
        } else if (type != null && type.isBasic("real")) {
            fmt = "printf(\"%f\"," + exprText + ");";
        } else if (type != null && type.isBasic("logico")) {
            fmt = "printf(\"%d\"," + exprText + ");";
        } else {
            fmt = "printf(\"%s\"," + exprText + ");";
        }
        current.statements.add(indentStr() + fmt);
    }

    void emitAssignment(String targetText, String exprText, SemanticAnalyzer.TypeInfo targetType) {
        if (targetType != null && targetType.isBasic("literal")) {
            current.statements.add(indentStr() + "strcpy(" + targetText + "," + exprText + ");");
        } else {
            current.statements.add(indentStr() + targetText + " = " + exprText + ";");
        }
    }

    void enterBlockWithHeader(String header) {
        current.statements.add(indentStr() + header + " {");
        current.indent++;
    }

    void exitBlock() {
        current.indent = Math.max(0, current.indent - 1);
        current.statements.add(indentStr() + "}");
    }

    void emitIfStart(String expr) {
        enterBlockWithHeader("if (" + expr + ")");
    }

    void emitElseStart() {
        // close previous block and start else
        exitBlock();
        current.statements.add(indentStr() + "else {");
        current.indent++;
    }

    void emitWhileStart(String expr) {
        enterBlockWithHeader("while (" + expr + ")");
    }

    void emitDoWhile(String expr) {
        // used after body: append while(expr);
        current.statements.add(indentStr() + "while (" + expr + ");");
    }

    void emitFor(String var, String start, String end) {
        String header = "for (" + var + " = " + start + "; " + var + " <= " + end + "; " + var + "++)";
        enterBlockWithHeader(header);
    }

    void emitReturn(String expr) {
        if (expr == null || expr.isEmpty()) {
            current.statements.add(indentStr() + "return;");
        } else {
            current.statements.add(indentStr() + "return " + expr + ";");
        }
    }

    void emitProcedureCall(String name, String args) {
        current.statements.add(indentStr() + name + "(" + args + ");");
    }

    void emitSwitchStart(String expr) {
        enterBlockWithHeader("switch (" + expr + ")");
    }

    void emitCaseLabel(String label) {
        current.statements.add(indentStr() + "case " + label + ":");
        current.indent++;
    }

    void emitDefaultLabel() {
        current.statements.add(indentStr() + "default:");
        current.indent++;
    }

    void emitBreakAndCloseCase() {
        current.statements.add(indentStr() + "break;");
        current.indent = Math.max(0, current.indent - 1);
    }

    String getCode() {
        StringBuilder b = new StringBuilder();
        b.append("#include <stdio.h>\n");
        b.append("#include <stdlib.h>\n\n");
        b.append("#include <string.h>\n\n");

        for (String d : typeDeclarations) {
            b.append(d).append("\n\n");
        }
        for (String r : routines) {
            b.append(r).append('\n');
        }

        b.append("int main() {\n");

        for (String d : main.declarations) {
            b.append("    ").append(d).append('\n');
        }
        b.append('\n');

        for (String s : main.statements) {
            b.append(s).append('\n');
        }

        b.append("    return 0;\n");
        b.append("}\n");
        return b.toString();
    }
}
