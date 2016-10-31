package visitor;

import symboltable.Class;
import symboltable.Method;
import symboltable.SymbolTable;
import ast.And;
import ast.ArrayAssign;
import ast.ArrayLength;
import ast.ArrayLookup;
import ast.Assign;
import ast.Block;
import ast.BooleanType;
import ast.Call;
import ast.ClassDeclExtends;
import ast.ClassDeclSimple;
import ast.False;
import ast.Formal;
import ast.Identifier;
import ast.IdentifierExp;
import ast.IdentifierType;
import ast.If;
import ast.IntArrayType;
import ast.IntegerLiteral;
import ast.IntegerType;
import ast.LessThan;
import ast.MainClass;
import ast.MethodDecl;
import ast.Minus;
import ast.NewArray;
import ast.NewObject;
import ast.Not;
import ast.Plus;
import ast.Print;
import ast.Program;
import ast.This;
import ast.Times;
import ast.True;
import ast.Type;
import ast.VarDecl;
import ast.While;

public class TypeCheckVisitor implements TypeVisitor {

	private SymbolTable symbolTable;
	private Method currMethod;
	private Class currClass;

	TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		n.i1.accept(this);
		currClass = symbolTable.getClass(n.i1.s);
		n.i2.accept(this);
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		n.i.accept(this);
		currClass = symbolTable.getClass(n.i.s);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currClass = null;
		return null;	
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		n.i.accept(this);
		n.j.accept(this);
		currClass = symbolTable.getClass(n.i.s);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}		
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currClass = null;
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		Type vt1 = n.t.accept(this);
		n.i.accept(this);
		return vt1;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		Type vt1 = n.t.accept(this);
		n.i.accept(this);
		currMethod = currClass.getMethod(n.i.s);
		
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
		currMethod = null;
		return vt1;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		Type vt1 = n.t.accept(this);
		n.i.accept(this);
		return vt1;
	}

	public Type visit(IntArrayType n) {
		return new IntArrayType();
	}

	public Type visit(BooleanType n) {
		return new BooleanType();
	}

	public Type visit(IntegerType n) {
		return new IntegerType();
	}

	// String s;
	public Type visit(IdentifierType n) {
		return new IdentifierType(n.s);
	}

	// StatementList sl;
	public Type visit(Block n) {
		Type returnType = null;
		for (int i = 0; i < n.sl.size(); i++) {
			returnType = n.sl.elementAt(i).accept(this);
		}
		return returnType;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		Type vt1 = n.e.accept(this);
		if(!(vt1 instanceof BooleanType)){
			return null;
		}
		Type vt2 = n.s1.accept(this);
		Type vt3 = n.s2.accept(this);
		return new BooleanType();
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		Type vt1 = n.e.accept(this);
		Type vt2 = null;
		if(vt1 instanceof BooleanType){
			vt2 = n.s.accept(this);
			return new BooleanType();
		}else{
			return null;
		}
	}

	// Exp e;
	public Type visit(Print n) {
		return n.e.accept(this);
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		Type vt1 = n.i.accept(this);
		Type vt2 = n.e.accept(this);
		if(symbolTable.compareTypes(vt1,vt2)){
			return n.e.accept(this);
		}else if(vt1 instanceof IdentifierType && vt2 instanceof IdentifierType){
			Class type1 = symbolTable.getClass(((IdentifierType) vt1).s);
			Class type2 = symbolTable.getClass(((IdentifierType) vt2).s);
			if(type1.equals(type2)){
				return vt1;
			}else if(type1.parent().equals(type2)){
				return vt1;
			}
			return null;
		}else{
			return null;
		}
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		Type vt1 = n.i.accept(this);
		Type vt2 = n.e1.accept(this);
		Type vt3 = n.e2.accept(this);
		if(vt2 instanceof IntegerType){
			if(vt1 instanceof IntArrayType && vt3 instanceof IntegerType){
				return vt1;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e2.accept(this);
		if(symbolTable.compareTypes(vt1, vt2) && vt1 instanceof BooleanType){
			return new BooleanType();
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e2.accept(this);
		if(vt1 instanceof IntegerType && vt2 instanceof IntegerType){
			return new BooleanType();
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e1.accept(this);
		if(vt1 instanceof IntegerType && vt2 instanceof IntegerType){
			return new IntegerType();
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e1.accept(this);
		if(vt1 instanceof IntegerType && vt2 instanceof IntegerType){
			return new IntegerType();
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e1.accept(this);
		if(vt1 instanceof IntegerType && vt2 instanceof IntegerType){
			return new IntegerType();
		}else{
			return null;
		}
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type vt1 = n.e1.accept(this);
		Type vt2 = n.e2.accept(this);
		if(vt2 instanceof IntegerType){
			return new IntegerType();
		}else{
			return null;
		}
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		return new IntegerType();
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		Type callerType = n.e.accept(this);
		Type vt1 = null;
		if(callerType instanceof IdentifierType){
			if(symbolTable.containsClass(((IdentifierType) callerType).s)){
				Class objectClass = symbolTable.getClass(((IdentifierType) callerType).s);
				if(objectClass.containsMethod(n.i.s)){
					Class tempClass = currClass;
					this.currClass = objectClass;
					Method tempMethod = currMethod;
					currMethod = null;					
					n.i.accept(this);				
					this.currMethod = currClass.getMethod(n.i.s);
					vt1 =currClass.getMethod(n.i.s).type();				
					this.currClass = tempClass;
					this.currMethod = tempMethod;
					for (int i = 0; i < n.el.size(); i++) {
						n.el.elementAt(i).accept(this);
					}	
				}
			}
		}
		return vt1;
	}
	
	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}
	
	public Type visit(False n) {
		return new BooleanType();
	}

	public boolean insideMethod(){
		boolean r = true;
		if(currMethod==null){
			r = false;
		}
		return r;
	}
	
	public Type getVarType(String vN){
		if(insideMethod()){
			if(currMethod.containsVar(vN)){
				return currMethod.getVar(vN).type();
			}else if(currMethod.containsParam(vN)){
				return currMethod.getParam(vN).type();
			}
		}
		if((currClass!=null)){
			if(currClass.containsVar(vN)){
				return currClass.getVar(vN).type();
			}
			if(currClass.containsMethod(vN)){
				return currClass.getMethod(vN).type();
			}
		}
		if(symbolTable.containsClass(vN)){
			return symbolTable.getClass(vN).type();
		}
		return null;
	}
	
	// String s;
	public Type visit(IdentifierExp n) {
		return getVarType(n.s);
	}

	public Type visit(This n) {
		return getVarType(currClass.getId());
	}
	// Exp e;
	public Type visit(NewArray n) {
		return new IntArrayType();
	}

	public Class getClass(String cN){
		if(symbolTable.containsClass(cN)){
			return symbolTable.getClass(cN);
		}else{
			return null;
		}
	}
	// Identifier i;
	public Type visit(NewObject n) {
		if(getClass(n.i.s)!=null){
			return getClass(n.i.s).type();
		}else{
			return null;
		}
	}
	// Exp e;
	public Type visit(Not n) {
		Type vt1 = n.e.accept(this);
		if(vt1 instanceof BooleanType){
			return new BooleanType();
		}else{
			return null;
		}
	}

	// String s;
	public Type visit(Identifier n) {
		return getVarType(n.s);
	}
}
