package com.joeylawrance.language;

import java.util.HashMap;

class Symbol extends Expression {
	final char c;
	static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	private Symbol (char c) { this.c = c; }
	public static Symbol symbol (char c) {
		if (!flyweight.containsKey(c))
			flyweight.put(c, new Symbol(c));
		return flyweight.get(c);
	}
}