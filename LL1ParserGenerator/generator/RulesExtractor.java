import org.antlr.v4.runtime.misc.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by antonkov on 6/8/14.
 */
public class RulesExtractor extends ParserGenBaseVisitor<String> {
    TreeMap<Terminal, String> terminals;
    TreeSet<NotTerminal> notTerminals;
    TreeMap<NotTerminal, ArrayList<Production>> rules;
    NotTerminal start;
    TreeMap<NotTerminal, String> synthesized;
    TreeMap<NotTerminal, String> inherited;
    String header, members;

    public RulesExtractor(TreeMap<Terminal, String> terminals,
                          TreeSet<NotTerminal> notTerminals,
                          TreeMap<NotTerminal, ArrayList<Production>> rules,
                          NotTerminal start) {
        this.terminals = terminals;
        this.notTerminals = notTerminals;
        this.rules = rules;
        this.start = start;
        synthesized = new TreeMap<>();
        inherited = new TreeMap<>();
    }

    private String chopLR(String s) {
        return s.substring(1, s.length() - 1);
    }

    @Override
    public String visitHeader(@NotNull ParserGenParser.HeaderContext ctx) {
        header = chopLR(ctx.TRANSSYM().getText());
        return super.visitHeader(ctx);
    }

    @Override
    public String visitMembers(@NotNull ParserGenParser.MembersContext ctx) {
        members = chopLR(ctx.TRANSSYM().getText());
        return super.visitMembers(ctx);
    }

    @Override
    public String visitRules(@NotNull ParserGenParser.RulesContext ctx) {
        if (ctx.ruleForParser().size() == 0) {
            throw new RuntimeException("no rules for parser");
        }
        ParserGenParser.RuleForParserContext rule = ctx.ruleForParser(0);
        start.name = rule.PARSERID().getText();
        return super.visitRules(ctx);
    }

    @Override
    public String visitRuleForParser(@NotNull ParserGenParser.RuleForParserContext ctx) {
        NotTerminal from = new NotTerminal(ctx.PARSERID().getText());
        notTerminals.add(from);
        ArrayList<Production> prods = rules.get(from);
        if (ctx.synthesized() != null)
            synthesized.put(from, chopLR(ctx.synthesized().ATTRS().getText()) + ";");
        if (ctx.inherited() != null)
            inherited.put(from, chopLR(ctx.inherited().ATTRS().getText()) + ";");
        if (prods == null)
            prods = new ArrayList<>();
        for (ParserGenParser.ProductionContext pctx : ctx.production()) {
            Production prod = new Production();
            if (pctx.TRANSSYM() != null) {
                prod.transSymbol = pctx.TRANSSYM().getText();
            }
            for (ParserGenParser.NodeContext nctx : pctx.node()) {
                Node newNode = null;
                if (nctx.PARSERID() != null) {
                    newNode = new NotTerminal(nctx.PARSERID().getText());
                } else if (nctx.LEXERID() != null) {
                    newNode = new Terminal((nctx.LEXERID().getText()));
                } else if (nctx.LEXEM() != null) {
                    String name = "TERMINAL_";
                    String lexem = nctx.LEXEM().getText();
                    for (char c : lexem.toCharArray())
                        name += Integer.toHexString((int)c).toUpperCase();
                    Terminal term = new Terminal(name);
                    terminals.put(term, chopLR(lexem));
                    newNode = term;
                }
                if (newNode instanceof NotTerminal) {
                    NotTerminal cur = new NotTerminal(newNode.name);
                    if (nctx.label() != null)
                        cur.label = nctx.label().getText();
                    if (nctx.ATTRS() != null)
                        cur.arguments = "{" + chopLR(nctx.ATTRS().getText()) + ";}";
                    newNode = cur;
                }
                if (nctx.TRANSSYM() != null) {
                    newNode.transSymbol = nctx.TRANSSYM().getText();
                }
                prod.addNode(newNode);
            }
            prods.add(prod);
        }
        rules.put(from, prods);
        return "";
    }

    @Override
    public String visitRuleForLexer(@NotNull ParserGenParser.RuleForLexerContext ctx) {
        return super.visitRuleForLexer(ctx);
    }
}
