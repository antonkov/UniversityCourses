package com.dbobrov.num_methods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Log implements AutoCloseable {
    private final PrintWriter out;
    public Log(String fileName) throws FileNotFoundException {
        out = new PrintWriter(fileName);
    }

    public void d(String s) {
        out.println("[DEBUG] " + s);
    }

    public void e(String s) {
        out.println("[ERROR] " + s);
    }

    public void e(String s, Throwable tr) {
        out.println("[ERROR] " + s);
        out.println("\t" + tr);
        printStackTrace(tr);
        for (Throwable cause = tr.getCause(); cause != null; cause = cause.getCause()) {
            out.println("\tCaused by: " + cause);
            printStackTrace(cause);
        }
    }

    private void printStackTrace(Throwable tr) {
        for (StackTraceElement el : tr.getStackTrace()) {
            out.println("\tat " + el);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
