package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;


public class EmptyString extends Expression {
	private EmptyString() {}
	public static final EmptyString emptyString = new EmptyString();
}