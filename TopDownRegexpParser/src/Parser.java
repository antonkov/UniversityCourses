import com.sun.org.apache.bcel.internal.generic.ALOAD;

import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by antonkov on 4/5/14.
 */
public class Parser {
    LexicalAnalyzer lex;

    Tree REGEXP() throws ParseException {
        switch (lex.curToken()) {
            case ALT:
            case LPAREN:
            case LETTER:
                Tree Alt = ALT();
                Tree RegexpPrime = REGEXPPrime();
                return new Tree("REGEXP", Alt, RegexpPrime);
            case RPAREN:
            case END:
                return new Tree("REGEXP", new Tree("epsilon"));
            case STAR:
            case PLUS:
            case QUEST:
                throw new ParseException(lex.curToken() + " not expected " +
                                    "at position " + (lex.curPos() - 1), lex.curPos() - 1);
            default:
                throw new AssertionError();
        }
    }

    Tree REGEXPPrime() throws ParseException {
        switch (lex.curToken()) {
            case ALT:
                lex.nextToken();
                Tree Regexp = REGEXP();
                return new Tree("REGEXPPrime", new Tree("|"), Regexp);
            case RPAREN:
            case END:
                return new Tree("REGEXPPrime", new Tree("epsilon"));
            default:
                throw new AssertionError();
        }
    }

    Tree ALT() throws ParseException {
        switch (lex.curToken()) {
            case LPAREN:
            case LETTER:
                Tree Repeat = REPEAT();
                Tree Alt = ALT();
                return new Tree("ALT", Repeat, Alt);
            case ALT:
            case RPAREN:
            case END:
                return new Tree("ALT", new Tree("epsilon"));
            case STAR:
            case PLUS:
            case QUEST:
                throw new ParseException(lex.curToken() + " not expected " +
                        "at position " + (lex.curPos() - 1), lex.curPos() - 1);
            default:
                throw new AssertionError();
        }
    }

    Tree REPEAT() throws ParseException {
        switch (lex.curToken()) {
            case LPAREN:
            case LETTER:
                Tree Group = GROUP();
                Tree MaybeModif = MAYBEMODIF();
                return new Tree("REPEAT", Group, MaybeModif);
            default:
                throw new AssertionError();
        }
    }

    Tree MAYBEMODIF() throws ParseException {
        switch (lex.curToken()) {
            case STAR:
                lex.nextToken();
                return new Tree("MAYBEMODIF", new Tree("*"));
            case PLUS:
                lex.nextToken();
                return new Tree("MAYBEMODIF", new Tree("+"));
            case QUEST:
                lex.nextToken();
                return new Tree("MAYBEMODIF", new Tree("?"));
            case LPAREN:
            case RPAREN:
            case LETTER:
            case ALT:
            case END:
                return new Tree("MAYBEMODIF", new Tree("epsilon"));
            default:
                throw new AssertionError();
        }
    }

    Tree GROUP() throws ParseException {
        switch (lex.curToken()) {
            case LPAREN:
                lex.nextToken();
                Tree Regexp = REGEXP();
                if (lex.curToken() != Token.RPAREN) {
                    throw new ParseException(") expected at position " + (lex.curPos() - 1),
                            lex.curPos() - 1);
                }
                lex.nextToken();
                return new Tree("GROUP", new Tree("("), Regexp, new Tree(")"));
            case LETTER:
                Tree Base = BASE();
                return new Tree("GROUP", Base);
            default:
                throw new AssertionError();
        }
    }

    Tree BASE() throws ParseException {
        switch (lex.curToken()) {
            case LETTER:
                Tree res = new Tree("BASE", new Tree(lex.details()));
                lex.nextToken();
                return res;
            default:
                throw new AssertionError();
        }
    }

    Tree parse(InputStream is) throws ParseException {
        lex = new LexicalAnalyzer(is);
        lex.nextToken();
        Tree res = REGEXP();
        if (lex.curToken() != Token.END)
            throw new ParseException("expected end of expression, but found " + lex.curToken(), lex.curPos());
        return res;
    }
}
