package com.joeylawrance.language;

class EquivalentExpression extends Expression {
	private Parser equivalent;
	Parser node;
	Parser getEquivalent() {
		return equivalent;
	}
	void setEquivalent(Parser equivalent) {
		this.equivalent = equivalent;
	}
}