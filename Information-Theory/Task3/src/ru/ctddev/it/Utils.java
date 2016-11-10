package ru.ctddev.it;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Borys Minaiev on 09.12.2014.
 */
public class Utils {

    public static String charTo01String(int value) {
        return convertIntListToString(charTo01List(value));
    }

    public static List<Integer> charTo01List(int value) {
        List<Integer> res = new ArrayList<Integer>();
        for (int bit = 7; bit >= 0; bit--) {
            res.add((((1 << bit) & value) != 0) ? 1 : 0);
        }
        return res;
    }

    /**
     * e.g. 5 -> [1, 0, 1] + bound (fill remaining positions with zeros)
     */
    public static List<Integer> convertIntToBinaryList(int value, int bound) {
        List<Integer> result = new ArrayList<Integer>();
        while (value != 0) {
            result.add(value % 2);
            value /= 2;
        }
        int cntToAdd = getBitsCount(bound) - result.size();
        for (int i = 0; i < cntToAdd; i++) {
            result.add(0);
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * e.g. 5 -> [1, 0, 1]
     */
    public static List<Integer> convertIntToBinaryList(int value) {
        return convertIntToBinaryList(value, value);
    }

    /**
     * e.g. 5 -> "101"
     */
    public static String convertIntToBinaryString(int value, int bound) {
        return convertIntListToString(convertIntToBinaryList(value, bound));
    }

    /**
     * e.g. 5 -> 3
     */
    public static int getBitsCount(int value) {
        int cnt = 0;
        while (value != 0) {
            cnt++;
            value /= 2;
        }
        return cnt;
    }

    public static int getBitsCount(BigInteger value) {
        int cnt = 0;
        BigInteger two = BigInteger.valueOf(2);
        while (!value.equals(BigInteger.ZERO)) {
            cnt++;
            value = value.divide(two);
        }
        return cnt;
    }

    /**
     * e.g. [1, 1, 0, 0, 1] -> "11001"
     */
    public static String convertIntListToString(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : list) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    /**
     * e.g. "11001" -> [1, 1, 0, 0, 1]
     */
    public static List<Integer> convertStringToIntList(String input) {
        List<Integer> result = new ArrayList<>();
        for (char c : input.toCharArray()) {
            result.add(Integer.parseInt("" + c));
        }
        return result;
    }

    public static List<Integer> getUnaryCode(int value) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < value - 1; i++) {
            result.add(1);
        }
        result.add(0);
        return result;
    }

    public static List<Integer> getMonotoneCode(int value) {
        List<Integer> result = new ArrayList<>();
        result.addAll(getUnaryCode(getBitsCount(value)));
        List<Integer> valueBits = convertIntToBinaryList(value);
        valueBits.remove(0);
        result.addAll(valueBits);
        return result;
    }

    public static double log2(double value) {
        return Math.log(value) / Math.log(2.0);
    }

}
