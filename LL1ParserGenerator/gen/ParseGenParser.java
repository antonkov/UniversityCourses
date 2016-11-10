// Generated from ParseGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParseGenParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__2=1, T__1=2, T__0=3, LEXERID=4, PARSERID=5, ID=6, WS=7, NEWLINE=8, 
		LEXEM=9;
	public static final String[] tokenNames = {
		"<INVALID>", "':'", "';'", "'|'", "LEXERID", "PARSERID", "ID", "WS", "NEWLINE", 
		"LEXEM"
	};
	public static final int
		RULE_rules = 0, RULE_ruleForLexer = 1, RULE_ruleForParser = 2, RULE_production = 3, 
		RULE_node = 4;
	public static final String[] ruleNames = {
		"rules", "ruleForLexer", "ruleForParser", "production", "node"
	};

	@Override
	public String getGrammarFileName() { return "ParseGen.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ParseGenParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RulesContext extends ParserRuleContext {
		public List<RuleForParserContext> ruleForParser() {
			return getRuleContexts(RuleForParserContext.class);
		}
		public RuleForParserContext ruleForParser(int i) {
			return getRuleContext(RuleForParserContext.class,i);
		}
		public RuleForLexerContext ruleForLexer(int i) {
			return getRuleContext(RuleForLexerContext.class,i);
		}
		public List<RuleForLexerContext> ruleForLexer() {
			return getRuleContexts(RuleForLexerContext.class);
		}
		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rules; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParseGenVisitor ) return ((ParseGenVisitor<? extends T>)visitor).visitRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LEXERID || _la==PARSERID) {
				{
				{
				setState(12);
				switch (_input.LA(1)) {
				case LEXERID:
					{
					setState(10); ruleForLexer();
					}
					break;
				case PARSERID:
					{
					setState(11); ruleForParser();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(14); match(2);
				}
				}
				setState(20);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleForLexerContext extends ParserRuleContext {
		public ProductionContext production(int i) {
			return getRuleContext(ProductionContext.class,i);
		}
		public List<ProductionContext> production() {
			return getRuleContexts(ProductionContext.class);
		}
		public TerminalNode LEXERID() { return getToken(ParseGenParser.LEXERID, 0); }
		public RuleForLexerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleForLexer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParseGenVisitor ) return ((ParseGenVisitor<? extends T>)visitor).visitRuleForLexer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleForLexerContext ruleForLexer() throws RecognitionException {
		RuleForLexerContext _localctx = new RuleForLexerContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ruleForLexer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21); match(LEXERID);
			setState(22); match(1);
			setState(23); production();
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==3) {
				{
				{
				setState(24); match(3);
				setState(25); production();
				}
				}
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleForParserContext extends ParserRuleContext {
		public ProductionContext production(int i) {
			return getRuleContext(ProductionContext.class,i);
		}
		public List<ProductionContext> production() {
			return getRuleContexts(ProductionContext.class);
		}
		public TerminalNode PARSERID() { return getToken(ParseGenParser.PARSERID, 0); }
		public RuleForParserContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleForParser; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParseGenVisitor ) return ((ParseGenVisitor<? extends T>)visitor).visitRuleForParser(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleForParserContext ruleForParser() throws RecognitionException {
		RuleForParserContext _localctx = new RuleForParserContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ruleForParser);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31); match(PARSERID);
			setState(32); match(1);
			setState(33); production();
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==3) {
				{
				{
				setState(34); match(3);
				setState(35); production();
				}
				}
				setState(40);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProductionContext extends ParserRuleContext {
		public NodeContext node(int i) {
			return getRuleContext(NodeContext.class,i);
		}
		public List<NodeContext> node() {
			return getRuleContexts(NodeContext.class);
		}
		public ProductionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_production; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParseGenVisitor ) return ((ParseGenVisitor<? extends T>)visitor).visitProduction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProductionContext production() throws RecognitionException {
		ProductionContext _localctx = new ProductionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_production);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LEXERID) | (1L << PARSERID) | (1L << LEXEM))) != 0)) {
				{
				{
				setState(41); node();
				}
				}
				setState(46);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NodeContext extends ParserRuleContext {
		public TerminalNode LEXERID() { return getToken(ParseGenParser.LEXERID, 0); }
		public TerminalNode LEXEM() { return getToken(ParseGenParser.LEXEM, 0); }
		public TerminalNode PARSERID() { return getToken(ParseGenParser.PARSERID, 0); }
		public NodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_node; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParseGenVisitor ) return ((ParseGenVisitor<? extends T>)visitor).visitNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeContext node() throws RecognitionException {
		NodeContext _localctx = new NodeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_node);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LEXERID) | (1L << PARSERID) | (1L << LEXEM))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\64\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\5\2\17\n\2\3\2\3\2\7\2\23\n\2\f\2"+
		"\16\2\26\13\2\3\3\3\3\3\3\3\3\3\3\7\3\35\n\3\f\3\16\3 \13\3\3\4\3\4\3"+
		"\4\3\4\3\4\7\4\'\n\4\f\4\16\4*\13\4\3\5\7\5-\n\5\f\5\16\5\60\13\5\3\6"+
		"\3\6\3\6\2\2\7\2\4\6\b\n\2\3\4\2\6\7\13\13\63\2\24\3\2\2\2\4\27\3\2\2"+
		"\2\6!\3\2\2\2\b.\3\2\2\2\n\61\3\2\2\2\f\17\5\4\3\2\r\17\5\6\4\2\16\f\3"+
		"\2\2\2\16\r\3\2\2\2\17\20\3\2\2\2\20\21\7\4\2\2\21\23\3\2\2\2\22\16\3"+
		"\2\2\2\23\26\3\2\2\2\24\22\3\2\2\2\24\25\3\2\2\2\25\3\3\2\2\2\26\24\3"+
		"\2\2\2\27\30\7\6\2\2\30\31\7\3\2\2\31\36\5\b\5\2\32\33\7\5\2\2\33\35\5"+
		"\b\5\2\34\32\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37\5\3\2"+
		"\2\2 \36\3\2\2\2!\"\7\7\2\2\"#\7\3\2\2#(\5\b\5\2$%\7\5\2\2%\'\5\b\5\2"+
		"&$\3\2\2\2\'*\3\2\2\2(&\3\2\2\2()\3\2\2\2)\7\3\2\2\2*(\3\2\2\2+-\5\n\6"+
		"\2,+\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\t\3\2\2\2\60.\3\2\2\2\61"+
		"\62\t\2\2\2\62\13\3\2\2\2\7\16\24\36(.";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}