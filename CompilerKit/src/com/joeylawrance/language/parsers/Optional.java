package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Optional extends EquivalentExpression {
	public Optional(Parser node) {
		this.setNode(node);
		this.setEquivalent(Alternation.alternation(node,EmptyString.emptyString));
	}
}
