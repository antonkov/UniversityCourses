import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by antonkov on 6/7/14.
 */
public class ParserGenerator {
    PrintWriter out;
    String grammarName;

    TreeMap<NotTerminal, ArrayList<Production>> rules;
    TreeMap<String, Terminal> mapToTerminal;

    TreeMap<Terminal, String> terminals;
    TreeSet<NotTerminal> notTerminals;
    TreeMap<PairNodes, Production> parsingTable;

    TreeSet<Node> nodes;
    NotTerminal start;

    TreeMap<Node, TreeSet<Terminal>> first, follow;

    RulesExtractor extractor;
    TreeMap<NotTerminal, String> synthesized, inherited;

    static Terminal epsilon = new Terminal("");

    void generateContexts() {
        out.println("class Context implements DSTreeNode {");
        out.println("    protected ArrayList<Context> children;");
        out.println("    String name;\n");
        out.println("    String text;\n");
        out.println("    Context(String name) {\n" +
                    "        children = new ArrayList<Context>();\n" +
                    "        this.name = name;\n" +
                    "        text = name;\n" +
                    "    }\n\n" +
                    "    void addChildren(Context ctx) {\n" +
                    "        children.add(ctx);\n" +
                    "    }\n" +
                    "    @Override\n" +
                    "    public DSTreeNode[] DSgetChildren() {\n" +
                    "        if (children != null)\n" +
                    "            return children.toArray(new DSTreeNode[0]);\n" +
                    "        else\n" +
                    "            return new DSTreeNode[0];\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public Object DSgetValue() {\n" +
                    "        return name;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public Color DSgetColor() {\n" +
                    "        return Color.red;\n" +
                    "    }\n}\n\n" +
                    "    class NotTerminalContext extends Context {\n" +
                    "        NotTerminalContext(String name) {\n" +
                    "            super(name);\n" +
                    "            text = \"\";\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        void addChildren(Context ctx) {\n" +
                    "            children.add(ctx);\n" +
                    "            text += ctx.text;\n" +
                    "        }\n" +
                    "        @Override\n" +
                    "        public Color DSgetColor() {\n" +
                    "            return Color.blue;\n" +
                    "        }" +
                    "    }");
        out.println();

        synthesized = extractor.synthesized;
        inherited = extractor.inherited;
        for (NotTerminal notTerm : notTerminals) {
            out.println("class " + capitalize(notTerm.name) + "Context extends NotTerminalContext {");
            if (synthesized.containsKey(notTerm))
                out.println(synthesized.get(notTerm));
            if (inherited.containsKey(notTerm))
                out.println(inherited.get(notTerm));
            TreeSet<String> vars = new TreeSet<>();
            for (Production prod : rules.get(notTerm)) {
                for (Node node : prod.to) {
                    if (node instanceof NotTerminal) {
                        NotTerminal cur = (NotTerminal)node;
                        if (cur.label != null)
                            vars.add(capitalize(cur.name) + "Context " + cur.label);
                        vars.add(capitalize(cur.name) + "Context " + cur.name);
                    }
                }
            }
            for (String var : vars) {
                out.println(var + ";");
            }
            out.println("public " + capitalize(notTerm.name) + "Context() {\n" +
                        "   super(\"" + notTerm.name + "\");\n" +
                        "}");
            out.println("}");
            out.println("");
        }
    }

    void templateNotTerminal(NotTerminal a) {
        String contextVar = a.name + "Context";
        String contextName = capitalize(a.name) + "Context";
        out.println("   public " + contextName + " " + a.name + "() throws ParseException {\n" +
                    "       " + contextName + " " + contextVar + " = new " + contextName + "();\n" +
                    "       return " + a.name + "(" + contextVar + ");\n" +
                    "}\n");
        out.println("   public " + contextName + " " + a.name + "(" + contextName +
                                                                " " + contextVar + ") throws ParseException {");
        out.println("       switch (lex.curToken()) {");
        for (Terminal term : terminals.keySet()) {
            out.print("           case " + term.name.toUpperCase() + " : ");
            out.println("// " + terminals.get(term));
            Production prod = parsingTable.get(new PairNodes(a, term));
            if (prod == null) {
                out.println("               throw new ParseException(\"not expected symbol\", 0);");
                continue;
            }
            int countTrees = prod.to.size();
            if (prod.transSymbol != null) {
                String res = prod.transSymbol.replaceAll("[$]", contextVar + ".");
                out.println(res);
            }
            for (Node node : prod.to) {
                if (node instanceof  NotTerminal) {
                    String args = ((NotTerminal) node).arguments;
                    String nodeContextVar = contextVar + "." + node.name;
                    String nodeContextName = capitalize(node.name) + "Context";
                    if (args != null) {
                        args = args.replaceAll("[$]", contextVar + ".");
                        args = args.replace("#", nodeContextVar + ".");
                        out.println(nodeContextVar + " = new " + nodeContextName + "();");
                        out.println(args);
                    }
                    out.print("               " + contextVar + "." + node.name + " = " + node.name + "(");
                    if (args != null)
                        out.print(nodeContextVar);
                    out.println(");");
                    if (((NotTerminal) node).label != null) {
                        out.println("               " + contextVar + "." + ((NotTerminal) node).label
                                                      + " = " + contextVar + "." + node.name + ";");
                    }
                    out.println("               " + contextVar + ".addChildren("
                                                  + contextVar + "." + node.name + ");");
                } else {
                    out.println("               match(GToken." + node.name + ");");
                    out.println("               " + contextVar + ".addChildren(new Context(\""
                                                  + terminals.get(node) + "\"));");
                    out.println("               lex.nextToken();");
                }
                if (node.transSymbol != null) {
                    String res = node.transSymbol.replaceAll("[$]", contextVar + ".");
                    out.println(res);
                }
            }
            out.println("               return " + contextVar + ";");
        }
        out.print("           default : ");
        out.println("               throw new ParseException(\"not expected symbol\", 0);");

        out.println("      }");
        out.println("   }");
        out.println("");
    }

