package ru.ctddev.it;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.*;

public class AdaptiveArithmeticCoding extends Algorithm {

    @Override
    public List<Integer> encode(String inputString, boolean showDebugInfo) {

        if (showDebugInfo) {
            out.println("| Шаг | x | p(x) | q(x) | F | G | ");
            out.println("|:-|:-|:-|:-|:-|:-|");
        }

        String input = null;
        try {
            input = new String(inputString.getBytes("ascii"), "ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        List<Integer> code = new ArrayList<>();
        char[] inputCharArr = input.toCharArray();
        char[] inputCharArrOrig = inputString.toCharArray();

        double[] currentWeight = new double[ALPHABET];
        Arrays.fill(currentWeight, 1);
        int totalWeight = ALPHABET;

        BigDecimal[] q = new BigDecimal[ALPHABET];

        q[0] = BigDecimal.ZERO;
        for (int i = 1; i < ALPHABET; i++) {
            q[i] = q[i - 1].add(BigDecimal.valueOf(1.0 / ALPHABET));
        }
        int n = input.length();
        BigDecimal f = BigDecimal.ZERO;
        BigDecimal g = BigDecimal.ONE;

        double logsum = 0;
        int stepNumber = 0;
        for (int i = 0; i < n; i++) {
            f = f.add(q[inputCharArr[i]].multiply(g));
            g = g.multiply(BigDecimal.valueOf(currentWeight[inputCharArr[i]] / totalWeight));


            logsum += Utils.log2(currentWeight[inputCharArr[i]] / totalWeight);

            currentWeight[inputCharArr[i]]++;
            for (int j = 1; j < ALPHABET; j++) {
                q[j] = q[j - 1].add(BigDecimal.valueOf(currentWeight[j - 1] / totalWeight));
            }
            totalWeight++;
            stepNumber++;
            if (showDebugInfo) {
                System.out.print(" | " + stepNumber + " | ");
                System.out.print(inputCharArrOrig[i] + " | ");
                System.out.print(String.format("%.4f", currentWeight[inputCharArr[i]] / totalWeight) + " | ");
                System.out.print(new DecimalFormat("#0.###").format(q[inputCharArr[i]]) + " | ");
                System.out.print(new DecimalFormat("#0.#############################################").format(f) + " | ");
                System.out.println(new DecimalFormat("#0.#############################################").format(g) + " | ");
            }
        }
        int totalLength = (int) Math.ceil(-logsum) + 1;
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal result = f.add(g.divide(two));

        for (int i = 0; i < totalLength; i++) {
            result = result.multiply(two);
            if (result.compareTo(BigDecimal.ONE) >= 0) {
                code.add(1);
                result = result.subtract(BigDecimal.ONE);
            } else {
                code.add(0);
            }
        }


        if (showDebugInfo) {
            out.println("Итого = " + code.size() + " бит");
            out.println("Искомый код = " + Utils.convertIntListToString(code));
        }

        return code;
    }
}