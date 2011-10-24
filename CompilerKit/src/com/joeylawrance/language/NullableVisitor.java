package com.joeylawrance.language;

class NullableVisitor extends RegularVisitor<Parser> {
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return emptyString; }
	public Parser visit(Symbol symbol)           { return Regular.emptySet; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.left);
		Parser right = visit(alternation.right);
		if (left == Regular.emptyString || right == Regular.emptyString)
			return Regular.emptyString;
		else return Regular.emptySet;
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		if (left == Regular.emptySet || right == Regular.emptySet) {
			return Regular.emptySet;
		} else return Regular.emptyString;
	}
	public Parser visit(KleeneClosure kleeneClosure) { return Regular.emptyString; }
}