import java.io.*;
import java.util.*;
import java.text.ParseException;

public class Parser {
   Lexer lex;
   public static void main(String args[]) {
       System.out.println("Go");
   }

   void match(Token token) throws ParseException {
       if (token != lex.curToken())
           throw new ParseException("not match", 0);
   }

   public Tree alt() throws ParseException {
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
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               trees.add(repeat());
               trees.add(alt());
               return new Tree("alt", trees.get(0), trees.get(1));
           case TERMINAL_276227 : // 'b'
               trees.add(repeat());
               trees.add(alt());
               return new Tree("alt", trees.get(0), trees.get(1));
           case TERMINAL_276327 : // 'c'
               trees.add(repeat());
               trees.add(alt());
               return new Tree("alt", trees.get(0), trees.get(1));
           case TERMINAL_277C27 : // '|'
               return new Tree("alt");
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree group() throws ParseException {
       ArrayList<Tree> trees = new ArrayList<Tree>();
       switch (lex.curToken()) {
           case END : // 
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272827 : // '('
               match(Token.TERMINAL_272827);
               trees.add(new Tree("'('" + " " + lex.details()));
               lex.nextToken();
               trees.add(regexp());
               match(Token.TERMINAL_272927);
               trees.add(new Tree("')'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("group", trees.get(0), trees.get(1), trees.get(2));
           case TERMINAL_272927 : // ')'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272A27 : // '*'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               trees.add(let());
               return new Tree("group", trees.get(0));
           case TERMINAL_276227 : // 'b'
               trees.add(let());
               return new Tree("group", trees.get(0));
           case TERMINAL_276327 : // 'c'
               trees.add(let());
               return new Tree("group", trees.get(0));
           case TERMINAL_277C27 : // '|'
               throw new ParseException("not expected symbol", 0);
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree let() throws ParseException {
       ArrayList<Tree> trees = new ArrayList<Tree>();
       switch (lex.curToken()) {
           case END : // 
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272827 : // '('
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272927 : // ')'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272A27 : // '*'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               match(Token.TERMINAL_276127);
               trees.add(new Tree("'a'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("let", trees.get(0));
           case TERMINAL_276227 : // 'b'
               match(Token.TERMINAL_276227);
               trees.add(new Tree("'b'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("let", trees.get(0));
           case TERMINAL_276327 : // 'c'
               match(Token.TERMINAL_276327);
               trees.add(new Tree("'c'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("let", trees.get(0));
           case TERMINAL_277C27 : // '|'
               throw new ParseException("not expected symbol", 0);
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree maymodif() throws ParseException {
       ArrayList<Tree> trees = new ArrayList<Tree>();
       switch (lex.curToken()) {
           case END : // 
               return new Tree("maymodif");
           case TERMINAL_272827 : // '('
               return new Tree("maymodif");
           case TERMINAL_272927 : // ')'
               return new Tree("maymodif");
           case TERMINAL_272A27 : // '*'
               match(Token.TERMINAL_272A27);
               trees.add(new Tree("'*'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("maymodif", trees.get(0));
           case TERMINAL_272B27 : // '+'
               match(Token.TERMINAL_272B27);
               trees.add(new Tree("'+'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("maymodif", trees.get(0));
           case TERMINAL_273F27 : // '?'
               match(Token.TERMINAL_273F27);
               trees.add(new Tree("'?'" + " " + lex.details()));
               lex.nextToken();
               return new Tree("maymodif", trees.get(0));
           case TERMINAL_276127 : // 'a'
               return new Tree("maymodif");
           case TERMINAL_276227 : // 'b'
               return new Tree("maymodif");
           case TERMINAL_276327 : // 'c'
               return new Tree("maymodif");
           case TERMINAL_277C27 : // '|'
               return new Tree("maymodif");
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree regexp() throws ParseException {
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
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               trees.add(alt());
               trees.add(regexp2());
               return new Tree("regexp", trees.get(0), trees.get(1));
           case TERMINAL_276227 : // 'b'
               trees.add(alt());
               trees.add(regexp2());
               return new Tree("regexp", trees.get(0), trees.get(1));
           case TERMINAL_276327 : // 'c'
               trees.add(alt());
               trees.add(regexp2());
               return new Tree("regexp", trees.get(0), trees.get(1));
           case TERMINAL_277C27 : // '|'
               trees.add(alt());
               trees.add(regexp2());
               return new Tree("regexp", trees.get(0), trees.get(1));
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree regexp2() throws ParseException {
       ArrayList<Tree> trees = new ArrayList<Tree>();
       switch (lex.curToken()) {
           case END : // 
               return new Tree("regexp2");
           case TERMINAL_272827 : // '('
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272927 : // ')'
               return new Tree("regexp2");
           case TERMINAL_272A27 : // '*'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276227 : // 'b'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276327 : // 'c'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_277C27 : // '|'
               match(Token.TERMINAL_277C27);
               trees.add(new Tree("'|'" + " " + lex.details()));
               lex.nextToken();
               trees.add(regexp());
               return new Tree("regexp2", trees.get(0), trees.get(1));
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   public Tree repeat() throws ParseException {
       ArrayList<Tree> trees = new ArrayList<Tree>();
       switch (lex.curToken()) {
           case END : // 
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272827 : // '('
               trees.add(group());
               trees.add(maymodif());
               return new Tree("repeat", trees.get(0), trees.get(1));
           case TERMINAL_272927 : // ')'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272A27 : // '*'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_272B27 : // '+'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_273F27 : // '?'
               throw new ParseException("not expected symbol", 0);
           case TERMINAL_276127 : // 'a'
               trees.add(group());
               trees.add(maymodif());
               return new Tree("repeat", trees.get(0), trees.get(1));
           case TERMINAL_276227 : // 'b'
               trees.add(group());
               trees.add(maymodif());
               return new Tree("repeat", trees.get(0), trees.get(1));
           case TERMINAL_276327 : // 'c'
               trees.add(group());
               trees.add(maymodif());
               return new Tree("repeat", trees.get(0), trees.get(1));
           case TERMINAL_277C27 : // '|'
               throw new ParseException("not expected symbol", 0);
           default :                throw new ParseException("not expected symbol", 0);
      }
   }

   Tree parse(InputStream is) throws ParseException {
        lex = new Lexer(is);
        lex.nextToken();
        Tree res = regexp();
        if (lex.curToken() != Token.END)
            throw new ParseException("expected end of expression, but found " + lex.curToken(), lex.curPos());
        return res;
   }
}
