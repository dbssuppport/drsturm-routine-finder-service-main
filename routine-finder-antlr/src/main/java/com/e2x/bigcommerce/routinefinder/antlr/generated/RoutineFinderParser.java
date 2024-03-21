// Generated from RoutineFinder.g4 by ANTLR 4.9.1

package com.e2x.bigcommerce.routinefinder.antlr.generated;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RoutineFinderParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OR=1, AND=2, COMMA=3, LB=4, RB=5, NOT=6, IS=7, IN=8, QUESTION=9, WS=10, 
		VALUE=11, ANY=12;
	public static final int
		RULE_start = 0, RULE_and_or = 1, RULE_expr = 2, RULE_in_operand = 3, RULE_is_operand = 4, 
		RULE_match_rule = 5, RULE_values = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "and_or", "expr", "in_operand", "is_operand", "match_rule", 
			"values"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'or'", "'and'", "','", "'('", "')'", "'not'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OR", "AND", "COMMA", "LB", "RB", "NOT", "IS", "IN", "QUESTION", 
			"WS", "VALUE", "ANY"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "RoutineFinder.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RoutineFinderParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StartContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			expr();
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

	public static class And_orContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(RoutineFinderParser.AND, 0); }
		public TerminalNode OR() { return getToken(RoutineFinderParser.OR, 0); }
		public And_orContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterAnd_or(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitAnd_or(this);
		}
	}

	public final And_orContext and_or() throws RecognitionException {
		And_orContext _localctx = new And_orContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_and_or);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			_la = _input.LA(1);
			if ( !(_la==OR || _la==AND) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	public static class ExprContext extends ParserRuleContext {
		public Match_ruleContext match_rule() {
			return getRuleContext(Match_ruleContext.class,0);
		}
		public And_orContext and_or() {
			return getRuleContext(And_orContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expr);
		try {
			setState(23);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(18);
				match_rule();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(19);
				match_rule();
				setState(20);
				and_or();
				setState(21);
				expr();
				}
				break;
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

	public static class In_operandContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(RoutineFinderParser.IN, 0); }
		public TerminalNode NOT() { return getToken(RoutineFinderParser.NOT, 0); }
		public In_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterIn_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitIn_operand(this);
		}
	}

	public final In_operandContext in_operand() throws RecognitionException {
		In_operandContext _localctx = new In_operandContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_in_operand);
		try {
			setState(28);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(25);
				match(IN);
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(26);
				match(NOT);
				setState(27);
				match(IN);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class Is_operandContext extends ParserRuleContext {
		public TerminalNode IS() { return getToken(RoutineFinderParser.IS, 0); }
		public TerminalNode NOT() { return getToken(RoutineFinderParser.NOT, 0); }
		public Is_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_is_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterIs_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitIs_operand(this);
		}
	}

	public final Is_operandContext is_operand() throws RecognitionException {
		Is_operandContext _localctx = new Is_operandContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_is_operand);
		try {
			setState(33);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				match(IS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(31);
				match(IS);
				setState(32);
				match(NOT);
				}
				break;
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

	public static class Match_ruleContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(RoutineFinderParser.QUESTION, 0); }
		public Is_operandContext is_operand() {
			return getRuleContext(Is_operandContext.class,0);
		}
		public TerminalNode VALUE() { return getToken(RoutineFinderParser.VALUE, 0); }
		public In_operandContext in_operand() {
			return getRuleContext(In_operandContext.class,0);
		}
		public TerminalNode LB() { return getToken(RoutineFinderParser.LB, 0); }
		public ValuesContext values() {
			return getRuleContext(ValuesContext.class,0);
		}
		public TerminalNode RB() { return getToken(RoutineFinderParser.RB, 0); }
		public Match_ruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_match_rule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterMatch_rule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitMatch_rule(this);
		}
	}

	public final Match_ruleContext match_rule() throws RecognitionException {
		Match_ruleContext _localctx = new Match_ruleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_match_rule);
		try {
			setState(45);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				match(QUESTION);
				setState(36);
				is_operand();
				setState(37);
				match(VALUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(39);
				match(QUESTION);
				setState(40);
				in_operand();
				setState(41);
				match(LB);
				setState(42);
				values();
				setState(43);
				match(RB);
				}
				break;
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

	public static class ValuesContext extends ParserRuleContext {
		public TerminalNode VALUE() { return getToken(RoutineFinderParser.VALUE, 0); }
		public TerminalNode COMMA() { return getToken(RoutineFinderParser.COMMA, 0); }
		public ValuesContext values() {
			return getRuleContext(ValuesContext.class,0);
		}
		public ValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_values; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).enterValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoutineFinderListener ) ((RoutineFinderListener)listener).exitValues(this);
		}
	}

	public final ValuesContext values() throws RecognitionException {
		ValuesContext _localctx = new ValuesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_values);
		try {
			setState(51);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(47);
				match(VALUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				match(VALUE);
				setState(49);
				match(COMMA);
				setState(50);
				values();
				}
				break;
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\168\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\5\4\32\n\4\3\5\3\5\3\5\5\5\37\n\5\3\6\3\6\3\6\5\6$\n\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\60\n\7\3\b\3\b\3\b\3\b\5\b\66\n"+
		"\b\3\b\2\2\t\2\4\6\b\n\f\16\2\3\3\2\3\4\2\65\2\20\3\2\2\2\4\22\3\2\2\2"+
		"\6\31\3\2\2\2\b\36\3\2\2\2\n#\3\2\2\2\f/\3\2\2\2\16\65\3\2\2\2\20\21\5"+
		"\6\4\2\21\3\3\2\2\2\22\23\t\2\2\2\23\5\3\2\2\2\24\32\5\f\7\2\25\26\5\f"+
		"\7\2\26\27\5\4\3\2\27\30\5\6\4\2\30\32\3\2\2\2\31\24\3\2\2\2\31\25\3\2"+
		"\2\2\32\7\3\2\2\2\33\37\7\n\2\2\34\35\7\b\2\2\35\37\7\n\2\2\36\33\3\2"+
		"\2\2\36\34\3\2\2\2\37\t\3\2\2\2 $\7\t\2\2!\"\7\t\2\2\"$\7\b\2\2# \3\2"+
		"\2\2#!\3\2\2\2$\13\3\2\2\2%&\7\13\2\2&\'\5\n\6\2\'(\7\r\2\2(\60\3\2\2"+
		"\2)*\7\13\2\2*+\5\b\5\2+,\7\6\2\2,-\5\16\b\2-.\7\7\2\2.\60\3\2\2\2/%\3"+
		"\2\2\2/)\3\2\2\2\60\r\3\2\2\2\61\66\7\r\2\2\62\63\7\r\2\2\63\64\7\5\2"+
		"\2\64\66\5\16\b\2\65\61\3\2\2\2\65\62\3\2\2\2\66\17\3\2\2\2\7\31\36#/"+
		"\65";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}