package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}

	public ReflectiveVisitor<Parser> getDerivative() {
		return new DerivativeVisitor();
	}
}