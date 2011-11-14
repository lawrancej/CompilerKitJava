package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Parser;

public class Catenation extends BinaryOperatorImpl {
	private static final HashMap<Parser,HashMap<Parser,Catenation>> flyweight = new HashMap<Parser,HashMap<Parser,Catenation>>();

	private Catenation(Parser left, Parser right) { super(left, right); }
	
	public static Catenation build (Parser left, Parser right) {
		HashMap<Parser,Catenation> lookup;
		Catenation catenation;
		if (!flyweight.containsKey(left)) {
			lookup = new HashMap<Parser,Catenation>();
			flyweight.put(left, lookup);
		} else {
			lookup = flyweight.get(left);
		}
		if (!lookup.containsKey(right)) {
			catenation = new Catenation(left,right);
			lookup.put(right, catenation);
		} else {
			catenation = lookup.get(right);
		}
		return catenation;
	}

	public static Catenation catenation (Parser ... parsers) {
		Parser current = parsers[0];
		for (int i = 1; i < parsers.length; i++) {
			current = build (current, parsers[i]);
		}
		return (Catenation) current;
	}
}