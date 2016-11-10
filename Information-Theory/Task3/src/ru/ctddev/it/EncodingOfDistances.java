package ru.ctddev.it;

import java.util.Arrays;

/**
 * Created by antonkov on 1/16/2015.
 */

public class EncodingOfDistances {
    //private static String[] proverbs = Main.PROVERBS;
    private static final String[] proverbs = {"CC######LLWWWWISNUUAANNHWDD#AAOOO#####OOEEDTESFDSE"};

    public static void print(char[] seq, int[] indices) {
        boolean[] aim = new boolean[seq.length];
        for (int idx : indices)
                aim[idx] = true;
        for (int i = 0; i < seq.length; i++) {
            if (aim[i])
                System.out.print("<span style=\"background-color:yellow\">");
            System.out.print(seq[i]);
            if (aim[i])
                System.out.print("</span>");
        }
    }

    public static int encode(String s) {
        System.out.println("|i|Последовательность|y_i(y_i,max)|");
        System.out.println("|:-|:-|:-|");
        char[] seq = new char[s.length()];
        Arrays.fill(seq, '?');
        int filled = 0;
        for (int i = 0; i < s.length(); i++) {
            if (!s.substring(0, i).contains(s.substring(i, i + 1))) {
                seq[i] = s.charAt(i);
                filled++;
            }
        }
        int currentIteration = 0;
        int currentLetter = 0;
        while (currentLetter < seq.length) {
            System.out.print(currentIteration++ + "|");

            int countQuestions = 0;
            int st = currentLetter + 1;
            while (st < s.length() && seq[st] == '?') {
                st++;
            }
            int idx = s.substring(st).indexOf(s.substring(currentLetter, currentLetter + 1));
            if (idx != -1) {
                idx += st;
                for (int i = st; i <= idx; i++)
                    if (seq[i] == '?')
                        countQuestions++;

                print(seq, new int[]{currentLetter, idx});
                System.out.print("|" + countQuestions);

                for (int i = idx + 1; i < seq.length; i++)
                    if (seq[i] == '?')
                        countQuestions++;
                System.out.println("(" + countQuestions + ")");

                seq[idx] = seq[currentLetter];
                filled++;
            } else {
                print(seq, new int[]{currentLetter});
                if (filled == seq.length) {
                    break;
                } else {
                    System.out.print("|0");
                }
                System.out.println("|");
            }

            int let = currentLetter + 1;
            while (let < seq.length && seq[let] == '?') {
                seq[let++] = seq[currentLetter];
                filled++;
            }
            currentLetter = let;
        }
        return 100;
    }

    public static void main(String[] args) {
        System.out.println("Кодирование расстояний");
        for (final String proverb : proverbs) {
            encode(proverb);
        }
    }
}
