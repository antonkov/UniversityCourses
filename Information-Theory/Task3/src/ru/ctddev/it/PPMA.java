package ru.ctddev.it;

import java.util.HashSet;
import java.util.Locale;

/**
 * Created by Borys Minaiev on 19.12.2014.
 */
public class PPMA {
    private static String[] proverbs = Main.PROVERBS;
    //private static final String[] proverbs = {"IF WE CANNOT DO AS WE WOULD WE SHOULD DO AS WE CAN"};
    final static int D = 5;

    private static int countOccurrences(String s, String t) {
        int result = 0;
        for (int start = 0; start + t.length() <= s.length(); start++) {
            if (s.substring(start, start + t.length()).equals(t)) {
                result++;
            }
        }
        return result;
    }

    private static void encodePPMA(final String s){
        System.out.println("proverb = " + s + "\n");
        int unused = 1 << 8;
        String was = "";
        System.out.println("|Шаг|Буква|Контекст|tau_s|pr_esc|pr_symbol|");
        System.out.println("|:-|:-|:-|:-|:-|:-|");
        double prEsc = 0, prSymbol = 0;
        for (int pos = 0; pos < s.length(); pos++) {
            System.out.print("|" + (pos + 1) + "|" + (s.charAt(pos) == ' ' ? "\\_" : s.charAt(pos)) + "|");
            int d = 0;
            while (pos - d >= 0 && d <= D && (was.length() == 0 || was.substring(0, was.length() - 1).contains(s.substring(pos - d, pos)))) {
                d++;
            }
            d--;
            String context = s.substring(pos - d, pos);
            if (context.length() == 0) {
                System.out.print("#");
            } else {
                for (int i = 0; i < context.length(); i++) {
                    if (context.charAt(i) == ' ') {
                        System.out.print("\\_");
                    } else {
                        System.out.print(context.charAt(i));
                    }
                }
            }
            System.out.print("|");
            String tau = Integer.toString(countOccurrences(was, context) - 1);
            if (pos == 0) {
                tau = "0";
            }
            double tmpPrEsc = 1, tmpPrSymbol = 1;
            HashSet<Character> cantUse = new HashSet<Character>();
            while (d > 0) {
                if (was.contains(context + s.charAt(pos))) {
                    break;
                }
                int remTmp = 0;
                for (Character c : cantUse) {
                    remTmp += countOccurrences(was, context + c);
                }
                for (int startPos = 0; startPos + context.length() + 1 <= was.length(); startPos++) {
                    if (was.substring(startPos, startPos + context.length()).equals(context)) {
                        cantUse.add(was.charAt(startPos + context.length()));
                    }
                }
                tmpPrEsc *= countOccurrences(was.substring(0, was.length() - 1), context) + 1 - remTmp;
                context = context.substring(1);
                d--;
                tau += "," + Integer.toString(countOccurrences(was, context) - 1);
            }
            System.out.print(tau + "|");
            int rem = 0;
            for (Character c : cantUse) {
                rem += countOccurrences(was, context + c);
            }
            if (d > 0) {
                int cntWithCurrentLetter = countOccurrences(was, context + s.charAt(pos));
                int cntTotal = countOccurrences(was.substring(0, was.length() - 1), context);
                tmpPrSymbol *= (cntTotal + 1.0 - rem) / cntWithCurrentLetter;
            } else {
                if (was.indexOf(s.charAt(pos)) != -1) {
                    int cntCurrentLetter = countOccurrences(was, s.substring(pos, pos + 1));
                    int cntTotal = was.length();
                    tmpPrSymbol *= (cntTotal + 1.0 - rem) / cntCurrentLetter;
                } else {
                    tmpPrEsc *= was.length() + 1 - rem; // esc-symbol
                    tmpPrSymbol *= unused--;
                }
            }
            if (tmpPrEsc != 1) {
                System.out.print(genFraction(1 / tmpPrEsc));
            } else {
                System.out.print(" ");
            }
            System.out.print("|" + genFraction(1 / tmpPrSymbol));
            prEsc += Utils.log2(tmpPrEsc);
            prSymbol += Utils.log2(tmpPrSymbol);
            was += s.charAt(pos);
            System.out.println("|");
        }
        System.out.printf(Locale.US, "Итого = up[ %.4f + %.4f ] + 1 = %d бит\n\n", prEsc, prSymbol, (1 + (int) Math.ceil(prEsc + prSymbol)));
    }

    private static String genFraction(double x) {
        final int MAX_DENOM = 100000;
        final double eps = 1e-9;
        int bestDenom = 1;
        for (int denom = 1; denom <= MAX_DENOM; denom++) {
            if (Math.abs(denom * x - Math.round(denom * x)) <= eps) {
                bestDenom = denom;
            }
        }
        int bestNom = (int) Math.round(bestDenom * x);
        int g = gcd(bestNom, bestDenom);
        bestDenom /= g;
        bestNom /= g;
        return bestDenom == 1 ? Integer.toString(bestNom) : Integer.toString(bestNom) + "/" + Integer.toString(bestDenom);
    }

    private static int gcd(int x, int y) {
        return x == 0 ? y : gcd(y % x, x);
    }

    public static void main(String[] args) {
        System.out.println("PPMA кодирование");
        for (final String proverb : proverbs) {
            encodePPMA(proverb);
        }
    }
}
