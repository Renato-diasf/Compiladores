// Generated from UaiLang.g4 by ANTLR 4.13.1

package br.com.compiladores.uailang;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class UaiLangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		UAI=1, CABOU=2, TREM=3, ESCUITA=4, MOSTRA=5, SE=6, ENTAO=7, SENAO=8, FIMSE=9, 
		ENQUANTO=10, FAZ=11, FIMENQUANTO=12, PRA=13, DE=14, ATE=15, FIMPRA=16, 
		INTEIRO=17, REAL=18, TEXTO=19, LOGICO=20, VERDADE=21, MENTIRA=22, E=23, 
		OU=24, NAO=25, IGUAL_IGUAL=26, DIFERENTE=27, MENOR_IGUAL=28, MAIOR_IGUAL=29, 
		IGUAL=30, MENOR=31, MAIOR=32, MAIS=33, MENOS=34, VEZES=35, DIVIDIDO=36, 
		RESTO=37, ABRE_PAR=38, FECHA_PAR=39, DOIS_PONTOS=40, VIRGULA=41, PONTO=42, 
		NUM_REAL=43, NUM_INT=44, TEXTO_LITERAL=45, IDENT=46, COMENTARIO=47, WS=48;
	public static final int
		RULE_programa = 0, RULE_comando = 1, RULE_declaracao = 2, RULE_atribuicao = 3, 
		RULE_leitura = 4, RULE_escrita = 5, RULE_condicional = 6, RULE_repeticaoEnquanto = 7, 
		RULE_repeticaoPara = 8, RULE_tipo = 9, RULE_expressao = 10, RULE_ouExpr = 11, 
		RULE_eExpr = 12, RULE_igualdadeExpr = 13, RULE_relExpr = 14, RULE_adExpr = 15, 
		RULE_multExpr = 16, RULE_unExpr = 17, RULE_primario = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "comando", "declaracao", "atribuicao", "leitura", "escrita", 
			"condicional", "repeticaoEnquanto", "repeticaoPara", "tipo", "expressao", 
			"ouExpr", "eExpr", "igualdadeExpr", "relExpr", "adExpr", "multExpr", 
			"unExpr", "primario"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'uai'", "'cabou'", "'trem'", "'escuita'", "'mostra'", "'se'", 
			"'entao'", "'senao'", "'fimse'", "'int\\u00E9'", "'faz'", "'prontim'", 
			"'pra'", "'de'", "'ate'", "'fimpra'", "'inteiro'", "'real'", "'texto'", 
			"'logico'", "'verdade'", "'mentira'", "'e'", "'ou'", "'nao'", "'=='", 
			"'!='", "'<='", "'>='", "'='", "'<'", "'>'", "'+'", "'-'", "'*'", "'/'", 
			"'%'", "'('", "')'", "':'", "','", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "UAI", "CABOU", "TREM", "ESCUITA", "MOSTRA", "SE", "ENTAO", "SENAO", 
			"FIMSE", "ENQUANTO", "FAZ", "FIMENQUANTO", "PRA", "DE", "ATE", "FIMPRA", 
			"INTEIRO", "REAL", "TEXTO", "LOGICO", "VERDADE", "MENTIRA", "E", "OU", 
			"NAO", "IGUAL_IGUAL", "DIFERENTE", "MENOR_IGUAL", "MAIOR_IGUAL", "IGUAL", 
			"MENOR", "MAIOR", "MAIS", "MENOS", "VEZES", "DIVIDIDO", "RESTO", "ABRE_PAR", 
			"FECHA_PAR", "DOIS_PONTOS", "VIRGULA", "PONTO", "NUM_REAL", "NUM_INT", 
			"TEXTO_LITERAL", "IDENT", "COMENTARIO", "WS"
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
	public String getGrammarFileName() { return "UaiLang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public UaiLangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramaContext extends ParserRuleContext {
		public TerminalNode UAI() { return getToken(UaiLangParser.UAI, 0); }
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public TerminalNode CABOU() { return getToken(UaiLangParser.CABOU, 0); }
		public TerminalNode EOF() { return getToken(UaiLangParser.EOF, 0); }
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterPrograma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitPrograma(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitPrograma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(UAI);
			setState(39);
			match(IDENT);
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744187000L) != 0)) {
				{
				{
				setState(40);
				comando();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
			match(CABOU);
			setState(47);
			match(EOF);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ComandoContext extends ParserRuleContext {
		public DeclaracaoContext declaracao() {
			return getRuleContext(DeclaracaoContext.class,0);
		}
		public TerminalNode PONTO() { return getToken(UaiLangParser.PONTO, 0); }
		public AtribuicaoContext atribuicao() {
			return getRuleContext(AtribuicaoContext.class,0);
		}
		public LeituraContext leitura() {
			return getRuleContext(LeituraContext.class,0);
		}
		public EscritaContext escrita() {
			return getRuleContext(EscritaContext.class,0);
		}
		public CondicionalContext condicional() {
			return getRuleContext(CondicionalContext.class,0);
		}
		public RepeticaoEnquantoContext repeticaoEnquanto() {
			return getRuleContext(RepeticaoEnquantoContext.class,0);
		}
		public RepeticaoParaContext repeticaoPara() {
			return getRuleContext(RepeticaoParaContext.class,0);
		}
		public ComandoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comando; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterComando(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitComando(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitComando(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComandoContext comando() throws RecognitionException {
		ComandoContext _localctx = new ComandoContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_comando);
		try {
			setState(64);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TREM:
				enterOuterAlt(_localctx, 1);
				{
				setState(49);
				declaracao();
				setState(50);
				match(PONTO);
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(52);
				atribuicao();
				setState(53);
				match(PONTO);
				}
				break;
			case ESCUITA:
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				leitura();
				setState(56);
				match(PONTO);
				}
				break;
			case MOSTRA:
				enterOuterAlt(_localctx, 4);
				{
				setState(58);
				escrita();
				setState(59);
				match(PONTO);
				}
				break;
			case SE:
				enterOuterAlt(_localctx, 5);
				{
				setState(61);
				condicional();
				}
				break;
			case ENQUANTO:
				enterOuterAlt(_localctx, 6);
				{
				setState(62);
				repeticaoEnquanto();
				}
				break;
			case PRA:
				enterOuterAlt(_localctx, 7);
				{
				setState(63);
				repeticaoPara();
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

	@SuppressWarnings("CheckReturnValue")
	public static class DeclaracaoContext extends ParserRuleContext {
		public TerminalNode TREM() { return getToken(UaiLangParser.TREM, 0); }
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public TerminalNode DOIS_PONTOS() { return getToken(UaiLangParser.DOIS_PONTOS, 0); }
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode IGUAL() { return getToken(UaiLangParser.IGUAL, 0); }
		public ExpressaoContext expressao() {
			return getRuleContext(ExpressaoContext.class,0);
		}
		public DeclaracaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterDeclaracao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitDeclaracao(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitDeclaracao(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracaoContext declaracao() throws RecognitionException {
		DeclaracaoContext _localctx = new DeclaracaoContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declaracao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(TREM);
			setState(67);
			match(IDENT);
			setState(68);
			match(DOIS_PONTOS);
			setState(69);
			tipo();
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IGUAL) {
				{
				setState(70);
				match(IGUAL);
				setState(71);
				expressao();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AtribuicaoContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public TerminalNode IGUAL() { return getToken(UaiLangParser.IGUAL, 0); }
		public ExpressaoContext expressao() {
			return getRuleContext(ExpressaoContext.class,0);
		}
		public AtribuicaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atribuicao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterAtribuicao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitAtribuicao(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitAtribuicao(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtribuicaoContext atribuicao() throws RecognitionException {
		AtribuicaoContext _localctx = new AtribuicaoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atribuicao);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(IDENT);
			setState(75);
			match(IGUAL);
			setState(76);
			expressao();
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

	@SuppressWarnings("CheckReturnValue")
	public static class LeituraContext extends ParserRuleContext {
		public TerminalNode ESCUITA() { return getToken(UaiLangParser.ESCUITA, 0); }
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public LeituraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leitura; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterLeitura(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitLeitura(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitLeitura(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeituraContext leitura() throws RecognitionException {
		LeituraContext _localctx = new LeituraContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_leitura);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			match(ESCUITA);
			setState(79);
			match(IDENT);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EscritaContext extends ParserRuleContext {
		public TerminalNode MOSTRA() { return getToken(UaiLangParser.MOSTRA, 0); }
		public List<ExpressaoContext> expressao() {
			return getRuleContexts(ExpressaoContext.class);
		}
		public ExpressaoContext expressao(int i) {
			return getRuleContext(ExpressaoContext.class,i);
		}
		public List<TerminalNode> VIRGULA() { return getTokens(UaiLangParser.VIRGULA); }
		public TerminalNode VIRGULA(int i) {
			return getToken(UaiLangParser.VIRGULA, i);
		}
		public EscritaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_escrita; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterEscrita(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitEscrita(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitEscrita(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EscritaContext escrita() throws RecognitionException {
		EscritaContext _localctx = new EscritaContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_escrita);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(MOSTRA);
			setState(82);
			expressao();
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VIRGULA) {
				{
				{
				setState(83);
				match(VIRGULA);
				setState(84);
				expressao();
				}
				}
				setState(89);
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

	@SuppressWarnings("CheckReturnValue")
	public static class CondicionalContext extends ParserRuleContext {
		public TerminalNode SE() { return getToken(UaiLangParser.SE, 0); }
		public ExpressaoContext expressao() {
			return getRuleContext(ExpressaoContext.class,0);
		}
		public TerminalNode ENTAO() { return getToken(UaiLangParser.ENTAO, 0); }
		public TerminalNode FIMSE() { return getToken(UaiLangParser.FIMSE, 0); }
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public TerminalNode SENAO() { return getToken(UaiLangParser.SENAO, 0); }
		public TerminalNode PONTO() { return getToken(UaiLangParser.PONTO, 0); }
		public CondicionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condicional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterCondicional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitCondicional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitCondicional(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondicionalContext condicional() throws RecognitionException {
		CondicionalContext _localctx = new CondicionalContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_condicional);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(SE);
			setState(91);
			expressao();
			setState(92);
			match(ENTAO);
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744187000L) != 0)) {
				{
				{
				setState(93);
				comando();
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SENAO) {
				{
				setState(99);
				match(SENAO);
				setState(103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744187000L) != 0)) {
					{
					{
					setState(100);
					comando();
					}
					}
					setState(105);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(108);
			match(FIMSE);
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PONTO) {
				{
				setState(109);
				match(PONTO);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RepeticaoEnquantoContext extends ParserRuleContext {
		public TerminalNode ENQUANTO() { return getToken(UaiLangParser.ENQUANTO, 0); }
		public ExpressaoContext expressao() {
			return getRuleContext(ExpressaoContext.class,0);
		}
		public TerminalNode FAZ() { return getToken(UaiLangParser.FAZ, 0); }
		public TerminalNode FIMENQUANTO() { return getToken(UaiLangParser.FIMENQUANTO, 0); }
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public TerminalNode PONTO() { return getToken(UaiLangParser.PONTO, 0); }
		public RepeticaoEnquantoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeticaoEnquanto; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterRepeticaoEnquanto(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitRepeticaoEnquanto(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitRepeticaoEnquanto(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RepeticaoEnquantoContext repeticaoEnquanto() throws RecognitionException {
		RepeticaoEnquantoContext _localctx = new RepeticaoEnquantoContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_repeticaoEnquanto);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(ENQUANTO);
			setState(113);
			expressao();
			setState(114);
			match(FAZ);
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744187000L) != 0)) {
				{
				{
				setState(115);
				comando();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(121);
			match(FIMENQUANTO);
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PONTO) {
				{
				setState(122);
				match(PONTO);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RepeticaoParaContext extends ParserRuleContext {
		public TerminalNode PRA() { return getToken(UaiLangParser.PRA, 0); }
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public TerminalNode DE() { return getToken(UaiLangParser.DE, 0); }
		public List<ExpressaoContext> expressao() {
			return getRuleContexts(ExpressaoContext.class);
		}
		public ExpressaoContext expressao(int i) {
			return getRuleContext(ExpressaoContext.class,i);
		}
		public TerminalNode ATE() { return getToken(UaiLangParser.ATE, 0); }
		public TerminalNode FAZ() { return getToken(UaiLangParser.FAZ, 0); }
		public TerminalNode FIMPRA() { return getToken(UaiLangParser.FIMPRA, 0); }
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public TerminalNode PONTO() { return getToken(UaiLangParser.PONTO, 0); }
		public RepeticaoParaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeticaoPara; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterRepeticaoPara(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitRepeticaoPara(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitRepeticaoPara(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RepeticaoParaContext repeticaoPara() throws RecognitionException {
		RepeticaoParaContext _localctx = new RepeticaoParaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_repeticaoPara);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(PRA);
			setState(126);
			match(IDENT);
			setState(127);
			match(DE);
			setState(128);
			expressao();
			setState(129);
			match(ATE);
			setState(130);
			expressao();
			setState(131);
			match(FAZ);
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744187000L) != 0)) {
				{
				{
				setState(132);
				comando();
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138);
			match(FIMPRA);
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PONTO) {
				{
				setState(139);
				match(PONTO);
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

	@SuppressWarnings("CheckReturnValue")
	public static class TipoContext extends ParserRuleContext {
		public TerminalNode INTEIRO() { return getToken(UaiLangParser.INTEIRO, 0); }
		public TerminalNode REAL() { return getToken(UaiLangParser.REAL, 0); }
		public TerminalNode TEXTO() { return getToken(UaiLangParser.TEXTO, 0); }
		public TerminalNode LOGICO() { return getToken(UaiLangParser.LOGICO, 0); }
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterTipo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitTipo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitTipo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_tipo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1966080L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressaoContext extends ParserRuleContext {
		public OuExprContext ouExpr() {
			return getRuleContext(OuExprContext.class,0);
		}
		public ExpressaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterExpressao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitExpressao(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitExpressao(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressaoContext expressao() throws RecognitionException {
		ExpressaoContext _localctx = new ExpressaoContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_expressao);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			ouExpr();
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

	@SuppressWarnings("CheckReturnValue")
	public static class OuExprContext extends ParserRuleContext {
		public List<EExprContext> eExpr() {
			return getRuleContexts(EExprContext.class);
		}
		public EExprContext eExpr(int i) {
			return getRuleContext(EExprContext.class,i);
		}
		public List<TerminalNode> OU() { return getTokens(UaiLangParser.OU); }
		public TerminalNode OU(int i) {
			return getToken(UaiLangParser.OU, i);
		}
		public OuExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ouExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterOuExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitOuExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitOuExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OuExprContext ouExpr() throws RecognitionException {
		OuExprContext _localctx = new OuExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_ouExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			eExpr();
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OU) {
				{
				{
				setState(147);
				match(OU);
				setState(148);
				eExpr();
				}
				}
				setState(153);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EExprContext extends ParserRuleContext {
		public List<IgualdadeExprContext> igualdadeExpr() {
			return getRuleContexts(IgualdadeExprContext.class);
		}
		public IgualdadeExprContext igualdadeExpr(int i) {
			return getRuleContext(IgualdadeExprContext.class,i);
		}
		public List<TerminalNode> E() { return getTokens(UaiLangParser.E); }
		public TerminalNode E(int i) {
			return getToken(UaiLangParser.E, i);
		}
		public EExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterEExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitEExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitEExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EExprContext eExpr() throws RecognitionException {
		EExprContext _localctx = new EExprContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_eExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			igualdadeExpr();
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==E) {
				{
				{
				setState(155);
				match(E);
				setState(156);
				igualdadeExpr();
				}
				}
				setState(161);
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

	@SuppressWarnings("CheckReturnValue")
	public static class IgualdadeExprContext extends ParserRuleContext {
		public List<RelExprContext> relExpr() {
			return getRuleContexts(RelExprContext.class);
		}
		public RelExprContext relExpr(int i) {
			return getRuleContext(RelExprContext.class,i);
		}
		public List<TerminalNode> IGUAL_IGUAL() { return getTokens(UaiLangParser.IGUAL_IGUAL); }
		public TerminalNode IGUAL_IGUAL(int i) {
			return getToken(UaiLangParser.IGUAL_IGUAL, i);
		}
		public List<TerminalNode> DIFERENTE() { return getTokens(UaiLangParser.DIFERENTE); }
		public TerminalNode DIFERENTE(int i) {
			return getToken(UaiLangParser.DIFERENTE, i);
		}
		public IgualdadeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_igualdadeExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterIgualdadeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitIgualdadeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitIgualdadeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IgualdadeExprContext igualdadeExpr() throws RecognitionException {
		IgualdadeExprContext _localctx = new IgualdadeExprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_igualdadeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			relExpr();
			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IGUAL_IGUAL || _la==DIFERENTE) {
				{
				{
				setState(163);
				_la = _input.LA(1);
				if ( !(_la==IGUAL_IGUAL || _la==DIFERENTE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(164);
				relExpr();
				}
				}
				setState(169);
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

	@SuppressWarnings("CheckReturnValue")
	public static class RelExprContext extends ParserRuleContext {
		public List<AdExprContext> adExpr() {
			return getRuleContexts(AdExprContext.class);
		}
		public AdExprContext adExpr(int i) {
			return getRuleContext(AdExprContext.class,i);
		}
		public List<TerminalNode> MENOR() { return getTokens(UaiLangParser.MENOR); }
		public TerminalNode MENOR(int i) {
			return getToken(UaiLangParser.MENOR, i);
		}
		public List<TerminalNode> MENOR_IGUAL() { return getTokens(UaiLangParser.MENOR_IGUAL); }
		public TerminalNode MENOR_IGUAL(int i) {
			return getToken(UaiLangParser.MENOR_IGUAL, i);
		}
		public List<TerminalNode> MAIOR() { return getTokens(UaiLangParser.MAIOR); }
		public TerminalNode MAIOR(int i) {
			return getToken(UaiLangParser.MAIOR, i);
		}
		public List<TerminalNode> MAIOR_IGUAL() { return getTokens(UaiLangParser.MAIOR_IGUAL); }
		public TerminalNode MAIOR_IGUAL(int i) {
			return getToken(UaiLangParser.MAIOR_IGUAL, i);
		}
		public RelExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterRelExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitRelExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitRelExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelExprContext relExpr() throws RecognitionException {
		RelExprContext _localctx = new RelExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_relExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			adExpr();
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7247757312L) != 0)) {
				{
				{
				setState(171);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7247757312L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(172);
				adExpr();
				}
				}
				setState(177);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AdExprContext extends ParserRuleContext {
		public List<MultExprContext> multExpr() {
			return getRuleContexts(MultExprContext.class);
		}
		public MultExprContext multExpr(int i) {
			return getRuleContext(MultExprContext.class,i);
		}
		public List<TerminalNode> MAIS() { return getTokens(UaiLangParser.MAIS); }
		public TerminalNode MAIS(int i) {
			return getToken(UaiLangParser.MAIS, i);
		}
		public List<TerminalNode> MENOS() { return getTokens(UaiLangParser.MENOS); }
		public TerminalNode MENOS(int i) {
			return getToken(UaiLangParser.MENOS, i);
		}
		public AdExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterAdExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitAdExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitAdExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdExprContext adExpr() throws RecognitionException {
		AdExprContext _localctx = new AdExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_adExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			multExpr();
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MAIS || _la==MENOS) {
				{
				{
				setState(179);
				_la = _input.LA(1);
				if ( !(_la==MAIS || _la==MENOS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(180);
				multExpr();
				}
				}
				setState(185);
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

	@SuppressWarnings("CheckReturnValue")
	public static class MultExprContext extends ParserRuleContext {
		public List<UnExprContext> unExpr() {
			return getRuleContexts(UnExprContext.class);
		}
		public UnExprContext unExpr(int i) {
			return getRuleContext(UnExprContext.class,i);
		}
		public List<TerminalNode> VEZES() { return getTokens(UaiLangParser.VEZES); }
		public TerminalNode VEZES(int i) {
			return getToken(UaiLangParser.VEZES, i);
		}
		public List<TerminalNode> DIVIDIDO() { return getTokens(UaiLangParser.DIVIDIDO); }
		public TerminalNode DIVIDIDO(int i) {
			return getToken(UaiLangParser.DIVIDIDO, i);
		}
		public List<TerminalNode> RESTO() { return getTokens(UaiLangParser.RESTO); }
		public TerminalNode RESTO(int i) {
			return getToken(UaiLangParser.RESTO, i);
		}
		public MultExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterMultExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitMultExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitMultExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultExprContext multExpr() throws RecognitionException {
		MultExprContext _localctx = new MultExprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_multExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			unExpr();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 240518168576L) != 0)) {
				{
				{
				setState(187);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 240518168576L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(188);
				unExpr();
				}
				}
				setState(193);
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

	@SuppressWarnings("CheckReturnValue")
	public static class UnExprContext extends ParserRuleContext {
		public UnExprContext unExpr() {
			return getRuleContext(UnExprContext.class,0);
		}
		public TerminalNode NAO() { return getToken(UaiLangParser.NAO, 0); }
		public TerminalNode MENOS() { return getToken(UaiLangParser.MENOS, 0); }
		public PrimarioContext primario() {
			return getRuleContext(PrimarioContext.class,0);
		}
		public UnExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterUnExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitUnExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitUnExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnExprContext unExpr() throws RecognitionException {
		UnExprContext _localctx = new UnExprContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_unExpr);
		int _la;
		try {
			setState(197);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAO:
			case MENOS:
				enterOuterAlt(_localctx, 1);
				{
				setState(194);
				_la = _input.LA(1);
				if ( !(_la==NAO || _la==MENOS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(195);
				unExpr();
				}
				break;
			case VERDADE:
			case MENTIRA:
			case ABRE_PAR:
			case NUM_REAL:
			case NUM_INT:
			case TEXTO_LITERAL:
			case IDENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(196);
				primario();
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

	@SuppressWarnings("CheckReturnValue")
	public static class PrimarioContext extends ParserRuleContext {
		public TerminalNode NUM_REAL() { return getToken(UaiLangParser.NUM_REAL, 0); }
		public TerminalNode NUM_INT() { return getToken(UaiLangParser.NUM_INT, 0); }
		public TerminalNode TEXTO_LITERAL() { return getToken(UaiLangParser.TEXTO_LITERAL, 0); }
		public TerminalNode VERDADE() { return getToken(UaiLangParser.VERDADE, 0); }
		public TerminalNode MENTIRA() { return getToken(UaiLangParser.MENTIRA, 0); }
		public TerminalNode IDENT() { return getToken(UaiLangParser.IDENT, 0); }
		public TerminalNode ABRE_PAR() { return getToken(UaiLangParser.ABRE_PAR, 0); }
		public ExpressaoContext expressao() {
			return getRuleContext(ExpressaoContext.class,0);
		}
		public TerminalNode FECHA_PAR() { return getToken(UaiLangParser.FECHA_PAR, 0); }
		public PrimarioContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primario; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).enterPrimario(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UaiLangListener ) ((UaiLangListener)listener).exitPrimario(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UaiLangVisitor ) return ((UaiLangVisitor<? extends T>)visitor).visitPrimario(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimarioContext primario() throws RecognitionException {
		PrimarioContext _localctx = new PrimarioContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_primario);
		try {
			setState(209);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUM_REAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(199);
				match(NUM_REAL);
				}
				break;
			case NUM_INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(200);
				match(NUM_INT);
				}
				break;
			case TEXTO_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(201);
				match(TEXTO_LITERAL);
				}
				break;
			case VERDADE:
				enterOuterAlt(_localctx, 4);
				{
				setState(202);
				match(VERDADE);
				}
				break;
			case MENTIRA:
				enterOuterAlt(_localctx, 5);
				{
				setState(203);
				match(MENTIRA);
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 6);
				{
				setState(204);
				match(IDENT);
				}
				break;
			case ABRE_PAR:
				enterOuterAlt(_localctx, 7);
				{
				setState(205);
				match(ABRE_PAR);
				setState(206);
				expressao();
				setState(207);
				match(FECHA_PAR);
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

	public static final String _serializedATN =
		"\u0004\u00010\u00d4\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000*\b\u0000\n\u0000\f\u0000"+
		"-\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u0001A\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002I\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005V\b\u0005"+
		"\n\u0005\f\u0005Y\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006_\b\u0006\n\u0006\f\u0006b\t\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006f\b\u0006\n\u0006\f\u0006i\t\u0006\u0003\u0006k\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006o\b\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0005\u0007u\b\u0007\n\u0007\f\u0007x\t\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007|\b\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u0086\b\b\n\b\f\b\u0089\t\b"+
		"\u0001\b\u0001\b\u0003\b\u008d\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u0096\b\u000b\n\u000b\f\u000b"+
		"\u0099\t\u000b\u0001\f\u0001\f\u0001\f\u0005\f\u009e\b\f\n\f\f\f\u00a1"+
		"\t\f\u0001\r\u0001\r\u0001\r\u0005\r\u00a6\b\r\n\r\f\r\u00a9\t\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00ae\b\u000e\n\u000e\f\u000e"+
		"\u00b1\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00b6\b"+
		"\u000f\n\u000f\f\u000f\u00b9\t\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0005\u0010\u00be\b\u0010\n\u0010\f\u0010\u00c1\t\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0003\u0011\u00c6\b\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012\u00d2\b\u0012\u0001\u0012\u0000\u0000\u0013"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$\u0000\u0006\u0001\u0000\u0011\u0014\u0001\u0000\u001a"+
		"\u001b\u0002\u0000\u001c\u001d\u001f \u0001\u0000!\"\u0001\u0000#%\u0002"+
		"\u0000\u0019\u0019\"\"\u00de\u0000&\u0001\u0000\u0000\u0000\u0002@\u0001"+
		"\u0000\u0000\u0000\u0004B\u0001\u0000\u0000\u0000\u0006J\u0001\u0000\u0000"+
		"\u0000\bN\u0001\u0000\u0000\u0000\nQ\u0001\u0000\u0000\u0000\fZ\u0001"+
		"\u0000\u0000\u0000\u000ep\u0001\u0000\u0000\u0000\u0010}\u0001\u0000\u0000"+
		"\u0000\u0012\u008e\u0001\u0000\u0000\u0000\u0014\u0090\u0001\u0000\u0000"+
		"\u0000\u0016\u0092\u0001\u0000\u0000\u0000\u0018\u009a\u0001\u0000\u0000"+
		"\u0000\u001a\u00a2\u0001\u0000\u0000\u0000\u001c\u00aa\u0001\u0000\u0000"+
		"\u0000\u001e\u00b2\u0001\u0000\u0000\u0000 \u00ba\u0001\u0000\u0000\u0000"+
		"\"\u00c5\u0001\u0000\u0000\u0000$\u00d1\u0001\u0000\u0000\u0000&\'\u0005"+
		"\u0001\u0000\u0000\'+\u0005.\u0000\u0000(*\u0003\u0002\u0001\u0000)(\u0001"+
		"\u0000\u0000\u0000*-\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000"+
		"+,\u0001\u0000\u0000\u0000,.\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000"+
		"\u0000./\u0005\u0002\u0000\u0000/0\u0005\u0000\u0000\u00010\u0001\u0001"+
		"\u0000\u0000\u000012\u0003\u0004\u0002\u000023\u0005*\u0000\u00003A\u0001"+
		"\u0000\u0000\u000045\u0003\u0006\u0003\u000056\u0005*\u0000\u00006A\u0001"+
		"\u0000\u0000\u000078\u0003\b\u0004\u000089\u0005*\u0000\u00009A\u0001"+
		"\u0000\u0000\u0000:;\u0003\n\u0005\u0000;<\u0005*\u0000\u0000<A\u0001"+
		"\u0000\u0000\u0000=A\u0003\f\u0006\u0000>A\u0003\u000e\u0007\u0000?A\u0003"+
		"\u0010\b\u0000@1\u0001\u0000\u0000\u0000@4\u0001\u0000\u0000\u0000@7\u0001"+
		"\u0000\u0000\u0000@:\u0001\u0000\u0000\u0000@=\u0001\u0000\u0000\u0000"+
		"@>\u0001\u0000\u0000\u0000@?\u0001\u0000\u0000\u0000A\u0003\u0001\u0000"+
		"\u0000\u0000BC\u0005\u0003\u0000\u0000CD\u0005.\u0000\u0000DE\u0005(\u0000"+
		"\u0000EH\u0003\u0012\t\u0000FG\u0005\u001e\u0000\u0000GI\u0003\u0014\n"+
		"\u0000HF\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000I\u0005\u0001"+
		"\u0000\u0000\u0000JK\u0005.\u0000\u0000KL\u0005\u001e\u0000\u0000LM\u0003"+
		"\u0014\n\u0000M\u0007\u0001\u0000\u0000\u0000NO\u0005\u0004\u0000\u0000"+
		"OP\u0005.\u0000\u0000P\t\u0001\u0000\u0000\u0000QR\u0005\u0005\u0000\u0000"+
		"RW\u0003\u0014\n\u0000ST\u0005)\u0000\u0000TV\u0003\u0014\n\u0000US\u0001"+
		"\u0000\u0000\u0000VY\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000"+
		"WX\u0001\u0000\u0000\u0000X\u000b\u0001\u0000\u0000\u0000YW\u0001\u0000"+
		"\u0000\u0000Z[\u0005\u0006\u0000\u0000[\\\u0003\u0014\n\u0000\\`\u0005"+
		"\u0007\u0000\u0000]_\u0003\u0002\u0001\u0000^]\u0001\u0000\u0000\u0000"+
		"_b\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000"+
		"\u0000aj\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000cg\u0005\b\u0000"+
		"\u0000df\u0003\u0002\u0001\u0000ed\u0001\u0000\u0000\u0000fi\u0001\u0000"+
		"\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hk\u0001"+
		"\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000jc\u0001\u0000\u0000\u0000"+
		"jk\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000ln\u0005\t\u0000\u0000"+
		"mo\u0005*\u0000\u0000nm\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000"+
		"o\r\u0001\u0000\u0000\u0000pq\u0005\n\u0000\u0000qr\u0003\u0014\n\u0000"+
		"rv\u0005\u000b\u0000\u0000su\u0003\u0002\u0001\u0000ts\u0001\u0000\u0000"+
		"\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000"+
		"\u0000\u0000wy\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000y{\u0005"+
		"\f\u0000\u0000z|\u0005*\u0000\u0000{z\u0001\u0000\u0000\u0000{|\u0001"+
		"\u0000\u0000\u0000|\u000f\u0001\u0000\u0000\u0000}~\u0005\r\u0000\u0000"+
		"~\u007f\u0005.\u0000\u0000\u007f\u0080\u0005\u000e\u0000\u0000\u0080\u0081"+
		"\u0003\u0014\n\u0000\u0081\u0082\u0005\u000f\u0000\u0000\u0082\u0083\u0003"+
		"\u0014\n\u0000\u0083\u0087\u0005\u000b\u0000\u0000\u0084\u0086\u0003\u0002"+
		"\u0001\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000"+
		"\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000"+
		"\u0000\u0000\u0088\u008a\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000"+
		"\u0000\u0000\u008a\u008c\u0005\u0010\u0000\u0000\u008b\u008d\u0005*\u0000"+
		"\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000"+
		"\u0000\u008d\u0011\u0001\u0000\u0000\u0000\u008e\u008f\u0007\u0000\u0000"+
		"\u0000\u008f\u0013\u0001\u0000\u0000\u0000\u0090\u0091\u0003\u0016\u000b"+
		"\u0000\u0091\u0015\u0001\u0000\u0000\u0000\u0092\u0097\u0003\u0018\f\u0000"+
		"\u0093\u0094\u0005\u0018\u0000\u0000\u0094\u0096\u0003\u0018\f\u0000\u0095"+
		"\u0093\u0001\u0000\u0000\u0000\u0096\u0099\u0001\u0000\u0000\u0000\u0097"+
		"\u0095\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098"+
		"\u0017\u0001\u0000\u0000\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u009a"+
		"\u009f\u0003\u001a\r\u0000\u009b\u009c\u0005\u0017\u0000\u0000\u009c\u009e"+
		"\u0003\u001a\r\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009e\u00a1\u0001"+
		"\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001"+
		"\u0000\u0000\u0000\u00a0\u0019\u0001\u0000\u0000\u0000\u00a1\u009f\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a7\u0003\u001c\u000e\u0000\u00a3\u00a4\u0007"+
		"\u0001\u0000\u0000\u00a4\u00a6\u0003\u001c\u000e\u0000\u00a5\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a6\u00a9\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u001b\u0001"+
		"\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa\u00af\u0003"+
		"\u001e\u000f\u0000\u00ab\u00ac\u0007\u0002\u0000\u0000\u00ac\u00ae\u0003"+
		"\u001e\u000f\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001"+
		"\u0000\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001"+
		"\u0000\u0000\u0000\u00b0\u001d\u0001\u0000\u0000\u0000\u00b1\u00af\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b7\u0003 \u0010\u0000\u00b3\u00b4\u0007\u0003"+
		"\u0000\u0000\u00b4\u00b6\u0003 \u0010\u0000\u00b5\u00b3\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b9\u0001\u0000\u0000\u0000\u00b7\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8\u001f\u0001\u0000\u0000"+
		"\u0000\u00b9\u00b7\u0001\u0000\u0000\u0000\u00ba\u00bf\u0003\"\u0011\u0000"+
		"\u00bb\u00bc\u0007\u0004\u0000\u0000\u00bc\u00be\u0003\"\u0011\u0000\u00bd"+
		"\u00bb\u0001\u0000\u0000\u0000\u00be\u00c1\u0001\u0000\u0000\u0000\u00bf"+
		"\u00bd\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0"+
		"!\u0001\u0000\u0000\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c3"+
		"\u0007\u0005\u0000\u0000\u00c3\u00c6\u0003\"\u0011\u0000\u00c4\u00c6\u0003"+
		"$\u0012\u0000\u00c5\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c6#\u0001\u0000\u0000\u0000\u00c7\u00d2\u0005+\u0000\u0000"+
		"\u00c8\u00d2\u0005,\u0000\u0000\u00c9\u00d2\u0005-\u0000\u0000\u00ca\u00d2"+
		"\u0005\u0015\u0000\u0000\u00cb\u00d2\u0005\u0016\u0000\u0000\u00cc\u00d2"+
		"\u0005.\u0000\u0000\u00cd\u00ce\u0005&\u0000\u0000\u00ce\u00cf\u0003\u0014"+
		"\n\u0000\u00cf\u00d0\u0005\'\u0000\u0000\u00d0\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d1\u00c7\u0001\u0000\u0000\u0000\u00d1\u00c8\u0001\u0000\u0000"+
		"\u0000\u00d1\u00c9\u0001\u0000\u0000\u0000\u00d1\u00ca\u0001\u0000\u0000"+
		"\u0000\u00d1\u00cb\u0001\u0000\u0000\u0000\u00d1\u00cc\u0001\u0000\u0000"+
		"\u0000\u00d1\u00cd\u0001\u0000\u0000\u0000\u00d2%\u0001\u0000\u0000\u0000"+
		"\u0014+@HW`gjnv{\u0087\u008c\u0097\u009f\u00a7\u00af\u00b7\u00bf\u00c5"+
		"\u00d1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}