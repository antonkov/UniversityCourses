import org.StructureGraphic.v1.DSutils;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by antonkov on 4/7/14.
 */
public class Main {
    static void correct(String s) {
        test(s, true);
    }

    static void incorret(String s) {
        test(s, false);
    }

    static int numTest = 1;

    static void test(String s, boolean correctTest) {
        System.err.println("test " + (numTest++) + " : " + (correctTest ? "correct" : "incorrect"));
        try {
            Tree res = parser.parse(new ByteArrayInputStream(s.getBytes("UTF-8")));
            if (numTest == 3)
                DSutils.show(res, 100, 30);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parser = new Parser();
        /*correct("");
        correct("||||");
        incorret("(*)");
        incorret("*");
        incorret("|*");
        incorret("&");
        correct("()*");
        incorret(")");
        incorret("(");
        correct("ab|ca");
        correct("abacaba");
        correct("a(ab|ca)");
        correct("(a|e)b");
        correct("a(b|c)|(d|e)h");
        correct("a(((b|c)))");
        correct("(()())");
        correct("a*");
        correct("ab*c");
        correct("a(b*|c)");
        correct("a(c)*");
        incorret("a**");
        correct("a(b*|c)*");
        correct("(a)((bc))");
        correct("((ab)*)*");
        correct("ab|c|d*");
        correct("((abc*b|a)*ab(aa|b*)b)*");
        */
        correct("a?");
        correct("(aba)+|c?");
        correct("(a?a+)?");
        incorret("??");
        incorret("a?+");
    }

    static Parser parser;
}
