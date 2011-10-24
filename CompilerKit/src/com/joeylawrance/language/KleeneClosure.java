package com.joeylawrance.language;

import com.joeylawrance.language.Regular.Expression;

class KleeneClosure extends Expression {
	Parser node;
	public KleeneClosure (Parser node) { this.node = node; }
}