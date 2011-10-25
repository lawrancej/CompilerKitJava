package com.joeylawrance.language;

class CompactionVisitor extends RegularVisitor<Parser> {
	static final CompactionVisitor compactor = new CompactionVisitor();
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return emptyString; }
	public Parser visit(Symbol symbol)           { return symbol; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.left);
		Parser right = visit(alternation.right);
		if (left == EmptySet.emptySet) return right;
		else if (right == EmptySet.emptySet) return left;
		else return new Alternation(left, right);
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.left);
		Parser right = visit(catenation.right);
		if (left == EmptyString.emptyString) return right;
		else if (left == EmptySet.emptySet) return EmptySet.emptySet;
		else return new Catenation(left, right);
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		return new KleeneClosure(visit(kleeneClosure.node));
	}
}