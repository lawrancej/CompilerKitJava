package com.joeylawrance.language;

import com.joeylawrance.visitor.Visitor;

class RegularDerivativeVisitor extends RegularVisitor<Parser> implements DerivativeVisitor<Object,Parser> {
	// TODO: use memoization for performance improvements?
	public char c;
	Visitor<Object,Parser> nullable;
	public RegularDerivativeVisitor(Visitor<Object,Parser> nullable) {this.nullable = nullable;}
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
	public Complement visit(Complement not) {
//		Parser notNode = visit(not.node);
//		if (notNode == EmptyString)
		return new Complement(visit(not.node));
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		Parser left = visit(kleeneClosure.node);
		if (left == EmptyString.emptyString) return kleeneClosure;
		else if (left == EmptySet.emptySet) return EmptySet.emptySet;
		return new Catenation (left, kleeneClosure);
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
	}
	@Override
	public Visitor<Object, Parser> getNullable() {
		return nullable;
	}
}