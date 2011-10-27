package com.joeylawrance.language;

class NullableVisitor extends RegularVisitor<Parser> {
	static final NullableVisitor nullable = new NullableVisitor();
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return emptyString; }
	public Parser visit(Symbol symbol)           { return EmptySet.emptySet; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.left);
		Parser right = visit(alternation.right);
		if (left == EmptyString.emptyString || right == EmptyString.emptyString)
			return EmptyString.emptyString;
		else return EmptySet.emptySet;
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		if (left == EmptySet.emptySet || right == EmptySet.emptySet) {
			return EmptySet.emptySet;
		} else return EmptyString.emptyString;
	}
	public Parser visit(KleeneClosure kleeneClosure) { return EmptyString.emptyString; }
	public Parser visit(Complement complement) {
		if (visit(complement.node) == EmptySet.emptySet)
			return EmptyString.emptyString;
		else return EmptySet.emptySet;
	}
	public Parser visit(Intersection intersection) {
		Parser left = visit(intersection.left);
		Parser right = visit(intersection.right);
		if (left == EmptySet.emptySet || right == EmptySet.emptySet)
			return EmptySet.emptySet;
		else return EmptyString.emptyString;
	}
}