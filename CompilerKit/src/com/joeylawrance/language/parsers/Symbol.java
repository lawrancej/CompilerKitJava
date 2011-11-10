package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Expression;


public class Symbol extends Expression {
	public final char c;
	private static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	// Limit construction to use memory wisely
	private Symbol (char c) { this.c = c; }
	public static Symbol symbol (char c) {
		if (!flyweight.containsKey(c))
			flyweight.put(c, new Symbol(c));
		return flyweight.get(c);
	}
}