package com.joeylawrance.language;

import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.VisitorMap;

// FIXME: unescape escape sequences
class RegularStringVisitor extends VisitorMap<Parser,String> {
	RegularStringVisitor() {
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
		this.register(Times.class, new DefaultVisitorEntry<Parser,Times,String>() {
			public String visit(Times times) {
				if (times.lo != times.hi)
					return "(" + getParent().visit(times.node) + "){" + times.lo + "," + times.hi + "}";
				else return "(" + getParent().visit(times.node) + "){" + times.hi + "}";
			}
		});
		this.register(CharacterRange.class, new DefaultVisitorEntry<Parser,CharacterRange,String>() {
			public String visit(CharacterRange characterRange) {
				return "[" + characterRange.start + "-" + characterRange.end + "]";
			}
		});
		this.register(Optional.class, new DefaultVisitorEntry<Parser,Optional,String>() {
			public String visit(Optional option) {
				return "(" + getParent().visit(option.node) + ")?";
			}
		});
		this.register(Complement.class, new DefaultVisitorEntry<Parser,Complement,String>() {
			public String visit(Complement not) {
				return "(" + getParent().visit(not.node) + ")'";
			}
		});
		this.register(Intersection.class, new DefaultVisitorEntry<Parser,Intersection,String>() {
			public String visit(Intersection intersection) {
				StringBuilder sb = new StringBuilder();
				if (intersection.left instanceof Alternation)
					sb.append("(" + getParent().visit(intersection.left) + ")");
				else
					sb.append(getParent().visit(intersection.left));
				sb.append("&");
				if (intersection.right instanceof Alternation)
					sb.append("(" + getParent().visit(intersection.right) + ")");
				else
					sb.append(getParent().visit(intersection.right));
				return sb.toString();
			}
		});
		this.register(Difference.class, new DefaultVisitorEntry<Parser,Difference,String>() {
			public String visit(Difference difference) {
				StringBuilder sb = new StringBuilder();
				if (difference.left instanceof Alternation)
					sb.append("(" + getParent().visit(difference.left) + ")");
				else
					sb.append(getParent().visit(difference.left));
				sb.append("-");
				if (difference.right instanceof Alternation)
					sb.append("(" + getParent().visit(difference.right) + ")");
				else
					sb.append(getParent().visit(difference.right));
				return sb.toString();
			}
		});
	}
}