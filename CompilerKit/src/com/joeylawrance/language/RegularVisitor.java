package com.joeylawrance.language;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.CharacterRange;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.Difference;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Optional;
import com.joeylawrance.language.parsers.PositiveClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.language.parsers.Times;
import com.joeylawrance.visitor.DefaultVisitorEntry;
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
// TODO: remove the extra visit methods
abstract class RegularVisitor<T> extends VisitorMap<Parser,T> {
	RegularVisitor() {
		this.register(PositiveClosure.class, new DefaultVisitorEntry<Parser,PositiveClosure,T>() {
			public T visit(PositiveClosure positiveClosure) { return getParent().visit(positiveClosure.getEquivalent()); }			
		});
		this.register(Times.class, new DefaultVisitorEntry<Parser,Times,T>() {
			public T visit(Times times) { return getParent().visit(times.getEquivalent()); }			
		});
		this.register(CharacterRange.class, new DefaultVisitorEntry<Parser,CharacterRange,T>() {
			public T visit(CharacterRange characterRange) { return getParent().visit(characterRange.getEquivalent()); }			
		});
		this.register(Optional.class, new DefaultVisitorEntry<Parser,Optional,T>() {
			public T visit(Optional optional) { return getParent().visit(optional.getEquivalent()); }			
		});
		this.register(Difference.class, new DefaultVisitorEntry<Parser,Difference,T>() {
			public T visit(Difference difference) { return getParent().visit(difference.getEquivalent()); }			
		});
	}	
}