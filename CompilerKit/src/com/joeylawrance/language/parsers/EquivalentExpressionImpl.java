package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class EquivalentExpressionImpl extends Expression implements EquivalentExpression {
	private Parser equivalent;
	public Parser getEquivalent() {
		return equivalent;
	}
	public void setEquivalent(Parser equivalent) {
		this.equivalent = equivalent;
	}
}