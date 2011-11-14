package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Parser;

/**
 * Represent the concatenation of two parsers.
 *
 */
public class Catenation extends BinaryOperatorImpl {
	// Cache catenations to prevent duplicate allocation
	private static final HashMap<Parser,HashMap<Parser,Catenation>> flyweight = new HashMap<Parser,HashMap<Parser,Catenation>>();

	// Constructor is private to prevent duplicate allocation
	private Catenation(Parser left, Parser right) { super(left, right); }
	
	// Check if the catenation already exists.
	// If so, return it.
	// Otherwise, allocate a new catenation and cache it.
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

	// Convenience method to produce a chain of catenations
	public static Catenation catenation (Parser ... parsers) {
		Parser current = parsers[0];
		for (int i = 1; i < parsers.length; i++) {
			current = build (current, parsers[i]);
		}
		return (Catenation) current;
	}
}