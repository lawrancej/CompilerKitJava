package com.joeylawrance.language;

import com.joeylawrance.visitor.Visitor;

// FIXME: nonterminals
public class Grammar implements Parser {
	private static ContextFreeStringVisitor printer = new ContextFreeStringVisitor();
	public Visitor<Parser,String> getPrinter() {
		return printer;
	}
	public String toString () {
		return getPrinter().visit(this);
	}
}