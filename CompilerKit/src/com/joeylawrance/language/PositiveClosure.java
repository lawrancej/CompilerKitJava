package com.joeylawrance.language;

class PositiveClosure extends EquivalentExpression {
	public PositiveClosure (Parser node) {
		this.equivalent = Regular.catenation(node, Regular.kleeneClosure(node));
		this.node = node;
	}
}