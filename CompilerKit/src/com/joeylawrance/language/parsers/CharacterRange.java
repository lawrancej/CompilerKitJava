package com.joeylawrance.language.parsers;


// TODO: Extend to allow multiple ranges, negation, single characters
// TODO: Make generic in anticipation of parsing on arbitrary non-character streams?
public class CharacterRange extends EquivalentExpression {
	private char start;
	private char end;
	public CharacterRange(char start, char end) {
		this.start = (start < end) ? start : end;
		this.end = (start < end) ? end : start;
		Symbol[] symbol = new Symbol[this.getEnd() + 1 - this.getStart()];
		for (char c = this.getStart(); c <= this.getEnd(); c++) {
			symbol[c-this.getStart()] = Symbol.symbol(c);
		}
		this.setNode(this);
		this.setEquivalent(Alternation.alternation(symbol));
	}
	public char getStart() {
		return start;
	}
	public char getEnd() {
		return end;
	}
}
