package com.joeylawrance.language;

import com.joeylawrance.language.parsers.CharacterRange;
import com.joeylawrance.language.parsers.Difference;
import com.joeylawrance.language.parsers.Optional;
import com.joeylawrance.language.parsers.PositiveClosure;
import com.joeylawrance.language.parsers.Times;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.VisitorMap;

/**
 * TODO: implement group capture, forward/backward references, tokens, scanning
 * TODO: implement Terminal?
 * in theory, you'd have interfaces instead of abstract classes
 * need lazy set and it'd be nice to do lambda calc on side
 * parser ought to be an interface
 * the root of the nodes (e.g., regex, terminal, cfg) ought to be an object
 */
abstract class RegularVisitor<T> extends VisitorMap<Parser,T> {
	RegularVisitor() {
		this.register(PositiveClosure.class, new EquivalentVisitorEntry<PositiveClosure,T>());
		this.register(Times.class, new EquivalentVisitorEntry<Times,T>());
		this.register(CharacterRange.class, new EquivalentVisitorEntry<CharacterRange,T>());
		this.register(Optional.class, new EquivalentVisitorEntry<Optional,T>());
		this.register(Difference.class, new EquivalentVisitorEntry<Difference,T>());
	}	
}