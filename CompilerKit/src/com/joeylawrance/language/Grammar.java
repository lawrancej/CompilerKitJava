package com.joeylawrance.language;

import com.joeylawrance.language.ContextFree.ContextFreeDerivativeVisitor;
import com.joeylawrance.visitor.Visitor;

// FIXME: nonterminals
class Grammar implements Parser {
	private static ContextFreeStringVisitor printer = new ContextFreeStringVisitor();
	public Visitor<Parser,String> getPrinter() {
		return printer;
	}
	@Override
	public DerivativeVisitor<Parser, Parser> getDerivative() {
		return new ContextFreeDerivativeVisitor(ContextFreeNullableVisitor.getInstance());
	}
	public String toString () {
		return getPrinter().visit(this);
	}
}