import java.io.*;
import java.util.*;
import java.text.ParseException;

public class RegexpParser {
   public static void main(String args[]) {
       System.out.println("Go");
   }

public void alt() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           return new Tree("alt");
       case TERMINAL_272827 : // '('
           trees.add(repeat());
           trees.add(alt());
           return new Tree("alt", trees.get(0), trees.get(1));
       case TERMINAL_272927 : // ')'
           return new Tree("alt");
       case TERMINAL_272A27 : // '*'
           throw new ParseException("not expected symbol");
       case TERMINAL_272B27 : // '+'
           throw new ParseException("not expected symbol");
       case TERMINAL_273F27 : // '?'
           throw new ParseException("not expected symbol");
       case TERMINAL_276127 : // 'a'
           trees.add(repeat());
           trees.add(alt());
           return new Tree("alt", trees.get(0), trees.get(1));
       case TERMINAL_277C27 : // '|'
           return new Tree("alt");
   }
}
public void group() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           throw new ParseException("not expected symbol");
       case TERMINAL_272827 : // '('
           match(TERMINAL_272827);
           trees.add(new Tree("'('" + " " + lex.details()));
           lex.nextToken();
           trees.add(regexp());
           match(TERMINAL_272927);
           trees.add(new Tree("')'" + " " + lex.details()));
           lex.nextToken();
           return new Tree("group", trees.get(0), trees.get(1), trees.get(2));
       case TERMINAL_272927 : // ')'
           throw new ParseException("not expected symbol");
       case TERMINAL_272A27 : // '*'
           throw new ParseException("not expected symbol");
       case TERMINAL_272B27 : // '+'
           throw new ParseException("not expected symbol");
       case TERMINAL_273F27 : // '?'
           throw new ParseException("not expected symbol");
       case TERMINAL_276127 : // 'a'
           match(TERMINAL_276127);
           trees.add(new Tree("'a'" + " " + lex.details()));
           lex.nextToken();
           return new Tree("group", trees.get(0));
       case TERMINAL_277C27 : // '|'
           throw new ParseException("not expected symbol");
   }
}
public void maymodif() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           return new Tree("maymodif");
       case TERMINAL_272827 : // '('
           return new Tree("maymodif");
       case TERMINAL_272927 : // ')'
           return new Tree("maymodif");
       case TERMINAL_272A27 : // '*'
           match(TERMINAL_272A27);
           trees.add(new Tree("'*'" + " " + lex.details()));
           lex.nextToken();
           return new Tree("maymodif", trees.get(0));
       case TERMINAL_272B27 : // '+'
           match(TERMINAL_272B27);
           trees.add(new Tree("'+'" + " " + lex.details()));
           lex.nextToken();
           return new Tree("maymodif", trees.get(0));
       case TERMINAL_273F27 : // '?'
           match(TERMINAL_273F27);
           trees.add(new Tree("'?'" + " " + lex.details()));
           lex.nextToken();
           return new Tree("maymodif", trees.get(0));
       case TERMINAL_276127 : // 'a'
           return new Tree("maymodif");
       case TERMINAL_277C27 : // '|'
           return new Tree("maymodif");
   }
}
public void regexp() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           return new Tree("regexp");
       case TERMINAL_272827 : // '('
           trees.add(alt());
           trees.add(regexp2());
           return new Tree("regexp", trees.get(0), trees.get(1));
       case TERMINAL_272927 : // ')'
           return new Tree("regexp");
       case TERMINAL_272A27 : // '*'
           throw new ParseException("not expected symbol");
       case TERMINAL_272B27 : // '+'
           throw new ParseException("not expected symbol");
       case TERMINAL_273F27 : // '?'
           throw new ParseException("not expected symbol");
       case TERMINAL_276127 : // 'a'
           trees.add(alt());
           trees.add(regexp2());
           return new Tree("regexp", trees.get(0), trees.get(1));
       case TERMINAL_277C27 : // '|'
           trees.add(alt());
           trees.add(regexp2());
           return new Tree("regexp", trees.get(0), trees.get(1));
   }
}
public void regexp2() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           return new Tree("regexp2");
       case TERMINAL_272827 : // '('
           throw new ParseException("not expected symbol");
       case TERMINAL_272927 : // ')'
           return new Tree("regexp2");
       case TERMINAL_272A27 : // '*'
           throw new ParseException("not expected symbol");
       case TERMINAL_272B27 : // '+'
           throw new ParseException("not expected symbol");
       case TERMINAL_273F27 : // '?'
           throw new ParseException("not expected symbol");
       case TERMINAL_276127 : // 'a'
           throw new ParseException("not expected symbol");
       case TERMINAL_277C27 : // '|'
           match(TERMINAL_277C27);
           trees.add(new Tree("'|'" + " " + lex.details()));
           lex.nextToken();
           trees.add(regexp());
           return new Tree("regexp2", trees.get(0), trees.get(1));
   }
}
public void repeat() {
   ArrayList<Tree> trees = new ArrayList<Tree>();
   switch (lex.curToken()) {
       case END : // 
           throw new ParseException("not expected symbol");
       case TERMINAL_272827 : // '('
           trees.add(group());
           trees.add(maymodif());
           return new Tree("repeat", trees.get(0), trees.get(1));
       case TERMINAL_272927 : // ')'
           throw new ParseException("not expected symbol");
       case TERMINAL_272A27 : // '*'
           throw new ParseException("not expected symbol");
       case TERMINAL_272B27 : // '+'
           throw new ParseException("not expected symbol");
       case TERMINAL_273F27 : // '?'
           throw new ParseException("not expected symbol");
       case TERMINAL_276127 : // 'a'
           trees.add(group());
           trees.add(maymodif());
           return new Tree("repeat", trees.get(0), trees.get(1));
       case TERMINAL_277C27 : // '|'
           throw new ParseException("not expected symbol");
   }
}
   Tree parse(InputStream is) throws ParseException {
       return new Tree("a");
   }
}
