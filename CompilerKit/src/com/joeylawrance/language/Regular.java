package com.joeylawrance.language;

/**
 * Matches regular expressions using derivatives.
 * Reference:
 * "Derivatives of Regular expressions", by Janus Brzozowski
 */
public class Regular {
	public static Symbol symbol (char c) {
		if (!Symbol.flyweight.containsKey(c))
			Symbol.flyweight.put(c, new Symbol(c));
		return Symbol.flyweight.get(c);
	}
	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
	public static Catenation catenation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Catenation (current, regexen[i]);
		}
		return (Catenation) current;
	}
	public static Catenation string (String str) {
		Symbol[] symbols = new Symbol[str.length()];
		for (int i = 0; i < str.length(); i++) {
			symbols[i] = symbol(str.charAt(i));
		}
		return catenation(symbols);
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static Parser positiveClosure (Parser r) {
		return new PositiveClosure(r);
	}
	public static Parser times (Parser r, int k) {
		return new Times(r, k);
	}
	public static Parser lower() { return characterRange('a','z'); }
	public static Parser characterRange(char start, char end) {
		return new CharacterRange(start, end);
	}
	public static Parser upper() { return characterRange('A','Z'); }
	public static Parser alpha() { return alternation(lower(),upper()); }
	public static Parser digit() { return characterRange('0','9'); }
	public static Parser parens(Parser parser) {
		return catenation(symbol('('),parser,symbol(')'));
	}
}
