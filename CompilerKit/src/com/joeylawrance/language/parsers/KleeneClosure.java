package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class KleeneClosure extends Expression {
	private Parser node;
	public KleeneClosure (Parser node) { this.node = node; }
	public Parser getNode() {
		return node;
	}
}