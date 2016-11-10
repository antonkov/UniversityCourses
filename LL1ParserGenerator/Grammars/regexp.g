@header {
import java.io.*;
import java.util.*;
}

@members {
int cur;
TreeMap<Integer, Integer> memory = new TreeMap();
}

regexp <int a> returns <int res>   : thisIsLabel = alt regexp2<#a = 3>{ System.out.println($alt.text); } | ;
regexp2  <int a> : '|' regexp | ;
alt      : repeat alt | ;
repeat   : group maymodif ;
maymodif : '*' | '?' | '+' | ;
group    : let | '(' regexp ')' ;
let      : 'a' | 'b' | 'c' ;