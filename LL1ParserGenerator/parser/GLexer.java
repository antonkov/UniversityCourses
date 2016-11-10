import java.io.*;
import java.util.*;
import java.text.ParseException;

public class GLexer {
    InputStream is;
    int curChar;
    int curPos;
    GToken curToken;
    String details;

    public GLexer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
        return c == ' ';
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
    public GToken curToken() {
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
           curToken = GToken.END;
        } else if (curChar == '(') {
           nextChar();
           curToken = GToken.TERMINAL_272827;
        } else if (curChar == ')') {
           nextChar();
           curToken = GToken.TERMINAL_272927;
        } else if (curChar == '*') {
           nextChar();
           curToken = GToken.TERMINAL_272A27;
        } else if (curChar == '+') {
           nextChar();
           curToken = GToken.TERMINAL_272B27;
        } else if (curChar == '-') {
           nextChar();
           curToken = GToken.TERMINAL_272D27;
        } else if (curChar == '/') {
           nextChar();
           curToken = GToken.TERMINAL_272F27;
        } else if (curChar == '0') {
           nextChar();
           curToken = GToken.TERMINAL_273027;
        } else if (curChar == '1') {
           nextChar();
           curToken = GToken.TERMINAL_273127;
        } else if (curChar == '2') {
           nextChar();
           curToken = GToken.TERMINAL_273227;
        } else if (curChar == '3') {
           nextChar();
           curToken = GToken.TERMINAL_273327;
        } else if (curChar == '4') {
           nextChar();
           curToken = GToken.TERMINAL_273427;
        } else if (curChar == '5') {
           nextChar();
           curToken = GToken.TERMINAL_273527;
        } else if (curChar == '6') {
           nextChar();
           curToken = GToken.TERMINAL_273627;
        } else if (curChar == '7') {
           nextChar();
           curToken = GToken.TERMINAL_273727;
        } else if (curChar == '8') {
           nextChar();
           curToken = GToken.TERMINAL_273827;
        } else if (curChar == '9') {
           nextChar();
           curToken = GToken.TERMINAL_273927;
        } else if (curChar == ';') {
           nextChar();
           curToken = GToken.TERMINAL_273B27;
        } else if (curChar == '=') {
           nextChar();
           curToken = GToken.TERMINAL_273D27;
        } else if (curChar == '\n') {
           nextChar();
           curToken = GToken.TERMINAL_275C6E27;
        } else if (curChar == 'a') {
           nextChar();
           curToken = GToken.TERMINAL_276127;
        } else if (curChar == 'b') {
           nextChar();
           curToken = GToken.TERMINAL_276227;
        } else if (curChar == 'c') {
           nextChar();
           curToken = GToken.TERMINAL_276327;
        } else if (curChar == 'd') {
           nextChar();
           curToken = GToken.TERMINAL_276427;
        } else if (curChar == 'e') {
           nextChar();
           curToken = GToken.TERMINAL_276527;
        } else if (curChar == 'l') {
           nextChar();
           curToken = GToken.TERMINAL_276C27;
        } else if (curChar == 't') {
           nextChar();
           curToken = GToken.TERMINAL_277427;
        } else {
            throw new ParseException("Illegal character " + (char) curChar, curPos);
        }
    }
    public static void main(String args[]) {
        System.out.println("Go");
    }

}
