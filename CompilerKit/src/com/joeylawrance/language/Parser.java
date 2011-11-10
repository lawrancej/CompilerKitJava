package com.joeylawrance.language;

import com.joeylawrance.visitor.Visitor;

// TODO: Allow recognize to work on abitrary streams? make generic? this would also affect derivative visitor
// TODO: move node classes into own package and use public interfaces everywhere
/**
 * Abstract superclass for all parser combinator classes.
 * Uses the Composite and Visitor design patterns.
 */
public interface Parser {
	public Visitor<Parser,String> getPrinter();
	public DerivativeVisitor<Parser,Parser> getDerivative();
}