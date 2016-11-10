// Generated from ParserGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParserGenParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, HEADERID=6, MEMBERSID=7, ATTRS=8, 
		TRANSSYM=9, LEXERID=10, PARSERID=11, ID=12, WS=13, LEXEM=14;
	public static final String[] tokenNames = {
		"<INVALID>", "':'", "'returns'", "'='", "';'", "'|'", "'@header'", "'@members'", 
		"ATTRS", "TRANSSYM", "LEXERID", "PARSERID", "ID", "WS", "LEXEM"
	};
	public static final int
		RULE_program = 0, RULE_header = 1, RULE_members = 2, RULE_rules = 3, RULE_ruleForLexer = 4, 
		RULE_ruleForParser = 5, RULE_inherited = 6, RULE_synthesized = 7, RULE_production = 8, 
		RULE_node = 9, RULE_label = 10;
	public static final String[] ruleNames = {
		"program", "header", "members", "rules", "ruleForLexer", "ruleForParser", 
		"inherited", "synthesized", "production", "node", "label"
	};

	@Override
	public String getGrammarFileName() { return "ParserGen.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ParserGenParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public MembersContext members() {
			return getRuleContext(MembersContext.class,0);
		}
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_la = _input.LA(1);
			if (_la==HEADERID) {
				{
				setState(22); header();
				}
			}

			setState(26);
			_la = _input.LA(1);
			if (_la==MEMBERSID) {
				{
				setState(25); members();
				}
			}

			setState(28); rules();
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

	public static class HeaderContext extends ParserRuleContext {
		public TerminalNode HEADERID() { return getToken(ParserGenParser.HEADERID, 0); }
		public TerminalNode TRANSSYM() { return getToken(ParserGenParser.TRANSSYM, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30); match(HEADERID);
			setState(31); match(TRANSSYM);
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

	public static class MembersContext extends ParserRuleContext {
		public TerminalNode MEMBERSID() { return getToken(ParserGenParser.MEMBERSID, 0); }
		public TerminalNode TRANSSYM() { return getToken(ParserGenParser.TRANSSYM, 0); }
		public MembersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_members; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitMembers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembersContext members() throws RecognitionException {
		MembersContext _localctx = new MembersContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_members);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33); match(MEMBERSID);
			setState(34); match(TRANSSYM);
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
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LEXERID || _la==PARSERID) {
				{
				{
				setState(38);
				switch (_input.LA(1)) {
				case LEXERID:
					{
					setState(36); ruleForLexer();
					}
					break;
				case PARSERID:
					{
					setState(37); ruleForParser();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(40); match(4);
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

	public static class RuleForLexerContext extends ParserRuleContext {
		public ProductionContext production(int i) {
			return getRuleContext(ProductionContext.class,i);
		}
		public List<ProductionContext> production() {
			return getRuleContexts(ProductionContext.class);
		}
		public TerminalNode LEXERID() { return getToken(ParserGenParser.LEXERID, 0); }
		public RuleForLexerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleForLexer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitRuleForLexer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleForLexerContext ruleForLexer() throws RecognitionException {
		RuleForLexerContext _localctx = new RuleForLexerContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ruleForLexer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47); match(LEXERID);
			setState(48); match(1);
			setState(49); production();
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==5) {
				{
				{
				setState(50); match(5);
				setState(51); production();
				}
				}
				setState(56);
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
		public SynthesizedContext synthesized() {
			return getRuleContext(SynthesizedContext.class,0);
		}
		public ProductionContext production(int i) {
			return getRuleContext(ProductionContext.class,i);
		}
		public InheritedContext inherited() {
			return getRuleContext(InheritedContext.class,0);
		}
		public List<ProductionContext> production() {
			return getRuleContexts(ProductionContext.class);
		}
		public TerminalNode PARSERID() { return getToken(ParserGenParser.PARSERID, 0); }
		public RuleForParserContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleForParser; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitRuleForParser(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleForParserContext ruleForParser() throws RecognitionException {
		RuleForParserContext _localctx = new RuleForParserContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ruleForParser);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57); match(PARSERID);
			setState(59);
			_la = _input.LA(1);
			if (_la==ATTRS) {
				{
				setState(58); inherited();
				}
			}

			setState(62);
			_la = _input.LA(1);
			if (_la==2) {
				{
				setState(61); synthesized();
				}
			}

			setState(64); match(1);
			setState(65); production();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==5) {
				{
				{
				setState(66); match(5);
				setState(67); production();
				}
				}
				setState(72);
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

	public static class InheritedContext extends ParserRuleContext {
		public TerminalNode ATTRS() { return getToken(ParserGenParser.ATTRS, 0); }
		public InheritedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inherited; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitInherited(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InheritedContext inherited() throws RecognitionException {
		InheritedContext _localctx = new InheritedContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_inherited);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73); match(ATTRS);
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

	public static class SynthesizedContext extends ParserRuleContext {
		public TerminalNode ATTRS() { return getToken(ParserGenParser.ATTRS, 0); }
		public SynthesizedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_synthesized; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitSynthesized(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SynthesizedContext synthesized() throws RecognitionException {
		SynthesizedContext _localctx = new SynthesizedContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_synthesized);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75); match(2);
			setState(76); match(ATTRS);
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
		public TerminalNode TRANSSYM() { return getToken(ParserGenParser.TRANSSYM, 0); }
		public ProductionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_production; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitProduction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProductionContext production() throws RecognitionException {
		ProductionContext _localctx = new ProductionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_production);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_la = _input.LA(1);
			if (_la==TRANSSYM) {
				{
				setState(78); match(TRANSSYM);
				}
			}

			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LEXERID) | (1L << PARSERID) | (1L << LEXEM))) != 0)) {
				{
				{
				setState(81); node();
				}
				}
				setState(86);
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
		public TerminalNode ATTRS() { return getToken(ParserGenParser.ATTRS, 0); }
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public TerminalNode TRANSSYM() { return getToken(ParserGenParser.TRANSSYM, 0); }
		public TerminalNode LEXERID() { return getToken(ParserGenParser.LEXERID, 0); }
		public TerminalNode LEXEM() { return getToken(ParserGenParser.LEXEM, 0); }
		public TerminalNode PARSERID() { return getToken(ParserGenParser.PARSERID, 0); }
		public NodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_node; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeContext node() throws RecognitionException {
		NodeContext _localctx = new NodeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_node);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(87); label();
				setState(88); match(3);
				}
				break;
			}
			setState(92);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LEXERID) | (1L << PARSERID) | (1L << LEXEM))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(94);
			_la = _input.LA(1);
			if (_la==ATTRS) {
				{
				setState(93); match(ATTRS);
				}
			}

			setState(97);
			_la = _input.LA(1);
			if (_la==TRANSSYM) {
				{
				setState(96); match(TRANSSYM);
				}
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

	public static class LabelContext extends ParserRuleContext {
		public TerminalNode PARSERID() { return getToken(ParserGenParser.PARSERID, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ParserGenVisitor ) return ((ParserGenVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99); match(PARSERID);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\20h\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\5\2\32\n\2\3\2\5\2\35\n\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\5\3\5\5\5)\n\5\3\5\3\5\7\5-\n\5\f\5\16\5\60\13\5\3\6\3\6\3\6\3\6\3\6"+
		"\7\6\67\n\6\f\6\16\6:\13\6\3\7\3\7\5\7>\n\7\3\7\5\7A\n\7\3\7\3\7\3\7\3"+
		"\7\7\7G\n\7\f\7\16\7J\13\7\3\b\3\b\3\t\3\t\3\t\3\n\5\nR\n\n\3\n\7\nU\n"+
		"\n\f\n\16\nX\13\n\3\13\3\13\3\13\5\13]\n\13\3\13\3\13\5\13a\n\13\3\13"+
		"\5\13d\n\13\3\f\3\f\3\f\2\2\r\2\4\6\b\n\f\16\20\22\24\26\2\3\4\2\f\r\20"+
		"\20i\2\31\3\2\2\2\4 \3\2\2\2\6#\3\2\2\2\b.\3\2\2\2\n\61\3\2\2\2\f;\3\2"+
		"\2\2\16K\3\2\2\2\20M\3\2\2\2\22Q\3\2\2\2\24\\\3\2\2\2\26e\3\2\2\2\30\32"+
		"\5\4\3\2\31\30\3\2\2\2\31\32\3\2\2\2\32\34\3\2\2\2\33\35\5\6\4\2\34\33"+
		"\3\2\2\2\34\35\3\2\2\2\35\36\3\2\2\2\36\37\5\b\5\2\37\3\3\2\2\2 !\7\b"+
		"\2\2!\"\7\13\2\2\"\5\3\2\2\2#$\7\t\2\2$%\7\13\2\2%\7\3\2\2\2&)\5\n\6\2"+
		"\')\5\f\7\2(&\3\2\2\2(\'\3\2\2\2)*\3\2\2\2*+\7\6\2\2+-\3\2\2\2,(\3\2\2"+
		"\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\t\3\2\2\2\60.\3\2\2\2\61\62\7\f\2"+
		"\2\62\63\7\3\2\2\638\5\22\n\2\64\65\7\7\2\2\65\67\5\22\n\2\66\64\3\2\2"+
		"\2\67:\3\2\2\28\66\3\2\2\289\3\2\2\29\13\3\2\2\2:8\3\2\2\2;=\7\r\2\2<"+
		">\5\16\b\2=<\3\2\2\2=>\3\2\2\2>@\3\2\2\2?A\5\20\t\2@?\3\2\2\2@A\3\2\2"+
		"\2AB\3\2\2\2BC\7\3\2\2CH\5\22\n\2DE\7\7\2\2EG\5\22\n\2FD\3\2\2\2GJ\3\2"+
		"\2\2HF\3\2\2\2HI\3\2\2\2I\r\3\2\2\2JH\3\2\2\2KL\7\n\2\2L\17\3\2\2\2MN"+
		"\7\4\2\2NO\7\n\2\2O\21\3\2\2\2PR\7\13\2\2QP\3\2\2\2QR\3\2\2\2RV\3\2\2"+
		"\2SU\5\24\13\2TS\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2W\23\3\2\2\2XV\3"+
		"\2\2\2YZ\5\26\f\2Z[\7\5\2\2[]\3\2\2\2\\Y\3\2\2\2\\]\3\2\2\2]^\3\2\2\2"+
		"^`\t\2\2\2_a\7\n\2\2`_\3\2\2\2`a\3\2\2\2ac\3\2\2\2bd\7\13\2\2cb\3\2\2"+
		"\2cd\3\2\2\2d\25\3\2\2\2ef\7\r\2\2f\27\3\2\2\2\17\31\34(.8=@HQV\\`c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}