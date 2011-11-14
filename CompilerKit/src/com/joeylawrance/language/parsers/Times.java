package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public class Times extends EquivalentExpressionImpl implements UnaryOperator {
	private int hi;
	private int lo;
	private Parser node;
	public Times (Parser node, int hi) {
		this(node,hi,hi);
	}
	public Times (Parser node, int lo, int hi) {
		this.lo = (lo <= hi) ? lo : hi;
		this.hi = (lo <= hi) ? hi : lo;
		
		Parser[] parsers = new Parser[this.getHi()];
		int i;
		for (i = 0; i < this.getLo(); i++)
			parsers[i] = node;
		for ( ; i < this.getHi(); i++)
			parsers[i] = Alternation.build(node,EmptyString.emptyString);
		this.setEquivalent(Catenation.catenation(parsers));
		this.node = node;
	}
	public int getHi() {
		return hi;
	}
	public int getLo() {
		return lo;
	}
	@Override
	public Parser getNode() {
		return node;
	}
}