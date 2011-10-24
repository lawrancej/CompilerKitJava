package com.joeylawrance.language;

class KleeneClosure extends Expression {
	Parser node;
	public KleeneClosure (Parser node) { this.node = node; }
}