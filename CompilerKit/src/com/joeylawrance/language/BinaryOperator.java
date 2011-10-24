package com.joeylawrance.language;

import com.joeylawrance.language.Regular.Expression;

class BinaryOperator extends Expression {
	Parser left, right;		
	public BinaryOperator (Parser left, Parser right) { this.left = left; this.right = right; }
}