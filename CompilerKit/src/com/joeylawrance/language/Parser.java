package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;

// TODO: Allow recognize to work on abitrary streams? make generic? this would also affect derivative visitor
/**
 * Abstract superclass for all parser combinator classes.
 * Uses the Composite and Visitor design patterns.
 */
public abstract class Parser {
	public abstract ReflectiveVisitor<String> getPrinter();
	public abstract ReflectiveVisitor<Parser> getDerivative();
	public String toString () {
		return getPrinter().visit(this);
	}
	public boolean recognize (CharSequence str) { // Move outside, because of visibility issues and besides, this wont work with Regular and Context-Free
		Parser parser = this;
		DerivativeVisitor derivative = new DerivativeVisitor();
		for (int i = 0; i < str.length(); i++) {
			derivative.c = str.charAt(i);
//			System.out.print(derivative.c);
//			System.out.println(parser);
			parser = derivative.visit(parser);
//			System.out.println(parser);
			if (parser == EmptySet.emptySet) break;
		}
		return NullableVisitor.nullable.visit(parser) == EmptyString.emptyString;
	}
}