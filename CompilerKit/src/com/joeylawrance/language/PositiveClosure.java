package com.joeylawrance.language;

class PositiveClosure extends EquivalentExpression {
	public PositiveClosure (Parser node) {
		this.equivalent = new Catenation(node, new KleeneClosure(node));
		this.node = node;
	}
}