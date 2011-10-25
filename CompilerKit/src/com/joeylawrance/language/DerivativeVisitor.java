package com.joeylawrance.language;

class DerivativeVisitor extends RegularVisitor<Parser> {
	public char c;
	public DerivativeVisitor() {}
	public DerivativeVisitor(char c)             { this.c = c; }
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return EmptySet.emptySet; }
	public Parser visit(Symbol symbol)           { return (symbol.c == c) ? EmptyString.emptyString : EmptySet.emptySet; }
	public Parser visit(Alternation alternation) {
		return new Alternation(visit(alternation.left), visit(alternation.right));
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		return new Alternation (
				new Catenation (visit(catenation.left), catenation.right),
				new Catenation (NullableVisitor.nullable.visit(catenation.left),visit(catenation.right)));
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		return new Catenation (visit(kleeneClosure.node), kleeneClosure);
	}
	public Parser visit(CharacterRange characterRange) {
		return (characterRange.start < c && c < characterRange.end) ? EmptyString.emptyString : EmptySet.emptySet;
	}
}