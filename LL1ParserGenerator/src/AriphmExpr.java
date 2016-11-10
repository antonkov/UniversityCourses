import org.StructureGraphic.v1.DSutils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
* Created by antonkov on 6/7/14.
*/

public class AriphmExpr {
    static GParser parser = new GParser();

    public static void main(String[] args) {
        String s = "let a=2;\n" +
                    "let b = 2;\n" +
                    "(b * a + a) * (b - 1) / 3 -2 ;\n" +
                    "let c = 9;\n" +
                    "let d = c * 2;\n" +
                    "d / (1 + 1) - c - 7;\n";
        try {
            GParser.CalcContext res = parser.parse(new ByteArrayInputStream(s.getBytes("UTF-8")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
