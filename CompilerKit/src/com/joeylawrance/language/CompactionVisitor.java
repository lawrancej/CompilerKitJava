package com.joeylawrance.language;

class CompactionVisitor extends RegularVisitor<Parser> {
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return emptyString; }
	public Parser visit(Symbol symbol)           { return symbol; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.left);
		Parser right = visit(alternation.right);
		if (left == Regular.emptySet) return right;
		else if (right == Regular.emptySet) return left;
		else return new Alternation(left, right);
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		if (left == Regular.emptyString) return right;
		else if (left == Regular.emptySet) return Regular.emptySet;
		else return new Catenation(left, right);
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		return new KleeneClosure(visit(kleeneClosure.node));
	}
}