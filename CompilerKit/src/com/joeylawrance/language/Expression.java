package com.joeylawrance.language;

class Expression extends Parser {
	public ReflectiveVisitor<String> getPrinter() {
		return new StringVisitor();
	}
}