package com.joeylawrance.language;

import java.util.HashMap;

class Symbol extends Expression {
	final char c;
	static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	public Symbol (char c) { this.c = c; }
}