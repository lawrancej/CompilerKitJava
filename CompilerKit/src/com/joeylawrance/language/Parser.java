package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;

// TODO: Allow recognize to work on abitrary streams? make generic? this would also affect derivative visitor
/**
 * Abstract superclass for all parser combinator classes.
 * Uses the Composite and Visitor design patterns.
 */
public abstract class Parser {
	public abstract ReflectiveVisitor<String> getPrinter();
	public abstract DerivativeVisitor<Object,Parser> getDerivative();

	public String toString () {
		return getPrinter().visit(this);
	}
	public boolean recognize (CharSequence str) { // Move outside, because of visibility issues and besides, this wont work with Regular and Context-Free
		Parser parser = this;
		DerivativeVisitor<Object,Parser> derivative = getDerivative();
		for (int i = 0; i < str.length(); i++) {
			derivative.setSymbol(str.charAt(i));
			System.out.print("Symbol:" + derivative.getSymbol());
			System.out.println("before: " + parser);
			parser = derivative.visit(parser);
			System.out.println("after: " + parser);
			if (parser == EmptySet.emptySet) break;
		}
		return derivative.getNullable().visit(parser) == EmptyString.emptyString;
	}
}