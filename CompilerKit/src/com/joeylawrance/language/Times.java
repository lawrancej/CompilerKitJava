package com.joeylawrance.language;

class Times extends EquivalentExpression {
	int k;
	public Times (Parser node, int k) {
		Parser[] parsers = new Parser[k];
		for (int i = 0; i < k; i++)
			parsers[i] = node;
		this.equivalent = Regular.catenation(parsers);
		this.node = node;
		this.k = k;
	}
}