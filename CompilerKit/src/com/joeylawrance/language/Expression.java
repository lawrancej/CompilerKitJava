package com.joeylawrance.language;

import com.joeylawrance.language.Regular.StringVisitor;
import com.joeylawrance.visitor.ReflectiveVisitor;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}
}