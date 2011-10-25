package com.joeylawrance.language;

import com.joeylawrance.visitor.ReflectiveVisitor;

/**
 * TODO: implement group capture, forward/backward references, tokens, scanning
 * TODO: boolean operations on regexes (e.g., not, and)
 * TODO: reflective visitor is slow :( maybe use enummap?
 * in theory, you'd have interfaces instead of abstract classes
 * in theory, you could split this class into primitive and extended visitors
 * need lazy set and it'd be nice to do lambda calc on side
 */

abstract class RegularVisitor<T> extends ReflectiveVisitor<T> {
	// Primitive regular expressions
	public abstract T visit(EmptySet emptySet);
	public abstract T visit(EmptyString emptyString);
	public abstract T visit(Symbol symbol);
	public abstract T visit(Alternation alternation);
	public abstract T visit(Catenation catenation);
	public abstract T visit(KleeneClosure kleeneClosure);
	
	// Regular expression extensions.
	// By default, use the equivalent expressions defined in terms of the primitives above
	public T visit(PositiveClosure positiveClosure) { return visit(positiveClosure.equivalent); }
	public T visit(Times times) { return visit(times.equivalent); }
	public T visit(CharacterRange characterRange) { return visit(characterRange.equivalent); }
	public T visit(Optional optional) { return visit(optional.equivalent); }
}