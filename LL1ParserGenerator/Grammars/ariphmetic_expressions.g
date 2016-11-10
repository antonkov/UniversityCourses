@header {import java.util.*;}

@members {Map<String, Integer> memory = new HashMap<String, Integer>();}

calc : statement calc | ;

statement : let id assign expr ent_stmt endline { System.out.println($id.text + "=" + $expr.value); memory.put($id.text, $expr.value); }
     | expr ent_stmt endline { System.out.println($expr.value); }
     ;

expr returns<int value>
  : term expr_prime<#i = $term.value> {$value = $expr_prime.value;}
  ;

expr_prime<int i> returns<int value>
  : {$value = $i;}
  | plus term e1=expr_prime<#i = $i + $term.value> {$value = $e1.value;}
  | minus term e2=expr_prime<#i = $i - $term.value> {$value = $e2.value;}
  ;

term returns<int value>
  : factor term_prime<#i = $factor.value> {$value = $term_prime.value;}
  ;

term_prime<int i> returns<int value>
  : {$value = $i;}
  | mul factor e3=term_prime<#i = $i * $factor.value> {$value = $e3.value;}
  | div factor e4=term_prime<#i = $i / $factor.value> {$value = $e4.value;}
  ;

factor returns<int value>
  : integer {$value = Integer.parseInt($integer.text);}
  | id {$value = memory.get($id.text);}
  | left_paren expr right_paren {$value = $expr.value;}
  ;

let : 'l' 'e' 't' ;
integer : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';
id : 'a' | 'b' | 'c' | 'd';
endline : '\n';
ent_stmt : ';';
assign : '=';
plus : '+';
minus : '-';
mul : '*';
div : '/';
left_paren : '(';
right_paren : ')';