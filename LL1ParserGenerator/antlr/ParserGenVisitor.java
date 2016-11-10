// Generated from ParserGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ParserGenParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ParserGenVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ParserGenParser#node}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNode(@NotNull ParserGenParser.NodeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#ruleForLexer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleForLexer(@NotNull ParserGenParser.RuleForLexerContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#ruleForParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleForParser(@NotNull ParserGenParser.RuleForParserContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(@NotNull ParserGenParser.ProgramContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(@NotNull ParserGenParser.LabelContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#inherited}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInherited(@NotNull ParserGenParser.InheritedContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#synthesized}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynthesized(@NotNull ParserGenParser.SynthesizedContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#production}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProduction(@NotNull ParserGenParser.ProductionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#rules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRules(@NotNull ParserGenParser.RulesContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#members}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembers(@NotNull ParserGenParser.MembersContext ctx);

	/**
	 * Visit a parse tree produced by {@link ParserGenParser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(@NotNull ParserGenParser.HeaderContext ctx);
}