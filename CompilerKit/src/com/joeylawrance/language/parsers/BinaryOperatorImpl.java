package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;
import com.joeylawrance.language.Parser;

public class BinaryOperatorImpl extends Expression implements BinaryOperator {
	private Parser left;
	private Parser right;
	public BinaryOperatorImpl (Parser left, Parser right) { this.left = left; this.right = right; }
	public Parser getLeft() { return left; }
	public Parser getRight() { return right; }
}