package com.joeylawrance.language;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.IdentityVisitor;

class RegularNullableVisitor extends RegularVisitor<Parser> {
	private static RegularNullableVisitor nullable = new RegularNullableVisitor();
	public static RegularNullableVisitor getInstance() { return nullable; }
	RegularNullableVisitor() {
		super();
		this.register(EmptySet.class, new IdentityVisitor<Parser,EmptySet>());
		this.register(EmptyString.class, new IdentityVisitor<Parser,EmptyString>());
		this.register(Symbol.class, new DefaultVisitorEntry<Parser,Symbol,Parser>() {
			public Parser visit(Symbol symbol) {
				return EmptySet.emptySet;
			}			
		});
		this.register(Alternation.class, new DefaultVisitorEntry<Parser, Alternation, Parser>() {
			public Parser visit(Alternation alternation) {
				Parser left = getParent().visit(alternation.getLeft());
				Parser right = getParent().visit(alternation.getRight());
				if (left == EmptyString.emptyString || right == EmptyString.emptyString)
					return EmptyString.emptyString;
				else return EmptySet.emptySet;
			}			
		});
		this.register(Catenation.class, new DefaultVisitorEntry<Parser, Catenation, Parser>() {
			public Parser visit(Catenation catenation) {
				Parser left = getParent().visit(catenation.getLeft());
				Parser right = getParent().visit(catenation.getRight());
				if (left == EmptySet.emptySet || right == EmptySet.emptySet) {
					return EmptySet.emptySet;
				} else return EmptyString.emptyString;
			}			
		});
		this.register(KleeneClosure.class, new DefaultVisitorEntry<Parser, KleeneClosure, Parser>() {
			public Parser visit(KleeneClosure kleeneClosure) { return EmptyString.emptyString; }
		});
		this.register(Complement.class, new DefaultVisitorEntry<Parser,Complement,Parser>() {
			public Parser visit(Complement complement) {
				if (getParent().visit(complement.getNode()) == EmptySet.emptySet)
					return EmptyString.emptyString;
				else return EmptySet.emptySet;
			}			
		});
		this.register(Intersection.class, new DefaultVisitorEntry<Parser,Intersection,Parser>() {
			public Parser visit(Intersection intersection) {
				Parser left = getParent().visit(intersection.getLeft());
				Parser right = getParent().visit(intersection.getRight());
				if (left == EmptySet.emptySet || right == EmptySet.emptySet)
					return EmptySet.emptySet;
				else return EmptyString.emptyString;
			}
		});
	}
}