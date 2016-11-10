import org.antlr.v4.runtime.misc.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by antonkov on 6/8/14.
 */
public class RulesExtractor extends ParseGenBaseVisitor<String> {
    TreeMap<Terminal, String> terminals;
    TreeSet<NotTerminal> notTerminals;
    TreeMap<NotTerminal, ArrayList<Production>> rules;
    NotTerminal start;

    public RulesExtractor(TreeMap<Terminal, String> terminals,
                          TreeSet<NotTerminal> notTerminals,
                          TreeMap<NotTerminal, ArrayList<Production>> rules,
                          NotTerminal start) {
        this.terminals = terminals;
        this.notTerminals = notTerminals;
        this.rules = rules;
        this.start = start;
    }

    @Override
    public String visitRules(@NotNull ParseGenParser.RulesContext ctx) {
        if (ctx.ruleForParser().size() == 0) {
            throw new RuntimeException("no rules for parser");
        }
        ParseGenParser.RuleForParserContext rule = ctx.ruleForParser(0);
        start.name = rule.PARSERID().getText();
        return super.visitRules(ctx);
    }

    @Override
    public String visitRuleForParser(@NotNull ParseGenParser.RuleForParserContext ctx) {
        NotTerminal from = new NotTerminal(ctx.PARSERID().getText());
        notTerminals.add(from);
        ArrayList<Production> prods = rules.get(from);
        if (prods == null)
            prods = new ArrayList<>();
        for (ParseGenParser.ProductionContext pctx : ctx.production()) {
            Production prod = new Production();
            for (ParseGenParser.NodeContext node : pctx.node()) {
                if (node.PARSERID() != null) {
                    prod.addNode(new NotTerminal(node.PARSERID().getText()));
                } else if (node.LEXERID() != null) {
                    prod.addNode(new Terminal((node.LEXERID().getText())));
                } else if (node.LEXEM() != null) {
                    String name = "TERMINAL_";
                    String lexem = node.LEXEM().getText();
                    for (char c : lexem.toCharArray())
                        name += Integer.toHexString((int)c).toUpperCase();
                    Terminal term = new Terminal(name);
                    terminals.put(term, lexem);
                    prod.addNode(term);
                }
            }
            prods.add(prod);
        }
        rules.put(from, prods);
        return "";
    }

    @Override
    public String visitRuleForLexer(@NotNull ParseGenParser.RuleForLexerContext ctx) {
        return super.visitRuleForLexer(ctx);
    }
}
