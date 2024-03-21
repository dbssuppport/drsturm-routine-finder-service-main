grammar RoutineFinder;

@header {
package com.e2x.bigcommerce.routinefinder.antlr.generated;
}

// parser
start
    : expr
    ;

and_or
    : AND
    | OR
    ;

expr
    : match_rule
    | match_rule and_or expr
    ;

in_operand
    : IN
    | NOT IN
    ;

is_operand
    : IS
    | IS NOT
    ;

match_rule
    : QUESTION is_operand VALUE
    | QUESTION in_operand '(' values ')'
    ;

values
    : VALUE
    | VALUE ',' values
    ;


// Lexer
OR
    : 'or'
    ;

AND
    : 'and'
    ;

COMMA
    : ','
    ;

LB
    : '('
    ;
RB
    : ')'
    ;

NOT
    : 'not'
    ;

IS
    : WS + 'is' + WS
    ;

IN
    : WS + 'in' + WS
    ;

QUESTION
    : 'acne located'
    | 'age'
    | 'best describes'
    | 'blue light'
    | 'skin breakout'
    | 'daily usage'
    | 'dark circle'
    | 'does it feel dry'
    | 'dryness goes away'
    | 'experience these problems'
    | 'minimize wrinkles'
    | 'occasional breakout'
    | 'oily no breakouts'
    | 'pigmentation'
    | 'routine type'
    | 'scarring'
    | 'skin concerns'
    | 'skin tone'
    | 'skin type'
    | 'sun protection'
    | 'throughout the day'
    | 'your skin feel'
    | 'large pores'
    | 'skincare or hair and scalp'
    | 'hair type'
    | 'texture'
    | 'color'
    | 'scalp'
    | 'hair concern'
    | 'scalp concern'
    ;

WS
    : (' ' | '\t')+ -> skip
    ;

VALUE
    : ('a'..'z' | 'A'..'Z' | '0'..'9' | '+' | '-' | 'very dark' | 'not important' | 'occa redness' | 'occa breakout' | 'hormonal acne' | 'oily skin' | 'dull skin' | 'dry skin' | '3 times' | 'a few times' | 'hair & scalp' | 'colored dyed bleached' | 'coloured one' | 'greasy oily' | 'lacking volume' | 'hair loss' | 'no concerns' | 'dry itchy sensitive')+
    ;

ANY
    : .
    ;
