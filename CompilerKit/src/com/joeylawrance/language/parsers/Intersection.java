package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Intersection extends BinaryOperatorImpl {

	public Intersection(Parser left, Parser right) {
		super(left, right);
	}
	
	public static Intersection intersection (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Intersection (current, regexen[i]);
		}
		return (Intersection) current;
	}
}
