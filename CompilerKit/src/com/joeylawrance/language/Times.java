package com.joeylawrance.language;

class Times extends EquivalentExpression {
	int hi;
	int lo;
	public Times (Parser node, int hi) {
		this(node,hi,hi);
	}
	public Times (Parser node, int lo, int hi) {
		this.lo = (lo <= hi) ? lo : hi;
		this.hi = (lo <= hi) ? hi : lo;
		
		Parser[] parsers = new Parser[this.hi];
		int i;
		for (i = 0; i < this.lo; i++)
			parsers[i] = node;
		for ( ; i < this.hi; i++)
			parsers[i] = new Optional(node);
		this.setEquivalent(Catenation.catenation(parsers));
		this.node = node;
	}
}