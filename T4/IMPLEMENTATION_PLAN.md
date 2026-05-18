# T4 - Implementation Plan

This plan is based on [T4 - especificação e critérios.pdf](/Users/muriloramos/workspaces/academic/Compiladores/T4/T4 - especificação e critérios.pdf).

## Goal

Implement the second semantic-analysis stage for the LA language, extending the T3 analyzer with the extra semantic checks required for T4, while preserving the command-line contract and file-based output format expected by the test cases.

## Requirements Summary

The analyzer must:

1. Detect all T3 semantic errors.
2. Detect five additional semantic error categories:
   - Use of an identifier already declared in the current scope, including pointer/record/function variants.
   - Use of an undeclared identifier, including pointer/record/function variants.
   - Mismatch between actual arguments and formal parameters in procedure/function calls.
   - Incompatible assignment types, now including pointers and records.
   - Illegal use of `retorne` inside a scope.
3. Continue processing after errors and report all of them until end of file.
4. Read the input file path and output file path from command-line arguments.
5. Write the diagnostics to the output file, never only to terminal.
6. Be buildable/runnable from the command line on Windows, macOS, or Linux.

## Proposed Deliverables Inside `T4`

Create a self-contained T4 implementation layout such as:

- `T4/src/main/java/...` for source code
- `T4/README.md` for external documentation
- `T4/IMPLEMENTATION_PLAN.md` for this roadmap
- reuse the existing `casos-de-teste/` directory for validation inputs and expected outputs

If the project already follows the T1/T2/T3 structure, reuse that layout instead of inventing a new one.

## Architecture Plan

### 1. Reuse the lexical/syntactic front-end

Keep the parser and AST generation from the previous tasks unchanged if possible.

Only extend what is necessary to support semantic analysis:

- AST node annotations
- symbol table/scoping support
- type information
- function/procedure signatures
- error collection

### 2. Add a semantic analysis layer

Create a dedicated semantic pass after parsing.

Recommended subcomponents:

- `SemanticAnalyzer`
- `SymbolTable` or `ScopeStack`
- `TypeSystem`
- `SemanticErrorReporter`
- `FunctionSignature` / `ParameterInfo`
- `RecordInfo` / `TypeInfo`

### 3. Make analysis non-fatal

The analyzer must not stop at the first error.

Implementation idea:

- collect errors in a list
- after each relevant semantic rule, append an error instead of throwing
- use error-recovery-friendly checks so analysis can continue when possible

### 4. Preserve expected output format

The analyzer should emit diagnostics exactly in the format used by the course test cases.

Typical rule:

- one diagnostic per line
- line numbers must be the line of the offending token
- if the test suite expects a specific prefix or wording, match it exactly

## Semantic Rules To Implement

### A. Redeclaration in the same scope

Detect when an identifier is declared more than once in the same scope.

Apply this to:

- variables
- constants
- procedures
- functions
- types

Also ensure the rule works for declarations involving:

- pointers
- records
- functions/procedures

### B. Undeclared identifier use

Detect references to identifiers that were never declared in any visible scope.

Include:

- variable access
- constant use
- procedure/function call
- type name usage
- pointer and record-related references

### C. Call argument validation

Verify that actual arguments match formal parameters in:

- quantity
- order
- type compatibility

Type compatibility should follow the assignment/conversion rules required by the statement:

- `endereco -> ponteiro`
- `real -> real`
- `inteiro -> inteiro`
- `literal -> literal`
- `logico -> logico`
- `registro -> registro` with the same type name

### D. Assignment compatibility

Check whether the left-hand side and right-hand side types are compatible.

Extend the T3 rules to include:

- pointer assignment rules
- record assignment rules
- expression type propagation

If the expression type becomes undefined because of an invalid subexpression, suppress cascading failures when possible but still report the original issue.

### E. Illegal `retorne`

Track whether the current scope is inside a function or procedure that allows `retorne`.

Report an error when `retorne` appears in a scope where it is forbidden.

## Implementation Phases

### Phase 1 - Project setup

1. Inspect the existing T1/T2/T3 codebase structure.
2. Reuse the same build tool, package layout, and execution entry point.
3. Create the T4 module/folder structure.
4. Add a command-line interface that accepts:
   - input file path
   - output file path

### Phase 2 - Symbol table and scope model

1. Implement nested scopes.
2. Store declarations by kind:
   - variables
   - constants
   - procedures
   - functions
   - types
3. Add duplicate-declaration checks in the current scope.
4. Add lookup across enclosing scopes.

### Phase 3 - Type model

1. Represent primitive types.
2. Represent pointer types.
3. Represent record types.
4. Represent named types and aliases if they are part of the language model.
5. Implement a compatibility checker.

### Phase 4 - Semantic traversal

1. Walk the AST after parsing.
2. Register declarations in the correct scope.
3. Resolve identifier uses.
4. Validate calls, assignments, expressions, and `retorne`.
5. Keep collecting errors until the end.

### Phase 5 - Error formatting

1. Match the official wording and line numbering expected by the tests.
2. Ensure diagnostics are written to the output file in the correct order.
3. If multiple errors occur, preserve the natural source order.

### Phase 6 - Documentation

Create `T4/README.md` with:

- how to compile
- how to run
- required runtime/version
- sample invocation
- explanation of the output format
- group members, if applicable

## Suggested Internal File Responsibilities

If the codebase is Java-based, a clean split would be:

- `Main.java` - command-line entry and file I/O
- `Lexer`/`Parser` - reuse from previous task
- `SemanticAnalyzer.java` - main analysis pass
- `Scope.java` / `SymbolTable.java` - symbol management
- `Type.java` / `TypeChecker.java` - type compatibility
- `SemanticError.java` / `ErrorCollector.java` - diagnostics

## Validation Strategy

Use the provided T4 test suite to validate:

1. acceptance cases with no semantic errors
2. T3 legacy semantic error cases
3. each of the 5 new T4 error categories
4. mixed programs with multiple errors
5. output file creation and exact message formatting

Suggested local checks:

- run the analyzer against a single input/output pair
- diff the produced output against the expected file
- run the full suite under `casos-de-teste/` if it matches the T4 expectations

## Definition of Done

The T4 work is complete when:

- all required semantic errors are detected
- the analyzer keeps running after errors
- diagnostics are written to the output file
- the project runs from the command line with two arguments
- the README explains build and execution
- the implementation passes the official T4 test cases

## Execution Order Recommendation

1. Confirm the current project structure from T1/T3.
2. Implement the scope and type model.
3. Wire semantic checks into the AST traversal.
4. Match diagnostic wording to the test suite.
5. Add/update documentation.
6. Run the official tests and fix mismatches.
