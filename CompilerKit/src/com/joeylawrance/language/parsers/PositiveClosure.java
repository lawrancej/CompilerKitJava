package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class PositiveClosure extends EquivalentExpressionImpl implements UnaryOperator {
	private Parser node;
	public PositiveClosure (Parser node) {
		this.setEquivalent(Catenation.build(node, new KleeneClosure(node)));
		this.node = node;
	}
	@Override
	public Parser getNode() {
		return node;
	}
}