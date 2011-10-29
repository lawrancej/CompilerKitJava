package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}

	public DerivativeVisitor<Object, Parser> getDerivative() {
		return new RegularDerivativeVisitor(RegularNullableVisitor.nullable);
	}
}