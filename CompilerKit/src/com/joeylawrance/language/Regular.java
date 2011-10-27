package com.joeylawrance.language;

/**
 * Matches regular expressions using derivatives.
 * Reference:
 * "Derivatives of Regular expressions", by Janus Brzozowski
 */
public class Regular {
	public static Symbol symbol (char c) {
		return Symbol.symbol(c);
	}
	public static Alternation alternation (Parser ... parsers) {
		return Alternation.alternation(parsers);
	}
	public static Catenation catenation (Parser ... parsers) {
		return Catenation.catenation(parsers);
	}
	public static Parser string (String str) {
		if (str.length() == 1) return Symbol.symbol(str.charAt(0));
		Symbol[] symbols = new Symbol[str.length()];
		for (int i = 0; i < str.length(); i++) {
			symbols[i] = Symbol.symbol(str.charAt(i));
		}
		return Catenation.catenation(symbols);
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static PositiveClosure positiveClosure (Parser r) { return new PositiveClosure(r); }
	public static Times times (Parser r, int k) { return new Times(r, k); }
	public static Times times (Parser r, int lo, int hi) { return new Times(r, lo, hi); }
	public static CharacterRange lower() { return characterRange('a','z'); }
	public static CharacterRange characterRange(char start, char end) {
		return new CharacterRange(start, end);
	}
	public static Parser upper() { return characterRange('A','Z'); }
	public static Parser alpha() { return Alternation.alternation(lower(),upper()); }
	public static Parser digit() { return characterRange('0','9'); }
	public static Parser alnum() { return alternation(alpha(),digit()); }
	public static Parser parens(Parser parser) {
		return Catenation.catenation(Symbol.symbol('('),parser,Symbol.symbol(')'));
	}
	public static Parser optional(Parser parser) { return new Optional(parser); }
	public static Parser not(Parser parser) { return new Complement(parser); }
	public static Parser intersection(Parser ... parsers) { return Intersection.intersection(parsers); }
	public static Parser difference(Parser left, Parser right) {return new Difference(left, right); }
}
