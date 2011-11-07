package com.joeylawrance.language;

import com.joeylawrance.visitor.VisitorMap;

/**
 * TODO: implement group capture, forward/backward references, tokens, scanning
 * TODO: implement Terminal?
 * in theory, you'd have interfaces instead of abstract classes
 * in theory, you could split this class into primitive and extended visitors
 * need lazy set and it'd be nice to do lambda calc on side
 * parser ought to be an interface
 * the root of the nodes (e.g., regex, terminal, cfg) ought to be an object
 */

abstract class RegularVisitor<T> extends VisitorMap<Parser,T> {
	// Primitive regular expressions
	public abstract T visit(EmptySet emptySet);
	public abstract T visit(EmptyString emptyString);
	public abstract T visit(Symbol symbol);
	public abstract T visit(Alternation alternation); // AKA: Union
	public abstract T visit(Catenation catenation);
	public abstract T visit(KleeneClosure kleeneClosure);
	public abstract T visit(Complement complement); // AKA: negation, inverse
	public abstract T visit(Intersection intersection);
	
	// Regular expression extensions.
	// By default, use the equivalent expressions defined in terms of the primitives above
	public T visit(PositiveClosure positiveClosure) { return visit(positiveClosure.getEquivalent()); }
	public T visit(Times times) { return visit(times.getEquivalent()); }
	public T visit(CharacterRange characterRange) { return visit(characterRange.getEquivalent()); }
	public T visit(Optional optional) { return visit(optional.getEquivalent()); }
	public T visit(Difference difference) { return visit(difference.equivalent); }
}