    private String capitalize(String name) {
        return "" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private void generate(String grammarName) throws IOException {
        this.grammarName = grammarName;

        File grammarFile = new File("grammars/" + grammarName + ".g");
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(grammarFile));
        ParserGenLexer lexer = new ParserGenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ParserGenParser parser = new ParserGenParser(tokens);
        ParseTree tree = parser.program();

        terminals = new TreeMap<>();
        notTerminals = new TreeSet<>();
        rules = new TreeMap<>();

        mapToTerminal = new TreeMap<>();

        start = new NotTerminal("");
        extractor = new RulesExtractor(terminals, notTerminals, rules, start);
        extractor.visit(tree);

        terminals.put(new Terminal("END"), "");
        nodes = new TreeSet<>();
        for (Node node : terminals.keySet())
            nodes.add(node);
        for (Node node : notTerminals)
            nodes.add(node);
        for (Terminal term : terminals.keySet())
            mapToTerminal.put(terminals.get(term), term);

        buildTokens();
        buildLexer();
        buildParser();
    }

    void generateFunctionsForTerminals() throws IOException {
        first = new TreeMap<>();
        follow = new TreeMap<>();
        buildFirst();
        buildFollow();
        parsingTable = new TreeMap<>();
        buildParsingTable();

        for (NotTerminal notTerm : notTerminals)
            templateNotTerminal(notTerm);
    }

    private void buildTokens() throws IOException {
        String className = "GToken";
        out = new PrintWriter("parser/" + className + ".java");

        out.println("public enum " + className + " { ");
        boolean first = true;
        for (Terminal term : terminals.keySet()) {
            out.print((first ? "    " : ", ") + term.name);
            first = false;
        }
        out.println(";");
        out.println("}");
        out.close();
    }

    private void buildParser() throws IOException {
        String className = "GParser";
        out = new PrintWriter("parser/" + className + ".java");

        out.println(extractor.header);

        out.println("import java.io.*;");
        out.println("import java.util.*;");
        out.println("import java.text.ParseException;");
        out.println("import java.awt.*;");
        out.println("import org.StructureGraphic.v1.DSTreeNode;");
        out.println("");
        out.println("public class " + className + " {");
        out.println("   " + "GLexer lex;");
        out.println(extractor.members);
        out.println("   public static void main(String args[]) {");
        out.println("       System.out.println(\"Go\");");
        out.println("   }");
        out.println("");
        out.println("   void match(GToken token) throws ParseException {\n" +
                    "       if (token != lex.curToken())\n" +
                    "           throw new ParseException(\"not match\", 0);\n" +
                    "   }\n" +
                    "");

        generateContexts();
        generateFunctionsForTerminals();

        String contextName = capitalize(start.name) + "Context";
        out.println("    " + contextName + " parse(InputStream is) throws ParseException {");
        out.println("        lex = new GLexer(is);\n" +
                    "        lex.nextToken();\n" +
                    "        " + contextName + " res = " + start.name + "();\n" +
                    "        if (lex.curToken() != GToken.END)\n" +
                    "            throw new ParseException(\"expected end of expression, but found \" + lex.curToken(), lex.curPos());\n" +
                    "        return res;");
        out.println("   }");
        out.println("}");

        out.close();
    }

