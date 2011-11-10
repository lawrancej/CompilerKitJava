package com.joeylawrance.language;

import com.joeylawrance.visitor.Visitor;

class Expression implements Parser {
	private static RegularStringVisitor printer = new RegularStringVisitor();
	public Visitor<Parser,String> getPrinter() {
		return printer;
	}

	public DerivativeVisitor<Parser, Parser> getDerivative() {
		return new RegularDerivativeVisitor(RegularNullableVisitor.getInstance());
	}
	public String toString () {
		return getPrinter().visit(this);
	}
}