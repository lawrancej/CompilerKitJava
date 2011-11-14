package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Parser;

public class Alternation extends BinaryOperatorImpl {
	private static final HashMap<Parser,HashMap<Parser,Alternation>> flyweight = new HashMap<Parser,HashMap<Parser,Alternation>>();

	private Alternation(Parser left, Parser right) { super(left, right); }

	public static Alternation build (Parser left, Parser right) {
		HashMap<Parser,Alternation> lookup;
		Alternation alternation;
		if (!flyweight.containsKey(left)) {
			lookup = new HashMap<Parser,Alternation>();
			flyweight.put(left, lookup);
		} else {
			lookup = flyweight.get(left);
		}
		if (!lookup.containsKey(right)) {
			alternation = new Alternation(left,right);
			lookup.put(right, alternation);
		} else {
			alternation = lookup.get(right);
		}
		return alternation;
	}

	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = build (current, regexen[i]);
		}
		return (Alternation) current;
	}
}