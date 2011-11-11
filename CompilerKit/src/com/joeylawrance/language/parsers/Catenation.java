package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Catenation extends BinaryOperatorImpl {
	public Catenation(Parser left, Parser right) { super(left, right); }

	public static Catenation catenation (Parser ... parsers) {
		Parser current = parsers[0];
		for (int i = 1; i < parsers.length; i++) {
			current = new Catenation (current, parsers[i]);
		}
		return (Catenation) current;
	}
}