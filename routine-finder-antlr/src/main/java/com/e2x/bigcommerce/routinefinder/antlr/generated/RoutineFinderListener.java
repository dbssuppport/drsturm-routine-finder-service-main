// Generated from RoutineFinder.g4 by ANTLR 4.9.1

package com.e2x.bigcommerce.routinefinder.antlr.generated;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RoutineFinderParser}.
 */
public interface RoutineFinderListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(RoutineFinderParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(RoutineFinderParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#and_or}.
	 * @param ctx the parse tree
	 */
	void enterAnd_or(RoutineFinderParser.And_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#and_or}.
	 * @param ctx the parse tree
	 */
	void exitAnd_or(RoutineFinderParser.And_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(RoutineFinderParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(RoutineFinderParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#in_operand}.
	 * @param ctx the parse tree
	 */
	void enterIn_operand(RoutineFinderParser.In_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#in_operand}.
	 * @param ctx the parse tree
	 */
	void exitIn_operand(RoutineFinderParser.In_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#is_operand}.
	 * @param ctx the parse tree
	 */
	void enterIs_operand(RoutineFinderParser.Is_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#is_operand}.
	 * @param ctx the parse tree
	 */
	void exitIs_operand(RoutineFinderParser.Is_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#match_rule}.
	 * @param ctx the parse tree
	 */
	void enterMatch_rule(RoutineFinderParser.Match_ruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#match_rule}.
	 * @param ctx the parse tree
	 */
	void exitMatch_rule(RoutineFinderParser.Match_ruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoutineFinderParser#values}.
	 * @param ctx the parse tree
	 */
	void enterValues(RoutineFinderParser.ValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoutineFinderParser#values}.
	 * @param ctx the parse tree
	 */
	void exitValues(RoutineFinderParser.ValuesContext ctx);
}