package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Expression;


public class EmptySet extends Expression {
	private EmptySet() {}
	public static final EmptySet emptySet = new EmptySet();
}