package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Optional extends EquivalentExpressionImpl implements UnaryOperator {
	private Parser node;
	public Optional(Parser node) {
		this.node = node;
		this.setEquivalent(Alternation.build(node,EmptyString.emptyString));
	}
	@Override
	public Parser getNode() {
		return node;
	}
}
