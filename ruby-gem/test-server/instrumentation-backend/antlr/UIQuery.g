grammar UIQuery;
options {
    output=AST;
    ASTLabelType=CommonTree;
}


@header {
    package sh.calaba.instrumentationbackend.query.antlr;
}
@lexer::header {
    package sh.calaba.instrumentationbackend.query.antlr;
}


query	:	expr (WHITE! expr)*  
		;
	

expr	:	(className | filter) 
		;

className   :   (NAME^ | QUALIFIED_NAME^);

QUALIFIED_NAME : NAME ('.' NAME)+;

filter : NAME FILTER_COLON^ (INT | STRING | BOOL | NIL);

FILTER_COLON  : ':'
	;


INT :	'0'..'9'+
    ;

BOOL :	'true' | 'false'
    ;

NIL :	'nil' | 'null'
    ;

NAME  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

STRING
    :  '\'' ( ESC_SEQ | ~('\\'|'"') )* '\''
    ;

WHITE   :	' '* ;
fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
