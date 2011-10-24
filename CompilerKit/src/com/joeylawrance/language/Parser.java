package com.joeylawrance.language;

import com.joeylawrance.language.Regular.DerivativeVisitor;
import com.joeylawrance.visitor.ReflectiveVisitor;

/**
 * Abstract superclass for all parser combinator classes.
 * Uses the Composite and Visitor design patterns.
 */
public abstract class Parser { // Probably should move out of regular
	public abstract ReflectiveVisitor<String> getPrinter();
	public String toString () {
		return getPrinter().visit(this);
	}
	public boolean recognize (CharSequence str) { // Move outside, because of visibility issues and besides, this wont work with Regular and Context-Free
		Parser parser = this;
		DerivativeVisitor derivative = new DerivativeVisitor();
		for (int i = 0; i < str.length(); i++) {
			derivative.c = str.charAt(i);
			parser = derivative.visit(parser);
			parser = Regular.compactor.visit(parser);
		}
		return Regular.nullable.visit(parser) == Regular.emptyString;
	}
}