package generated;
// Generated from jprm.g4 by ANTLR 4.4
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link jprmParser}.
 */
public interface jprmListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link jprmParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(@NotNull jprmParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(@NotNull jprmParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#goal}.
	 * @param ctx the parse tree
	 */
	void enterGoal(@NotNull jprmParser.GoalContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#goal}.
	 * @param ctx the parse tree
	 */
	void exitGoal(@NotNull jprmParser.GoalContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull jprmParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull jprmParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void enterMainClass(@NotNull jprmParser.MainClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void exitMainClass(@NotNull jprmParser.MainClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull jprmParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull jprmParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(@NotNull jprmParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(@NotNull jprmParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(@NotNull jprmParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(@NotNull jprmParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link jprmParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(@NotNull jprmParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link jprmParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(@NotNull jprmParser.ClassDeclarationContext ctx);
}