// Generated from ParserGen.g4 by ANTLR 4.2.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParserGenLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, HEADERID=6, MEMBERSID=7, ATTRS=8, 
		TRANSSYM=9, LEXERID=10, PARSERID=11, ID=12, WS=13, LEXEM=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"':'", "'returns'", "'='", "';'", "'|'", "'@header'", "'@members'", "ATTRS", 
		"TRANSSYM", "LEXERID", "PARSERID", "ID", "WS", "LEXEM"
	};
	public static final String[] ruleNames = {
		"T__4", "T__3", "T__2", "T__1", "T__0", "HEADERID", "MEMBERSID", "ATTRS", 
		"TRANSSYM", "LEXERID", "PARSERID", "ID", "WS", "LEXEM", "NEWLINE"
	};


	public ParserGenLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ParserGen.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\20\u0082\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\6\tE\n\t\r\t\16"+
		"\tF\3\t\5\tJ\n\t\7\tL\n\t\f\t\16\tO\13\t\3\t\3\t\3\n\3\n\6\nU\n\n\r\n"+
		"\16\nV\3\n\5\nZ\n\n\7\n\\\n\n\f\n\16\n_\13\n\3\n\3\n\3\13\3\13\5\13e\n"+
		"\13\3\f\3\f\5\fi\n\f\3\r\6\rl\n\r\r\r\16\rm\3\16\3\16\5\16r\n\16\3\16"+
		"\3\16\3\17\3\17\6\17x\n\17\r\17\16\17y\3\17\3\17\3\20\5\20\177\n\20\3"+
		"\20\3\20\3y\2\21\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\2\3\2\b\4\2>>@@\4\2}}\177\177\3\2C\\\3\2c|\6\2\62;"+
		"C\\aac|\4\2\13\13\"\"\u008c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3!\3"+
		"\2\2\2\5#\3\2\2\2\7+\3\2\2\2\t-\3\2\2\2\13/\3\2\2\2\r\61\3\2\2\2\179\3"+
		"\2\2\2\21B\3\2\2\2\23R\3\2\2\2\25b\3\2\2\2\27f\3\2\2\2\31k\3\2\2\2\33"+
		"q\3\2\2\2\35u\3\2\2\2\37~\3\2\2\2!\"\7<\2\2\"\4\3\2\2\2#$\7t\2\2$%\7g"+
		"\2\2%&\7v\2\2&\'\7w\2\2\'(\7t\2\2()\7p\2\2)*\7u\2\2*\6\3\2\2\2+,\7?\2"+
		"\2,\b\3\2\2\2-.\7=\2\2.\n\3\2\2\2/\60\7~\2\2\60\f\3\2\2\2\61\62\7B\2\2"+
		"\62\63\7j\2\2\63\64\7g\2\2\64\65\7c\2\2\65\66\7f\2\2\66\67\7g\2\2\678"+
		"\7t\2\28\16\3\2\2\29:\7B\2\2:;\7o\2\2;<\7g\2\2<=\7o\2\2=>\7d\2\2>?\7g"+
		"\2\2?@\7t\2\2@A\7u\2\2A\20\3\2\2\2BM\7>\2\2CE\n\2\2\2DC\3\2\2\2EF\3\2"+
		"\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2HJ\5\21\t\2IH\3\2\2\2IJ\3\2\2\2JL\3"+
		"\2\2\2KD\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2\2OM\3\2\2\2PQ\7"+
		"@\2\2Q\22\3\2\2\2R]\7}\2\2SU\n\3\2\2TS\3\2\2\2UV\3\2\2\2VT\3\2\2\2VW\3"+
		"\2\2\2WY\3\2\2\2XZ\5\23\n\2YX\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[T\3\2\2\2\\"+
		"_\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2_]\3\2\2\2`a\7\177\2\2a\24\3\2"+
		"\2\2bd\t\4\2\2ce\5\31\r\2dc\3\2\2\2de\3\2\2\2e\26\3\2\2\2fh\t\5\2\2gi"+
		"\5\31\r\2hg\3\2\2\2hi\3\2\2\2i\30\3\2\2\2jl\t\6\2\2kj\3\2\2\2lm\3\2\2"+
		"\2mk\3\2\2\2mn\3\2\2\2n\32\3\2\2\2or\t\7\2\2pr\5\37\20\2qo\3\2\2\2qp\3"+
		"\2\2\2rs\3\2\2\2st\b\16\2\2t\34\3\2\2\2uw\7)\2\2vx\13\2\2\2wv\3\2\2\2"+
		"xy\3\2\2\2yz\3\2\2\2yw\3\2\2\2z{\3\2\2\2{|\7)\2\2|\36\3\2\2\2}\177\7\17"+
		"\2\2~}\3\2\2\2~\177\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081\7\f\2\2\u0081"+
		" \3\2\2\2\17\2FIMVY]dhmqy~\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}