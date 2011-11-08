package com.joeylawrance.language;

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
		this.register(EmptyString.class, new IdentityVisitor<Parser,EmptyString>());
		this.register(Symbol.class, new DefaultVisitorEntry<Parser,Symbol,Parser>() {
			public Parser visit(Symbol symbol) {
				return (symbol.c == ((RegularDerivativeVisitor)getParent()).getSymbol()) ? EmptyString.emptyString : EmptySet.emptySet;
			}
		});
		this.register(Alternation.class, new MemoizedVisitorEntry<Parser,Alternation,Parser>(new DefaultVisitorEntry<Parser,Alternation,Parser>() {
			public Parser visit(Alternation alternation) {
				Parser left = getParent().visit(alternation.left);
				Parser right = getParent().visit(alternation.right);
				if (left == EmptySet.emptySet) return right;
				else if (right == EmptySet.emptySet) return left;
				else if (left == right) return left;
				else return new Alternation(left, right);
				// return new Alternation(visit(alternation.left), visit(alternation.right));			
			}
		}));
		this.register(Catenation.class, new MemoizedVisitorEntry<Parser,Catenation,Parser>(new DefaultVisitorEntry<Parser,Catenation,Parser>() {
			public Parser visit(Catenation catenation) {
				Parser left = getParent().visit(catenation.left);
				Parser right = getParent().visit(catenation.right);
				Parser nulled = ((RegularDerivativeVisitor)getParent()).getNullable().visit(catenation.left);
				Parser alternationLeft;
				Parser alternationRight;
				
				if (left == EmptyString.emptyString)
					alternationLeft = catenation.right;
				else if (left == EmptySet.emptySet)
					alternationLeft = EmptySet.emptySet;
				else
					alternationLeft = new Catenation(left, catenation.right);
				
				if (nulled == EmptyString.emptyString)
					alternationRight = right;
				else if (nulled == EmptySet.emptySet)
					alternationRight = EmptySet.emptySet;
				else
					alternationRight = new Catenation(nulled, right);
				
				if (alternationLeft == EmptySet.emptySet) return alternationRight;
				else if (alternationRight == EmptySet.emptySet) return alternationLeft;
				else return new Alternation(alternationLeft, alternationRight);
//				return new Alternation (
//						new Catenation (visit(catenation.left), catenation.right),
//						new Catenation (NullableVisitor.nullable.visit(catenation.left),visit(catenation.right)));
			}
		}));
		this.register(KleeneClosure.class,new MemoizedVisitorEntry<Parser,KleeneClosure,Parser>(new DefaultVisitorEntry<Parser,KleeneClosure,Parser>() {
			public Parser visit(KleeneClosure kleeneClosure) {
				Parser left = getParent().visit(kleeneClosure.node);
				if (left == EmptyString.emptyString) return kleeneClosure;
				else if (left == EmptySet.emptySet) return EmptySet.emptySet;
				return new Catenation (left, kleeneClosure);
			}			
		}));
		this.register(Complement.class, new MemoizedVisitorEntry<Parser,Complement,Parser>(new DefaultVisitorEntry<Parser,Complement,Parser>() {
			public Complement visit(Complement not) {
				return new Complement(getParent().visit(not.node));
			}
		}));
		this.register(CharacterRange.class, new MemoizedVisitorEntry<Parser,CharacterRange,Parser>(new DefaultVisitorEntry<Parser,CharacterRange,Parser>() {
			public Parser visit(CharacterRange characterRange) {
				return (characterRange.start <= c && c <= characterRange.end) ? EmptyString.emptyString : EmptySet.emptySet;
			}
		}));
		this.register(Intersection.class, new MemoizedVisitorEntry<Parser,Intersection,Parser>(new DefaultVisitorEntry<Parser,Intersection,Parser>(){
			public Parser visit(Intersection intersection) {
				Parser left = getParent().visit(intersection.left);
				Parser right = getParent().visit(intersection.right);
				if (left == right) return right;
				return new Intersection(left, right);
			}
		}));
	}
	@Override
	public Visitor<Parser, Parser> getNullable() {
		return nullable;
	}
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return EmptySet.emptySet; }
	public Parser visit(Symbol symbol)           { return (symbol.c == c) ? EmptyString.emptyString : EmptySet.emptySet; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.left);
		Parser right = visit(alternation.right);
		if (left == EmptySet.emptySet) return right;
		else if (right == EmptySet.emptySet) return left;
		else if (left == right) return left;
		else return new Alternation(left, right);
		// return new Alternation(visit(alternation.left), visit(alternation.right));
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		Parser nulled = this.getNullable().visit(catenation.left);
		Parser alternationLeft;
		Parser alternationRight;
		
		if (left == EmptyString.emptyString)
			alternationLeft = catenation.right;
		else if (left == EmptySet.emptySet)
			alternationLeft = EmptySet.emptySet;
		else
			alternationLeft = new Catenation(left, catenation.right);
		
		if (nulled == EmptyString.emptyString)
			alternationRight = right;
		else if (nulled == EmptySet.emptySet)
			alternationRight = EmptySet.emptySet;
		else
			alternationRight = new Catenation(nulled, right);
		
		if (alternationLeft == EmptySet.emptySet) return alternationRight;
		else if (alternationRight == EmptySet.emptySet) return alternationLeft;
		else return new Alternation(alternationLeft, alternationRight);
//		return new Alternation (
//				new Catenation (visit(catenation.left), catenation.right),
//				new Catenation (NullableVisitor.nullable.visit(catenation.left),visit(catenation.right)));
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		Parser left = visit(kleeneClosure.node);
		if (left == EmptyString.emptyString) return kleeneClosure;
		else if (left == EmptySet.emptySet) return EmptySet.emptySet;
		return new Catenation (left, kleeneClosure);
	}
	public Complement visit(Complement not) {
		return new Complement(visit(not.node));
	}
	public Parser visit(CharacterRange characterRange) {
		return (characterRange.start <= c && c <= characterRange.end) ? EmptyString.emptyString : EmptySet.emptySet;
	}
	public Parser visit(Intersection intersection) {
		Parser left = visit(intersection.left);
		Parser right = visit(intersection.right);
		if (left == right) return right;
		return new Intersection(left, right);
	}
	@Override
	public char getSymbol() {
		// TODO Auto-generated method stub
		return c;
	}
	@Override
	public void setSymbol(char c) {
		// TODO Auto-generated method stub
		this.c = c;
		setState(new Character(c));
	}
}