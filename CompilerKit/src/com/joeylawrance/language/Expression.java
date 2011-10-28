package com.joeylawrance.language;

import com.joeylawrance.language.ContextFree.ContextFreeNullableVisitor;
import com.joeylawrance.visitor.ReflectiveVisitor;
import com.joeylawrance.visitor.Visitor;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}

	public DerivativeVisitor<Object, Parser> getDerivative() {
		return new RegularDerivativeVisitor();
	}
	public Visitor<Object,Parser> getNullable() {
		return RegularNullableVisitor.nullable;
	}
}