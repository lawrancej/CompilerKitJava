package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Alternation extends BinaryOperatorImpl {
	public Alternation(Parser left, Parser right) { super(left, right); }

	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
}