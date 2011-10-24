package com.joeylawrance.language;

class BinaryOperator extends Expression {
	Parser left, right;		
	public BinaryOperator (Parser left, Parser right) { this.left = left; this.right = right; }
}