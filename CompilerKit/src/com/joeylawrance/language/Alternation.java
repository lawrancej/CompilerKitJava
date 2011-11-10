package com.joeylawrance.language;

public class Alternation extends BinaryOperator {
	public Alternation(Parser left, Parser right) { super(left, right); }

	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
}