package ru.ctddev.it;

import java.io.PrintStream;
import java.util.List;

/**
 * User: gtkachenko
 * Date: 21/12/14
 */
public abstract class Algorithm {
    protected static final int ALPHABET = 256;
    protected PrintStream out = System.out;

    public List<Integer> encode(String input) {
        return encode(input, false);
    }

    public abstract List<Integer> encode(String input, boolean showDebugInfo);
}
