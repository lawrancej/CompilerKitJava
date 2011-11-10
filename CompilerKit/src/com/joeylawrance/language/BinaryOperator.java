package com.joeylawrance.language;

public class BinaryOperator extends Expression {
	private Parser left;
	private Parser right;
	public BinaryOperator (Parser left, Parser right) { this.left = left; this.right = right; }
	public Parser getLeft() { return left; }
	public Parser getRight() { return right; }
}