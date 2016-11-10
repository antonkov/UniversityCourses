import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by antonkov on 4/5/14.
 */
public class LexicalAnalyzer {
    InputStream is;
    int curChar;
    int curPos;
    Token curToken;
    String details;

    public LexicalAnalyzer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    private void nextChar() throws ParseException {
        curPos++;
        curChar = -1;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public void nextToken() throws ParseException {
        while (isBlank(curChar)) {
            nextChar();
        }
        if (curChar == '+') {
            nextChar();
            curToken = Token.PLUS;
        } else if (curChar == '?') {
            nextChar();
            curToken = Token.QUEST;
        } else if (curChar == '(') {
            nextChar();
            curToken = Token.LPAREN;
        } else if (curChar == ')') {
            nextChar();
            curToken = Token.RPAREN;
        } else if (curChar == '*') {
            nextChar();
            curToken = Token.STAR;
        } else if (curChar == '|') {
            nextChar();
            curToken = Token.ALT;
        } else if (curChar >= 'a' && curChar <= 'z') {
            details = "" + (char)curChar;
            nextChar();
            curToken = Token.LETTER;
        } else if (curChar == -1) {
            curPos++;
            curToken = Token.END;
        } else {
            throw new ParseException("Illegal character " + (char) curChar, curPos);
        }
    }

    public Token curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }

    public String details() {
        return details;
    }
}
