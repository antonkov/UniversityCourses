grammar ParseGen ;

rules         : ((ruleForLexer | ruleForParser) ';' )* ;
ruleForLexer  : LEXERID  ':' production ('|' production)* ;
ruleForParser : PARSERID ':' production ('|' production)* ;
production    : node* ;
node          : PARSERID | LEXERID | LEXEM ;

LEXERID  : [A-Z] ID? ;
PARSERID : [a-z] ID? ;
ID       : [A-Za-z0-9_]+ ;
WS       : ([ \t] | NEWLINE) -> skip;
NEWLINE  : '\r'? '\n';
LEXEM    : '\'' . '\'' ;