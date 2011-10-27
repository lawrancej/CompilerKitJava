package com.joeylawrance.language;

// TODO: Extend to allow multiple ranges, negation, single characters
// TODO: Make generic in anticipation of parsing on arbitrary non-character streams?
class CharacterRange extends EquivalentExpression {
	char start, end;
	public CharacterRange(char start, char end) {
		if (start < end) {
			this.start = start; this.end = end;
		} else {
			this.start = end; this.end = start;
		}
		Symbol[] symbol = new Symbol[this.end + 1 - this.start];
		for (char c = this.start; c <= this.end; c++) {
			symbol[c-this.start] = Symbol.symbol(c);
		}
		this.node = this;
		this.equivalent = Alternation.alternation(symbol);
	}
}
