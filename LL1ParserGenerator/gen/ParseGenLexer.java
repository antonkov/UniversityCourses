// Generated from ParseGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParseGenLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__2=1, T__1=2, T__0=3, LEXERID=4, PARSERID=5, ID=6, WS=7, NEWLINE=8, 
		LEXEM=9;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"':'", "';'", "'|'", "LEXERID", "PARSERID", "ID", "WS", "NEWLINE", "LEXEM"
	};
	public static final String[] ruleNames = {
		"T__2", "T__1", "T__0", "LEXERID", "PARSERID", "ID", "WS", "NEWLINE", 
		"LEXEM"
	};


	public ParseGenLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ParseGen.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\13\67\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3"+
		"\2\3\3\3\3\3\4\3\4\3\5\3\5\5\5\36\n\5\3\6\3\6\5\6\"\n\6\3\7\6\7%\n\7\r"+
		"\7\16\7&\3\b\3\b\5\b+\n\b\3\b\3\b\3\t\5\t\60\n\t\3\t\3\t\3\n\3\n\3\n\3"+
		"\n\2\2\13\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\3\2\6\3\2C\\\3\2c|"+
		"\6\2\62;C\\aac|\4\2\13\13\"\";\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\3\25\3\2\2\2\5\27\3\2\2\2\7\31\3\2\2\2\t\33\3\2\2\2\13\37\3\2\2\2\r"+
		"$\3\2\2\2\17*\3\2\2\2\21/\3\2\2\2\23\63\3\2\2\2\25\26\7<\2\2\26\4\3\2"+
		"\2\2\27\30\7=\2\2\30\6\3\2\2\2\31\32\7~\2\2\32\b\3\2\2\2\33\35\t\2\2\2"+
		"\34\36\5\r\7\2\35\34\3\2\2\2\35\36\3\2\2\2\36\n\3\2\2\2\37!\t\3\2\2 \""+
		"\5\r\7\2! \3\2\2\2!\"\3\2\2\2\"\f\3\2\2\2#%\t\4\2\2$#\3\2\2\2%&\3\2\2"+
		"\2&$\3\2\2\2&\'\3\2\2\2\'\16\3\2\2\2(+\t\5\2\2)+\5\21\t\2*(\3\2\2\2*)"+
		"\3\2\2\2+,\3\2\2\2,-\b\b\2\2-\20\3\2\2\2.\60\7\17\2\2/.\3\2\2\2/\60\3"+
		"\2\2\2\60\61\3\2\2\2\61\62\7\f\2\2\62\22\3\2\2\2\63\64\7)\2\2\64\65\13"+
		"\2\2\2\65\66\7)\2\2\66\24\3\2\2\2\b\2\35!&*/\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}