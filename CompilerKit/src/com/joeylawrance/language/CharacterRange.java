package com.joeylawrance.language;

public class CharacterRange extends EquivalentExpression {
	char start, end;
	public CharacterRange(char start, char end) {
		if (start < end) {
			this.start = start; this.end = end;
		} else {
			this.start = end; this.end = start;
		}
		Symbol[] symbol = new Symbol[this.end + 1 - this.start];
		for (char c = this.start; c <= this.end; c++) {
			symbol[c-this.start] = Regular.symbol(c);
		}
		this.node = this;
		this.equivalent = Regular.alternation(symbol);
	}
}
