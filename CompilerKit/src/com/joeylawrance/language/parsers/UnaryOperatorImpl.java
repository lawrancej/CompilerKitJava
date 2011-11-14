package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class UnaryOperatorImpl extends Expression {
	private Parser node;
	public UnaryOperatorImpl (Parser node) { this.node = node; }
	public Parser getNode() {
		return node;
	}
}