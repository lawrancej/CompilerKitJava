package com.joeylawrance.language;

class Times extends EquivalentExpression {
	int hi;
	int lo;
	public Times (Parser node, int hi) {
		this(node,hi,hi);
	}
	public Times (Parser node, int lo, int hi) {
		if (lo <= hi) {
			this.lo = lo;
			this.hi = hi;
		}
		else if (hi < lo) {
			this.lo = hi;
			this.hi = lo;
		}
		Parser[] parsers = new Parser[this.hi];
		int i;
		for (i = 0; i < this.lo; i++)
			parsers[i] = node;
		for ( ; i < this.hi; i++)
			parsers[i] = new Optional(node);
		this.equivalent = Regular.catenation(parsers);
		this.node = node;
	}
	public static void main(String[] args) {
		Times times = new Times(new Symbol('B'),5);
		DerivativeVisitor derivative = new DerivativeVisitor('B');
		System.out.println(CompactionVisitor.compactor.visit(derivative.visit(times)));
	}
}