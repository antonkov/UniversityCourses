grammar ParserGen ;
import LexerRules ;

program       : header? members? rules ;
header        : HEADERID TRANSSYM ;
members       : MEMBERSID TRANSSYM ;
rules         : ((ruleForLexer | ruleForParser) ';' )* ;
ruleForLexer  : LEXERID  ':' production ('|' production)* ;
ruleForParser : PARSERID inherited? synthesized? ':' production ('|' production)* ;
inherited     : ATTRS ;
synthesized   : 'returns' ATTRS ;
production    : TRANSSYM? node*;
node          : (label '=')? (PARSERID | LEXERID | LEXEM) ATTRS? TRANSSYM? ;
label         : PARSERID ;