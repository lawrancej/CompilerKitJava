package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Parser;

/**
 * Represent the alternation (union) of two parsers.
 *
 */
public class Alternation extends BinaryOperatorImpl {
	// Cache alternations to prevent duplicate allocation
	private static final HashMap<Parser,HashMap<Parser,Alternation>> flyweight = new HashMap<Parser,HashMap<Parser,Alternation>>();

	// Constructor is private to prevent duplicate allocation
	private Alternation(Parser left, Parser right) { super(left, right); }

	// Check if the alternation already exists.
	// If so, return it.
	// Otherwise, allocate a new alternation and cache it.
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

	// Convenience method to produce a chain of alternations
	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = build (current, regexen[i]);
		}
		return (Alternation) current;
	}
}