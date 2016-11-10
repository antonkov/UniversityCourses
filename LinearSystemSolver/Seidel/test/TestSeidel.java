import gtkachenko.CustomVector;
import gtkachenko.LinearSystem;
import gtkachenko.Seidel;
import gtkachenko.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: gtkachenko
 */

@RunWith(value = Parameterized.class)
public class TestSeidel {
    private final static String INPUT_FILE_NAME = Seidel.INPUT_FILE_NAME;
    private final static String OUTPUT_FILE_NAME = Seidel.OUTPUT_FILE_NAME;
    private final static String LOG_FILE_NAME = Seidel.LOG_FILE_NAME;

    private static Writer outLog;
    private static Writer out;

    private LinearSystem linearSystem;
    private double epsilon;
    private int maxIteration;
    private CustomVector initApproximation;


    public TestSeidel(LinearSystem linearSystem, double epsilon, int maxIteration, CustomVector initApproximation) {
        this.linearSystem = linearSystem;
        this.epsilon = epsilon;
        this.maxIteration = maxIteration;
        this.initApproximation = initApproximation;
    }

    @Parameterized.Parameters
    public static Collection testCases() {
        IOUtils.FastScanner in = new IOUtils.FastScanner(new File(INPUT_FILE_NAME));
        List<Object[]> objects = new ArrayList<Object[]>();
        while (true) {
            Object[] current = IOUtils.nextTest(in);
            if (current == null) {
                break;
            }
            objects.add(current);
        }

        return objects;
    }

    @BeforeClass
    public static void emptyLog() {
        IOUtils.emptyFile(new File(OUTPUT_FILE_NAME));
        IOUtils.emptyFile(new File(LOG_FILE_NAME));
    }

    @Before
    public void setUp() throws Exception {
        out = new FileWriter(new File(OUTPUT_FILE_NAME), true);
        outLog = new FileWriter(new File(LOG_FILE_NAME), true);
    }

    @After
    public void tearDown() throws Exception {
        out.close();
        outLog.close();
    }

    @Test
    public void currentTest() throws Exception {
        out.write(Seidel.solveLinearSystem(linearSystem, epsilon, maxIteration, initApproximation, outLog) + "\n");
    }

}
