TopDownRegexpParser
===================

###Интуитивная грамматика

REGEXP -> ALT       
        | ALT '|' REGEXP

ALT    -> REPEAT ALT    
        | eps

REPEAT -> GROUP         
        | GROUP '*'

GROUP  -> BASE          
        | '(' REGEXP ')'

BASE   -> 'a'|'b'|...|'z'

* **REGEXP** - регулярное выражение
* **ALT** - один из альтернативных паттернов, разделенных '|', или его хвост
  без нескольких REPEAT
* **REPEAT** - GROUP, возможно с операция замыкания Клини, примененной к ней
* **GROUP** - часть выражения, рассматриваемая как единое выражение -
регулярное выражение в скобках, в частности базовый язык
* **BASE** - базовые языки состоящие из маленьких букв латинского алфавита

###Устранение левой рекурсии и правого ветвления

Левой рекурсии в грамматике нет, устраним правое ветвление

REGEXP -> ALT REGEXP'
        | eps

REGEXP'-> '|' REGEXP  
        | eps

ALT    -> REPEAT ALT    
        | eps

REPEAT -> GROUP MAYBESTAR        
        
MAYBESTAR -> '*'  
           | eps

GROUP  -> BASE          
        | '(' REGEXP ')'

BASE   -> 'a'|'b'|...|'z'

* **MAYBESTAR** - возможный оператор замыкания Клини
* **REGEXP'** - продолжение регулярного выражения

|             | FIRST      |     FOLLOW    |
|:------------|:----------:|:-------------:|
| REGEXP      | I( a e     |   )  $        |
| REGEXP'     | I    e     |   )  $        |
| ALT         |  ( a e     | I )  $        |
| REPEAT      |  ( a       | I()a $        |
| MAYBESTAR   |     *e     | I()a $        |
| GROUP       |  ( a       | I()a*$        |
| BASE        |    a       | I()a*$        |

e - epsilon    
a - a..z      
I - | (alternation)       

### Тесты
|  Тест                | Описание               |
|:--------------------:|:-----------------------|
|                      ||
|   IIII               ||
|   (*)                ||
|    *                 ||
|    I*                ||
|       &              ||
|    ()*               ||
| )                    ||
| (                    ||
| abIca                ||
| abacaba              ||
|  a(abIca)            ||
|  (aIe)b              ||
|  a(bIc)I(dIe)h       ||
|  a(((bIc)))          ||
| (()())               ||
| a*                   ||
| ab*c                 ||
| a(b*Ic)              ||
| a(c)*                ||
| a**                  ||
| a(b*Ic)*             ||
| (ab)((c))            ||
| (ab)((c))            ||
| ((ab)*)*             ||
| abIcId*              ||
|((abc*bIa)*ab(aaIb*)b)*||
