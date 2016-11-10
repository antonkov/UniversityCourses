package ru.ctddev.it;

import java.math.BigInteger;
import java.util.*;

public class NumericCoding extends Algorithm {
    @Override
    public List<Integer> encode(String input, boolean showDebugInfo) {

        List<Integer> code = new ArrayList<>();

        char[] inputCharArr = input.toCharArray();

        List<Integer> lexNumberOfComposition = Utils.convertStringToIntList(getLexNumberOfComposition(inputCharArr).toString(2));
        List<Integer> lexNumberOfSequence = Utils.convertStringToIntList(getLexNumberOfSequence(inputCharArr).toString(2));
        for (int i = 0; i < getNumberOfCombinationsWithRepetitions(ALPHABET, input.length()).subtract(BigInteger.ONE).toString(2).length() - lexNumberOfComposition.size(); i++) {
            code.add(0);
        }
        code.addAll(lexNumberOfComposition);
        int bitsComposition = code.size();
        for (int i = 0; i < getNumberOfSequences(inputCharArr).subtract(BigInteger.ONE).toString(2).length() - lexNumberOfSequence.size(); i++) {
            code.add(0);
        }
        code.addAll(lexNumberOfSequence);
        if (showDebugInfo) {
            out.println("string = " + input);
            out.println("total number of compositions = " + getNumberOfCombinationsWithRepetitions(ALPHABET, input.length()));
            out.println("our composition has number = " + getLexNumberOfComposition(inputCharArr));
            out.println("bits for composition = " + bitsComposition);

            out.println("total number of sequences = " + getNumberOfSequences(inputCharArr));
            out.println("our sequence has number = " + getLexNumberOfSequence(inputCharArr));
            out.println("bits for sequence = " + (code.size() - bitsComposition));
            out.println("total amount of bits = " + code.size());

            out.println("encoded string = " + Utils.convertIntListToString(code));
        }

        return code;
    }

    private BigInteger getNumberOfCombinationsWithRepetitions(int n, int k) {
        return getNumberOfCombinations(n + k - 1, n - 1);
    }

    private BigInteger getNumberOfCombinations(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k)));
    }

    private BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }


    private BigInteger getNumberOfSequences(int[] cnt) {
        BigInteger result = BigInteger.ONE;
        int total = 0;
        for (int cur : cnt) {
            total += cur;
        }
        for (int i = 1; i <= total; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        for (int curCnt : cnt) {
            for (int i = 1; i <= curCnt; i++) {
                result = result.divide(BigInteger.valueOf(i));
            }
        }
        return result;
    }

    private BigInteger getNumberOfSequences(char[] input) {
        return getNumberOfSequences(calcCnt(input));
    }

    private BigInteger getLexNumberOfComposition(char[] input) {
        int[] cnt = calcCnt(input);
        BigInteger result = BigInteger.ZERO;
        int n = input.length;
        int sum = 0;
        for (int i = 0; i < ALPHABET; i++) {
            for (int j = 0; j < cnt[i]; j++) {
                if (i + 1 != ALPHABET) {
                    result = result.add(getNumberOfCombinationsWithRepetitions(ALPHABET - i - 1, n - sum - j));
                }
            }
            sum += cnt[i];
        }
        return result;
    }

    private BigInteger getLexNumberOfSequence(char[] input) {
        int[] cnt = calcCnt(input);
        Set<Character> characters = new TreeSet<>();
        for (char c : input) {
            if (!characters.contains(c)) {
                characters.add(c);
            }
        }
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < input.length; i++) {
            Iterator<Character> it = characters.iterator();
            while (it.hasNext()) {
                char cur = it.next();
                if (cur == input[i]) {
                    break;
                }
                if (cnt[cur] != 0) {
                    cnt[cur]--;
                    result = result.add(getNumberOfSequences(cnt));
                    cnt[cur]++;
                }
            }
            cnt[input[i]]--;
        }

        return result;
    }

    private int[] calcCnt(char[] input) {
        int[] cnt = new int[ALPHABET];
        for (char c : input) {
            cnt[c]++;
        }
        return cnt;
    }

}