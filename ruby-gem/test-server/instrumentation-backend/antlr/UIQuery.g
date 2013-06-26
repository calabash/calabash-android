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

@lexer::members {
  public String getErrorMessage(RecognitionException e, String[] tokenNames)
  {
    List stack = getRuleInvocationStack(e, this.getClass().getName());
    String msg = null;
    if ( e instanceof NoViableAltException ) {
      NoViableAltException nvae = (NoViableAltException)e;
      msg = " no viable alt; token="+e.token+" (decision="+nvae.decisionNumber+" state "+nvae.stateNumber+")"+" decision=<<"+nvae.grammarDecisionDescription+">>";
      throw new RuntimeException(msg, e);
    }
    else {
    msg = super.getErrorMessage(e, tokenNames);
    }
    return stack+" "+msg;
  }
  public String getTokenErrorDisplay(Token t) {
    return t.toString();
  }
}

query	:	expr (WHITE! expr)*  
		;
	

expr	:	(className | filter | visibility | predicate | DIRECTION^) 
		;
		
DIRECTION : 'descendant' | 'child' | 'parent' | 'sibling'
		;

className   :   (WILDCARD^ | NAME^ | QUALIFIED_NAME^);

WILDCARD : '*';

QUALIFIED_NAME : NAME ('.' NAME)+;

visibility   :   (ALL^ | VISIBLE^);

ALL : 'all';

VISIBLE : 'visible';


filter : NAME FILTER_COLON^ (INT | STRING | BOOL | NIL);

FILTER_COLON  : ':'
	;

predicate : BEGINPRED^ NAME WHITE! RELATION WHITE! (INT | STRING | BOOL | NIL) ENDPRED!
	;
BEGINPRED : '{'
	;
ENDPRED	  : '}'
	;
	
RELATION : | '=' | '>' | '>=' | '<' | '<=' | 
			(( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' 
		       | 'beginswith' | 'endswith' | 'contains' | 'like') ('[' ('a'..'z' | 'A'..'Z')* ']')?)
		       
	; 

INT :	'0'..'9'+
    ;

BOOL :	'true' | 'false'
    ;

NIL :	'nil' | 'null'
    ;

NAME  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'$')*
    ;

STRING
    :  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\'' 
    |  '"' ( DOUBLE_ESC_SEQ | ~('\\'|'"') )* '"'    
    ;

WHITE   :	' '+ ;
fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
DOUBLE_ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\\')
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
