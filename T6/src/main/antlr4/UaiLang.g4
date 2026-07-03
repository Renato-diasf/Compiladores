grammar UaiLang;

@header {
package br.com.compiladores.uailang;
}

// UaiLang e uma linguagem pequena, com vocabulario inspirado no jeito mineiro,
// que compila programas imperativos simples para Python 3.
programa
    : UAI IDENT comando* CABOU EOF
    ;

comando
    : declaracao PONTO
    | atribuicao PONTO
    | leitura PONTO
    | escrita PONTO
    | condicional
    | repeticaoEnquanto
    | repeticaoPara
    ;

declaracao
    : TREM IDENT DOIS_PONTOS tipo (IGUAL expressao)?
    ;

atribuicao
    : IDENT IGUAL expressao
    ;

leitura
    : ESCUITA IDENT
    ;

escrita
    : MOSTRA expressao (VIRGULA expressao)*
    ;

condicional
    : SE expressao ENTAO comando* (SENAO comando*)? FIMSE PONTO?
    ;

repeticaoEnquanto
    : ENQUANTO expressao FAZ comando* FIMENQUANTO PONTO?
    ;

repeticaoPara
    : PRA IDENT DE expressao ATE expressao FAZ comando* FIMPRA PONTO?
    ;

tipo
    : INTEIRO
    | REAL
    | TEXTO
    | LOGICO
    ;

expressao
    : ouExpr
    ;

ouExpr
    : eExpr (OU eExpr)*
    ;

eExpr
    : igualdadeExpr (E igualdadeExpr)*
    ;

igualdadeExpr
    : relExpr ((IGUAL_IGUAL | DIFERENTE) relExpr)*
    ;

relExpr
    : adExpr ((MENOR | MENOR_IGUAL | MAIOR | MAIOR_IGUAL) adExpr)*
    ;

adExpr
    : multExpr ((MAIS | MENOS) multExpr)*
    ;

multExpr
    : unExpr ((VEZES | DIVIDIDO | RESTO) unExpr)*
    ;

unExpr
    : (NAO | MENOS) unExpr
    | primario
    ;

primario
    : NUM_REAL
    | NUM_INT
    | TEXTO_LITERAL
    | VERDADE
    | MENTIRA
    | IDENT
    | ABRE_PAR expressao FECHA_PAR
    ;

UAI: 'uai';
CABOU: 'cabou';
TREM: 'trem';
ESCUITA: 'escuita';
MOSTRA: 'mostra';
SE: 'se';
ENTAO: 'entao';
SENAO: 'senao';
FIMSE: 'fimse';
ENQUANTO: 'inté';
FAZ: 'faz';
FIMENQUANTO: 'prontim';
PRA: 'pra';
DE: 'de';
ATE: 'ate';
FIMPRA: 'fimpra';
INTEIRO: 'inteiro';
REAL: 'real';
TEXTO: 'texto';
LOGICO: 'logico';
VERDADE: 'verdade';
MENTIRA: 'mentira';
E: 'e';
OU: 'ou';
NAO: 'nao';

IGUAL_IGUAL: '==';
DIFERENTE: '!=';
MENOR_IGUAL: '<=';
MAIOR_IGUAL: '>=';
IGUAL: '=';
MENOR: '<';
MAIOR: '>';
MAIS: '+';
MENOS: '-';
VEZES: '*';
DIVIDIDO: '/';
RESTO: '%';
ABRE_PAR: '(';
FECHA_PAR: ')';
DOIS_PONTOS: ':';
VIRGULA: ',';
PONTO: '.';

NUM_REAL: [0-9]+ '.' [0-9]+;
NUM_INT: [0-9]+;
TEXTO_LITERAL: '"' (~["\\\r\n] | '\\' ["\\/bfnrt])* '"';
IDENT: [a-zA-Z_][a-zA-Z_0-9]*;

COMENTARIO: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;
