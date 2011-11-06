package com.joeylawrance.language;

class PositiveClosure extends EquivalentExpression {
	public PositiveClosure (Parser node) {
		this.setEquivalent(new Catenation(node, new KleeneClosure(node)));
		this.node = node;
	}
}