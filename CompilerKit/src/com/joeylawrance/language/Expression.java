package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;
import com.joeylawrance.visitor.Visitor;

class Expression extends Parser {
	public Visitor<Parser,String> getPrinter() {
		return new StringVisitor();
	}

	public DerivativeVisitor<Parser, Parser> getDerivative() {
		return new RegularDerivativeVisitor(RegularNullableVisitor.nullable);
	}
}