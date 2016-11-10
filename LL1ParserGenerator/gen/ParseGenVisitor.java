// Generated from ParseGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ParseGenParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ParseGenVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ParseGenParser#node}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNode(@NotNull ParseGenParser.NodeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParseGenParser#ruleForLexer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleForLexer(@NotNull ParseGenParser.RuleForLexerContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParseGenParser#ruleForParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleForParser(@NotNull ParseGenParser.RuleForParserContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParseGenParser#production}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProduction(@NotNull ParseGenParser.ProductionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParseGenParser#rules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRules(@NotNull ParseGenParser.RulesContext ctx);
}