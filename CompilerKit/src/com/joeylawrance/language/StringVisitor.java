package com.joeylawrance.language;

// FIXME: unescape escape sequences
class StringVisitor extends RegularVisitor<String> {
	public String visit(EmptySet emptySet)       { return "{}"; }
	public String visit(EmptyString emptyString) { return "λ"; }
	public String visit(Symbol symbol)           { return "" + symbol.c; }
	public String visit(Alternation alternation) {
		return visit(alternation.left) + "|" + visit(alternation.right);
	}
	public String visit(Catenation catenation) { // TODO:  maybe materialize new classes: {m,n}, character class, .?
		StringBuilder sb = new StringBuilder();
		if (catenation.left instanceof Alternation)
			sb.append("(" + visit(catenation.left) + ")");
		else
			sb.append(visit(catenation.left));
		if (catenation.right instanceof Alternation)
			sb.append("(" + visit(catenation.right) + ")");
		else
			sb.append(visit(catenation.right));
		return sb.toString();
	}
	public String visit(KleeneClosure kleeneClosure) {
		return "(" + visit(kleeneClosure.node) + ")*";
	}
	public String visit(PositiveClosure positiveClosure) {
		return "(" + visit(positiveClosure.node) + ")+";
	}
	public String visit(Times times) {
		if (times.lo != times.hi)
			return "(" + visit(times.node) + "){" + times.lo + "," + times.hi + "}";
		else return "(" + visit(times.node) + "){" + times.hi + "}";
	}
	public String visit(CharacterRange characterRange) {
		return "[" + characterRange.start + "-" + characterRange.end + "]";
	}
	public String visit(Optional option) {
		return "(" + visit(option.node) + ")?";
	}
	@Override
	public String visit(Complement not) {
		return "(" + visit(not.node) + ")'";
	}
}