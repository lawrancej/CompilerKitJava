package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class EquivalentExpression extends Expression {
	private Parser equivalent;
	private Parser node;
	public Parser getEquivalent() {
		return equivalent;
	}
	public void setEquivalent(Parser equivalent) {
		this.equivalent = equivalent;
	}
	public Parser getNode() {
		return node;
	}
	public void setNode(Parser node) {
		this.node = node;
	}
}