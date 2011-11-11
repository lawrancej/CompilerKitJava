package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Difference extends BinaryOperatorImpl implements EquivalentExpression {
	private Parser equivalent;
	public Difference(Parser left, Parser right) {
		super(left, right);
		setEquivalent(new Intersection(left,new Complement(right)));
	}
	public Parser getEquivalent() {
		return equivalent;
	}
	void setEquivalent(Parser equivalent) {
		this.equivalent = equivalent;
	}
}
