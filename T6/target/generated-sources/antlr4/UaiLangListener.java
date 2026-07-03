// Generated from UaiLang.g4 by ANTLR 4.13.1

package br.com.compiladores.uailang;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link UaiLangParser}.
 */
public interface UaiLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(UaiLangParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(UaiLangParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#comando}.
	 * @param ctx the parse tree
	 */
	void enterComando(UaiLangParser.ComandoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#comando}.
	 * @param ctx the parse tree
	 */
	void exitComando(UaiLangParser.ComandoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#declaracao}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao(UaiLangParser.DeclaracaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#declaracao}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao(UaiLangParser.DeclaracaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#atribuicao}.
	 * @param ctx the parse tree
	 */
	void enterAtribuicao(UaiLangParser.AtribuicaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#atribuicao}.
	 * @param ctx the parse tree
	 */
	void exitAtribuicao(UaiLangParser.AtribuicaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#leitura}.
	 * @param ctx the parse tree
	 */
	void enterLeitura(UaiLangParser.LeituraContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#leitura}.
	 * @param ctx the parse tree
	 */
	void exitLeitura(UaiLangParser.LeituraContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#escrita}.
	 * @param ctx the parse tree
	 */
	void enterEscrita(UaiLangParser.EscritaContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#escrita}.
	 * @param ctx the parse tree
	 */
	void exitEscrita(UaiLangParser.EscritaContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#condicional}.
	 * @param ctx the parse tree
	 */
	void enterCondicional(UaiLangParser.CondicionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#condicional}.
	 * @param ctx the parse tree
	 */
	void exitCondicional(UaiLangParser.CondicionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#repeticaoEnquanto}.
	 * @param ctx the parse tree
	 */
	void enterRepeticaoEnquanto(UaiLangParser.RepeticaoEnquantoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#repeticaoEnquanto}.
	 * @param ctx the parse tree
	 */
	void exitRepeticaoEnquanto(UaiLangParser.RepeticaoEnquantoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#repeticaoPara}.
	 * @param ctx the parse tree
	 */
	void enterRepeticaoPara(UaiLangParser.RepeticaoParaContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#repeticaoPara}.
	 * @param ctx the parse tree
	 */
	void exitRepeticaoPara(UaiLangParser.RepeticaoParaContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(UaiLangParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(UaiLangParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#expressao}.
	 * @param ctx the parse tree
	 */
	void enterExpressao(UaiLangParser.ExpressaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#expressao}.
	 * @param ctx the parse tree
	 */
	void exitExpressao(UaiLangParser.ExpressaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#ouExpr}.
	 * @param ctx the parse tree
	 */
	void enterOuExpr(UaiLangParser.OuExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#ouExpr}.
	 * @param ctx the parse tree
	 */
	void exitOuExpr(UaiLangParser.OuExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#eExpr}.
	 * @param ctx the parse tree
	 */
	void enterEExpr(UaiLangParser.EExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#eExpr}.
	 * @param ctx the parse tree
	 */
	void exitEExpr(UaiLangParser.EExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#igualdadeExpr}.
	 * @param ctx the parse tree
	 */
	void enterIgualdadeExpr(UaiLangParser.IgualdadeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#igualdadeExpr}.
	 * @param ctx the parse tree
	 */
	void exitIgualdadeExpr(UaiLangParser.IgualdadeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelExpr(UaiLangParser.RelExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelExpr(UaiLangParser.RelExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#adExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdExpr(UaiLangParser.AdExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#adExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdExpr(UaiLangParser.AdExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#multExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultExpr(UaiLangParser.MultExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#multExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultExpr(UaiLangParser.MultExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#unExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnExpr(UaiLangParser.UnExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#unExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnExpr(UaiLangParser.UnExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UaiLangParser#primario}.
	 * @param ctx the parse tree
	 */
	void enterPrimario(UaiLangParser.PrimarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link UaiLangParser#primario}.
	 * @param ctx the parse tree
	 */
	void exitPrimario(UaiLangParser.PrimarioContext ctx);
}