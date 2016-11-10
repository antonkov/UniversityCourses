import java.io.*;
import java.util.*;
import java.text.ParseException;

public class Lexer {
    InputStream is;
    int curChar;
    int curPos;
    Token curToken;
    String details;

    public Lexer(InputStream is) throws ParseException {
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
    public Token curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }

    public String details() {
        return details;
    }
    public void nextToken() throws ParseException {
        while (isBlank(curChar)) {
            nextChar();
        }

        if (curChar == -1) {
           nextChar();
           curToken = Token.END;
        } else if (curChar == '(') {
           nextChar();
           curToken = Token.TERMINAL_272827;
        } else if (curChar == ')') {
           nextChar();
           curToken = Token.TERMINAL_272927;
        } else if (curChar == '*') {
           nextChar();
           curToken = Token.TERMINAL_272A27;
        } else if (curChar == '+') {
           nextChar();
           curToken = Token.TERMINAL_272B27;
        } else if (curChar == '?') {
           nextChar();
           curToken = Token.TERMINAL_273F27;
        } else if (curChar == 'a') {
           nextChar();
           curToken = Token.TERMINAL_276127;
        } else if (curChar == 'b') {
           nextChar();
           curToken = Token.TERMINAL_276227;
        } else if (curChar == 'c') {
           nextChar();
           curToken = Token.TERMINAL_276327;
        } else if (curChar == '|') {
           nextChar();
           curToken = Token.TERMINAL_277C27;
        } else {
            throw new ParseException("Illegal character " + (char) curChar, curPos);
        }
    }
    public static void main(String args[]) {
        System.out.println("Go");
    }

}
