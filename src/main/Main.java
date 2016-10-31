package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.Program;
import build.BuildAst;
import generated.*;
import visitor.PrettyPrintVisitor;

public class Main {
	public static void main(String[] args) throws IOException {
		
		
		InputStream input= new FileInputStream("test.txt");
		ANTLRInputStream in = new ANTLRInputStream(input);
		jprmLexer lexer = new jprmLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
			
		jprmParser parser = new jprmParser(tokens);
		BuildAst builder = new BuildAst();
			
			
		Program prog = builder.visitGoal(parser.goal());
		PrettyPrintVisitor print = new PrettyPrintVisitor();
		prog.accept(print);
		
	}

}