    private void buildLexer() throws IOException {
        String className = "GLexer";
        out = new PrintWriter("parser/" + className + ".java");

        out.println("import java.io.*;");
        out.println("import java.util.*;");
        out.println("import java.text.ParseException;");
        out.println("");
        out.println("public class " + className + " {");
        out.println("    InputStream is;\n" +
                    "    int curChar;\n" +
                    "    int curPos;\n" +
                    "    GToken curToken;\n" +
                    "    String details;\n" +
                    "");
        out.println("    public " + className + "(InputStream is) throws ParseException {\n" +
                    "        this.is = is;\n" +
                    "        curPos = 0;\n" +
                    "        nextChar();\n" +
                    "    }\n" +
                    "");
        out.println("    private boolean isBlank(int c) {\n" +
                    "        return c == ' ';\n" +
                    "    }\n" +
                    "");
        out.println("    private void nextChar() throws ParseException {\n" +
                    "        curPos++;\n" +
                    "        curChar = -1;\n" +
                    "        try {\n" +
                    "            curChar = is.read();\n" +
                    "        } catch (IOException e) {\n" +
                    "            throw new ParseException(e.getMessage(), curPos);\n" +
                    "        }\n" +
                    "    }");
        out.println("    public GToken curToken() {\n" +
                    "        return curToken;\n" +
                    "    }\n" +
                    "\n" +
                    "    public int curPos() {\n" +
                    "        return curPos;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String details() {\n" +
                    "        return details;\n" +
                    "    }");
        out.println("    public void nextToken() throws ParseException {\n" +
                    "        while (isBlank(curChar)) {\n" +
                    "            nextChar();\n" +
                    "        }\n");
        boolean first = true;
        for (Terminal term : terminals.keySet()) {
            String s = terminals.get(term);
            if (s == "")
                s = "-1";
            else
                s = "'" + s + "'";
            out.println((first ? "        " : "else ") + "if (curChar == " + s + ") {");
            out.println("           nextChar();");
            out.println("           curToken = GToken." + term.name + ";");
            out.print("        } ");
            first = false;
        }
        out.println("else {\n" +
                    "            throw new ParseException(\"Illegal character \" + (char) curChar, curPos);\n" +
                    "        }\n" +
                    "    }");
        out.println("    public static void main(String args[]) {");
        out.println("        System.out.println(\"Go\");");
        out.println("    }");
        out.println("");


        out.println("}");

        out.close();
    }

    private void buildFirst() {
        for (Node node : nodes) {
            first.put(node, new TreeSet<Terminal>());
            if (node instanceof Terminal)
                first.get(node).add((Terminal)node);
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (NotTerminal notTerm : rules.keySet()) {
                TreeSet<Terminal> curFirst = first.get(notTerm);
                for (Production prod : rules.get(notTerm)) {
                    boolean beforeEps = true;
                    for (Node node : prod.to) {
                        for (Terminal term : first.get(node))
                            if (!term.equals(epsilon) && !curFirst.contains(term)) {
                                changed = true;
                                curFirst.add(term);
                            }
                        if (!first.get(node).contains(epsilon)) {
                            beforeEps = false;
                            break;
                        }
                    }
                    if (beforeEps && !curFirst.contains(epsilon)) {
                        changed = true;
                        curFirst.add(epsilon);
                    }
                }
            }
        }
    }

    private void buildFollow() {
        for (Node node : nodes)
            follow.put(node, new TreeSet<Terminal>());
        follow.get(start).add(new Terminal("END"));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (NotTerminal notTerm : notTerminals) {
                for (Production prod : rules.get(notTerm)) {
                    TreeSet<Terminal> curFirst = new TreeSet<>(follow.get(notTerm));
                    for (int it = prod.to.size() - 1; it >= 0; it--) {
                        Node node = prod.to.get(it);
                        if (node instanceof NotTerminal) {
                            TreeSet<Terminal> curFollow = follow.get(node);
                            for (Terminal term : curFirst) {
                                if (!curFollow.contains(term)) {
                                    changed = true;
                                    curFollow.add(term);
                                }
                            }
                        }
                        if (!first.get(node).contains(epsilon))
                            curFirst.clear();
                        for (Terminal term : first.get(node))
                            if (!term.equals(epsilon))
                                curFirst.add(term);
                    }
                }
            }
        }
    }

    TreeSet<Terminal> calcFirst(ArrayList<Node> nodes) {
        TreeSet<Terminal> result = new TreeSet<>();
        boolean beforeEps = true;
        for (Node node : nodes) {
            TreeSet<Terminal> curFirst = first.get(node);
            for (Terminal term : curFirst)
                if (!term.equals(epsilon))
                    result.add(term);
            if (!curFirst.contains(epsilon)) {
                beforeEps = false;
                break;
            }
        }
        if (beforeEps)
            result.add(epsilon);
        return result;
    }

    private void buildParsingTable() {
        for (NotTerminal notTerm : notTerminals) {
            TreeSet<Terminal> curFollow = follow.get(notTerm);
            for (Production prod : rules.get(notTerm)) {
                TreeSet<Terminal> curFirst = calcFirst(prod.to);
                if (curFirst.contains(epsilon))
                    curFirst.addAll(curFollow);
                for (Terminal term : curFirst) {
                    parsingTable.put(new PairNodes(notTerm, term), prod);
                }
            }
        }
    }

    public ParserGenerator(String grammarName) {
        try {
            generate(grammarName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
