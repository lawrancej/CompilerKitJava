package com.joeylawrance.language.parsers;

// TODO: Extend to allow multiple ranges, negation, single characters
// TODO: Make generic in anticipation of parsing on arbitrary non-character streams?
public class CharacterRange extends EquivalentExpressionImpl {
	private char start;
	private char end;
	public CharacterRange(char start, char end) {
		this.start = (start < end) ? start : end;
		this.end = (start < end) ? end : start;
		Symbol[] symbol = new Symbol[this.end + 1 - this.start];
		for (char c = this.start; c <= this.end; c++) {
			symbol[c-this.start] = Symbol.symbol(c);
		}
		this.setEquivalent(Alternation.alternation(symbol));
	}
	public char getStart() {
		return start;
	}
	public char getEnd() {
		return end;
	}
}
