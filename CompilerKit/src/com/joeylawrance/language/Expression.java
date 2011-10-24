package com.joeylawrance.language;

import com.joeylawrance.language.Regular.StringVisitor;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}
}