package build;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import ast.*;
import generated.jprmParser.*;

public class BuildAst  {

	public Program visitGoal(GoalContext goal){
		
		ClassDeclList classDeclList = this.visitClassDeclList(goal.classDeclaration());
		MainClass main = this.visitMain(goal.mainClass());
		return new Program(main, classDeclList);
	}

	private ClassDeclList visitClassDeclList(List<ClassDeclarationContext> listClassDecl) {
		
		ClassDeclList classDeclList = new ClassDeclList();
		for(int i = 0; i < listClassDecl.size(); i++){
			classDeclList.addElement(this.visitClassDecl(listClassDecl.get(i)));
		}
		return classDeclList;
	}
	
	private MainClass visitMain(MainClassContext mcc) {
		
		Statement statement = this.visitStatement(mcc.statement());
		Identifier id1 = new Identifier(mcc.Identifier(0).toString());
		Identifier id2 = new Identifier(mcc.Identifier(1).toString());
		
		return new MainClass(id1, id2, statement);
	}

	private ClassDecl visitClassDecl(ClassDeclarationContext classDeclCtx) {
		
		List<TerminalNode> tokens = classDeclCtx.Identifier();
		ClassDecl cd;
		if(tokens.size() < 2){
			cd = new ClassDeclSimple(new Identifier(tokens.get(0).toString()),this.visitVarDeclList(classDeclCtx.varDeclaration()), this.visitMethodDeclList(classDeclCtx.methodDeclaration()));
		}else{
		    cd = new ClassDeclExtends(new Identifier(tokens.get(0).toString()),new Identifier(tokens.get(3).toString()),this.visitVarDeclList(classDeclCtx.varDeclaration()), this.visitMethodDeclList(classDeclCtx.methodDeclaration()));
		}
		return cd;
	}

	
	private Statement visitStatement(StatementContext st) {
		
		String stmt = st.getText();
		TerminalNode ids = st.Identifier();
		List<ExpressionContext> exps = st.expression();
		if(stmt.startsWith("if")){
			return new If(this.visitExp(exps.get(0)), this.visitStatement(st.statement(0)), this.visitStatement(st.statement(1)));
		}else if(stmt.startsWith("while")){
			return new While(this.visitExp(exps.get(0)), this.visitStatement(st.statement(0)));
		}else if(ids != null && exps.size() == 2){
			return new ArrayAssign(new Identifier(ids.getText()), this.visitExp(exps.get(0)), this.visitExp(exps.get(1)));
		}else if(ids != null && exps.size() == 1){
			return new Assign(new Identifier(ids.getText()), this.visitExp(exps.get(0)));
		}else if(stmt.startsWith("System.out.println")){
			return new Print(this.visitExp(exps.get(0)));
		}else{
			return new Block(this.visitStatementList(st.statement()));
		}
	}

	private MethodDecl visitMethodDecl(MethodDeclarationContext mdc) {
		
		List<TypeContext> type = mdc.type();
		List<TerminalNode> tokens = mdc.Identifier();
		FormalList args = new FormalList();
		Type tipoMetodo = this.visitType(type.get(0));
		Identifier nomeMetodo = new Identifier(tokens.get(0).toString());
		
		for(int i = 1; i< type.size();i++){
			args.addElement(new Formal(this.visitType(type.get(i)),new Identifier(tokens.get(i).toString())));
		}
		VarDeclList variaveis = this.visitVarDeclList(mdc.varDeclaration());
		StatementList statements = this.visitStatementList(mdc.statement());
		Exp exp = this.visitExp(mdc.expression());
		return new MethodDecl(tipoMetodo,nomeMetodo,args,variaveis,statements,exp);
	}

	
	private MethodDeclList visitMethodDeclList(List<MethodDeclarationContext> md) {
		
		MethodDeclList mdList = new MethodDeclList();
		for(int i = 0; i < md.size(); i++){
			mdList.addElement(this.visitMethodDecl(md.get(i)));
		}
		return mdList;
	}


	private Exp visitExp(ExpressionContext exp) {
		
		String text = exp.getText();
		TerminalNode op = exp.Operand();
		TerminalNode ids = exp.Identifier();
		List<ExpressionContext> expList = exp.expression();
		TerminalNode num = exp.INTEGER_LITERAL();
		
		if(op != null){
			Exp e1 = this.visitExp(expList.get(0));
			Exp e2 = this.visitExp(expList.get(1));
			String opText = op.getText();
			switch(opText){
				case "&&":
					return new And(e1, e2);	
				case "<":
					return new LessThan(e1, e2);
				case "+":
					return new Plus(e1, e2);
				case "-":
					return new Minus(e1,e2);
				case "*":
					return new Times(e1, e2);	
			}	
		}else if(text.charAt(0) == '('){
			return this.visitExp(expList.get(0));
		}else if(expList.size() == 2 && ids == null){
			return new ArrayLookup(this.visitExp(expList.get(0)), this.visitExp(expList.get(1)));
		}else if(expList.size() >= 1 && ids != null){		
			return new Call(this.visitExp(expList.get(0)), new Identifier(ids.getText()), this.visitExpList(expList));
		}else if(expList.size() == 1 && !text.contains("new") && text.contains("length")){
			return new ArrayLength(this.visitExp(expList.get(0)));
		}else if(num != null){
			return new IntegerLiteral(Integer.parseInt(num.getText()));
		}else if(ids != null && !text.contains("new")){
			return new IdentifierExp(ids.getText());
		}else if(text.contains("true")){
			return new True();
		}else if(text.contains("false")){
			return new False();
		}else if(text.contains("this")){
			return new This();
		}else if(text.contains("new")){
			if(expList.size() == 1){
				return new NewArray(this.visitExp(expList.get(0)));
			}else{
				return new NewObject(new Identifier(ids.getText()));
			}
		}else if(text.contains("!")){
			return new Not(this.visitExp(expList.get(0)));
		}
			
		return this.visitExp(expList.get(0));
		
	}

	private ExpList visitExpList(List<ExpressionContext> exps) {
		ExpList expList = new ExpList();
		for(int i = 1; i<exps.size();i++){
			expList.addElement(this.visitExp(exps.get(i)));
		}
		return expList;

	}

	private StatementList visitStatementList(List<StatementContext> sts) {
		StatementList stmtList = new StatementList();
		for(int i = 0; i < sts.size(); i++){
			stmtList.addElement(this.visitStatement(sts.get(i)));
		}
		return stmtList;
	}

	private Type visitType(TypeContext tc) {
		String type = tc.getText();
		if(type.contains("int")){
			return new BooleanType();
		}else if(type.contains("int[]")){
			return new IntArrayType();
		}else if(type.contains("boolean")){
			return new IntegerType();
		}else{
			return new IdentifierType(tc.Identifier().getText());
		}
	}

	
	private VarDecl visitVarDecl(VarDeclarationContext vd) {
		return new VarDecl(this.visitType(vd.type()), new Identifier(vd.Identifier().getText()));
	}

	
	private VarDeclList visitVarDeclList(List<VarDeclarationContext> listVD) {
		VarDeclList vdList = new VarDeclList();
		for(int i = 0; i < listVD.size(); i++){
			vdList.addElement(this.visitVarDecl(listVD.get(i)));
		}
		return vdList;
	}


	
	
}
