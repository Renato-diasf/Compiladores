// Generated from UaiLang.g4 by ANTLR 4.13.1

package br.com.compiladores.uailang;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link UaiLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface UaiLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(UaiLangParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#comando}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComando(UaiLangParser.ComandoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#declaracao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao(UaiLangParser.DeclaracaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#atribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtribuicao(UaiLangParser.AtribuicaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#leitura}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeitura(UaiLangParser.LeituraContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#escrita}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscrita(UaiLangParser.EscritaContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#condicional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondicional(UaiLangParser.CondicionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#repeticaoEnquanto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeticaoEnquanto(UaiLangParser.RepeticaoEnquantoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#repeticaoPara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeticaoPara(UaiLangParser.RepeticaoParaContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(UaiLangParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#expressao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao(UaiLangParser.ExpressaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#ouExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuExpr(UaiLangParser.OuExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#eExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEExpr(UaiLangParser.EExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#igualdadeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgualdadeExpr(UaiLangParser.IgualdadeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#relExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(UaiLangParser.RelExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#adExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdExpr(UaiLangParser.AdExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#multExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExpr(UaiLangParser.MultExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#unExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnExpr(UaiLangParser.UnExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UaiLangParser#primario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimario(UaiLangParser.PrimarioContext ctx);
}