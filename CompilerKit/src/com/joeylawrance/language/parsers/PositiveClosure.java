package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class PositiveClosure extends EquivalentExpression {
	public PositiveClosure (Parser node) {
		this.setEquivalent(new Catenation(node, new KleeneClosure(node)));
		this.setNode(node);
	}
}