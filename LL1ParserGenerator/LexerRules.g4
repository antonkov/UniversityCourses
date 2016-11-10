lexer grammar LexerRules ;

HEADERID : '@header' ;
MEMBERSID: '@members' ;
ATTRS    : '<' (~[<>]+ ATTRS?)*    '>' ;
TRANSSYM : '{' (~[{}]+ TRANSSYM?)* '}' ;
LEXERID  : [A-Z] ID? ;
PARSERID : [a-z] ID? ;
ID       : [A-Za-z0-9_]+ ;
WS       : ([ \t] | NEWLINE) -> skip;
LEXEM    : '\'' .+? '\'' ;

fragment
NEWLINE  : '\r'? '\n';