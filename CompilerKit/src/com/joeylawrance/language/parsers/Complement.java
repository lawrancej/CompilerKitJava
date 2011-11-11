package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class Complement extends Expression implements UnaryOperator {
	private Parser node;
	public Complement(Parser p) { this.node = p; }
	public Parser getNode() {
		return node;
	}
}
