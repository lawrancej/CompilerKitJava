package com.joeylawrance.language;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.CharacterRange;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.IdentityVisitor;
import com.joeylawrance.visitor.MemoizedVisitorEntry;
import com.joeylawrance.visitor.Visitor;

class RegularDerivativeVisitor extends RegularVisitor<Parser> implements DerivativeVisitor<Parser,Parser> {
	public char c;
	Visitor<Parser,Parser> nullable;
	public RegularDerivativeVisitor(Visitor<Parser,Parser> nullable) {
		super();
		this.nullable = nullable;
		this.register(EmptySet.class, new IdentityVisitor<Parser,EmptySet>());
		this.register(EmptyString.class, new DefaultVisitorEntry<Parser,EmptyString,Parser>() {
			public Parser visit(EmptyString node) {
				return EmptySet.emptySet;
			}
		});
		this.register(Symbol.class, new DefaultVisitorEntry<Parser,Symbol,Parser>() {
			public Parser visit(Symbol symbol) {
				return (symbol.c == ((RegularDerivativeVisitor)getParent()).getSymbol()) ? EmptyString.emptyString : EmptySet.emptySet;
			}
		});
		this.register(Alternation.class, new MemoizedVisitorEntry<Parser,Alternation,Parser>(new DefaultVisitorEntry<Parser,Alternation,Parser>() {
			public Parser visit(Alternation alternation) {
				return Alternation.build(getParent().visit(alternation.getLeft()),
						getParent().visit(alternation.getRight()));
			}
		}));
		this.register(Catenation.class, new MemoizedVisitorEntry<Parser,Catenation,Parser>(new DefaultVisitorEntry<Parser,Catenation,Parser>() {
			public Parser visit(Catenation catenation) {
				Parser left = getParent().visit(catenation.getLeft());
				Parser right = getParent().visit(catenation.getRight());
				Parser nulled = getNullable().visit(catenation.getLeft());
								
				return Alternation.build(Catenation.build(left, catenation.getRight()),
						Catenation.build(nulled, right));
			}
		}));
		this.register(KleeneClosure.class,new MemoizedVisitorEntry<Parser,KleeneClosure,Parser>(new DefaultVisitorEntry<Parser,KleeneClosure,Parser>() {
			public Parser visit(KleeneClosure kleeneClosure) {
				Parser left = getParent().visit(kleeneClosure.getNode());
				return Catenation.build(left, kleeneClosure);
			}			
		}));
		this.register(Complement.class, new MemoizedVisitorEntry<Parser,Complement,Parser>(new DefaultVisitorEntry<Parser,Complement,Parser>() {
			public Complement visit(Complement not) {
				return new Complement(getParent().visit(not.getNode()));
			}
		}));
		this.register(CharacterRange.class, new DefaultVisitorEntry<Parser,CharacterRange,Parser>() {
			public Parser visit(CharacterRange characterRange) {
				return (characterRange.getStart() <= c && c <= characterRange.getEnd()) ? EmptyString.emptyString : EmptySet.emptySet;
			}
		});
		this.register(Intersection.class, new MemoizedVisitorEntry<Parser,Intersection,Parser>(new DefaultVisitorEntry<Parser,Intersection,Parser>(){
			public Parser visit(Intersection intersection) {
				Parser left = getParent().visit(intersection.getLeft());
				Parser right = getParent().visit(intersection.getRight());
				if (left == right) return right;
				return new Intersection(left, right);
			}
		}));
	}
	@Override
	public Visitor<Parser, Parser> getNullable() {
		return nullable;
	}
	@Override
	public char getSymbol() {
		return c;
	}
	@Override
	public void setSymbol(char c) {
		this.c = c;
		setState(new Character(c));
	}
}