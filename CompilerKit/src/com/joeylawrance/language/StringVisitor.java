package com.joeylawrance.language;

import com.joeylawrance.visitor.DefaultVisitorEntry;

// FIXME: unescape escape sequences
class StringVisitor extends RegularVisitor<String> {
	StringVisitor() {
		this.register(EmptySet.class, new DefaultVisitorEntry<Parser,EmptySet,String>() {
			public String visit(EmptySet node) { return "{}"; }
		});
		this.register(EmptyString.class, new DefaultVisitorEntry<Parser,EmptyString,String>() {
			public String visit(EmptyString node) { return "λ"; }
		});
		this.register(Symbol.class, new DefaultVisitorEntry<Parser,Symbol,String>() {
			public String visit(Symbol symbol) { return "" + symbol.c; }
		});
		this.register(Alternation.class, new DefaultVisitorEntry<Parser,Alternation,String>() {
			public String visit(Alternation alternation) {
				return getParent().visit(alternation.left) + "|" + getParent().visit(alternation.right);
			}
		});
		this.register(Catenation.class, new DefaultVisitorEntry<Parser,Catenation,String>() {
			public String visit(Catenation catenation) {
				StringBuilder sb = new StringBuilder();
				if (catenation.left instanceof Alternation)
					sb.append("(" + getParent().visit(catenation.left) + ")");
				else
					sb.append(getParent().visit(catenation.left));
				if (catenation.right instanceof Alternation)
					sb.append("(" + getParent().visit(catenation.right) + ")");
				else
					sb.append(getParent().visit(catenation.right));
				return sb.toString();
			}
		});
		this.register(KleeneClosure.class, new DefaultVisitorEntry<Parser,KleeneClosure,String>() {
			public String visit(KleeneClosure kleeneClosure) {
				return "(" + getParent().visit(kleeneClosure.node) + ")*";
			}
		});
		this.register(PositiveClosure.class, new DefaultVisitorEntry<Parser,PositiveClosure,String>() {
			public String visit(PositiveClosure positiveClosure) {
				return "(" + getParent().visit(positiveClosure.node) + ")+";
			}
		});
	}
	public String visit(EmptySet emptySet)       { return "{}"; }
	public String visit(EmptyString emptyString) { return "λ"; }
	public String visit(Symbol symbol)           { return "" + symbol.c; }
	public String visit(Alternation alternation) {
		return visit(alternation.left) + "|" + visit(alternation.right);
	}
	public String visit(Catenation catenation) {
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
	public String visit(Complement not) {
		return "(" + visit(not.node) + ")'";
	}
	public String visit(Intersection intersection) {
		StringBuilder sb = new StringBuilder();
		if (intersection.left instanceof Alternation)
			sb.append("(" + visit(intersection.left) + ")");
		else
			sb.append(visit(intersection.left));
		sb.append("&");
		if (intersection.right instanceof Alternation)
			sb.append("(" + visit(intersection.right) + ")");
		else
			sb.append(visit(intersection.right));
		return sb.toString();
	}
	public String visit(Difference difference) {
		StringBuilder sb = new StringBuilder();
		if (difference.left instanceof Alternation)
			sb.append("(" + visit(difference.left) + ")");
		else
			sb.append(visit(difference.left));
		sb.append("-");
		if (difference.right instanceof Alternation)
			sb.append("(" + visit(difference.right) + ")");
		else
			sb.append(visit(difference.right));
		return sb.toString();
	}
}