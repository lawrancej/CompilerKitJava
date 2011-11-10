package com.joeylawrance.language;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.CharacterRange;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.Difference;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Optional;
import com.joeylawrance.language.parsers.PositiveClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.language.parsers.Times;
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
				return getParent().visit(alternation.getLeft()) + "|" + getParent().visit(alternation.getRight());
			}
		});
		this.register(Catenation.class, new DefaultVisitorEntry<Parser,Catenation,String>() {
			public String visit(Catenation catenation) {
				StringBuilder sb = new StringBuilder();
				if (catenation.getLeft() instanceof Alternation)
					sb.append("(" + getParent().visit(catenation.getLeft()) + ")");
				else
					sb.append(getParent().visit(catenation.getLeft()));
				if (catenation.getRight() instanceof Alternation)
					sb.append("(" + getParent().visit(catenation.getRight()) + ")");
				else
					sb.append(getParent().visit(catenation.getRight()));
				return sb.toString();
			}
		});
		this.register(KleeneClosure.class, new DefaultVisitorEntry<Parser,KleeneClosure,String>() {
			public String visit(KleeneClosure kleeneClosure) {
				return "(" + getParent().visit(kleeneClosure.getNode()) + ")*";
			}
		});
		this.register(PositiveClosure.class, new DefaultVisitorEntry<Parser,PositiveClosure,String>() {
			public String visit(PositiveClosure positiveClosure) {
				return "(" + getParent().visit(positiveClosure.getNode()) + ")+";
			}
		});
		this.register(Times.class, new DefaultVisitorEntry<Parser,Times,String>() {
			public String visit(Times times) {
				if (times.getLo() != times.getHi())
					return "(" + getParent().visit(times.getNode()) + "){" + times.getLo() + "," + times.getHi() + "}";
				else return "(" + getParent().visit(times.getNode()) + "){" + times.getHi() + "}";
			}
		});
		this.register(CharacterRange.class, new DefaultVisitorEntry<Parser,CharacterRange,String>() {
			public String visit(CharacterRange characterRange) {
				return "[" + characterRange.getStart() + "-" + characterRange.getEnd() + "]";
			}
		});
		this.register(Optional.class, new DefaultVisitorEntry<Parser,Optional,String>() {
			public String visit(Optional option) {
				return "(" + getParent().visit(option.getNode()) + ")?";
			}
		});
		this.register(Complement.class, new DefaultVisitorEntry<Parser,Complement,String>() {
			public String visit(Complement not) {
				return "(" + getParent().visit(not.getNode()) + ")'";
			}
		});
		this.register(Intersection.class, new DefaultVisitorEntry<Parser,Intersection,String>() {
			public String visit(Intersection intersection) {
				StringBuilder sb = new StringBuilder();
				if (intersection.getLeft() instanceof Alternation)
					sb.append("(" + getParent().visit(intersection.getLeft()) + ")");
				else
					sb.append(getParent().visit(intersection.getLeft()));
				sb.append("&");
				if (intersection.getRight() instanceof Alternation)
					sb.append("(" + getParent().visit(intersection.getRight()) + ")");
				else
					sb.append(getParent().visit(intersection.getRight()));
				return sb.toString();
			}
		});
		this.register(Difference.class, new DefaultVisitorEntry<Parser,Difference,String>() {
			public String visit(Difference difference) {
				StringBuilder sb = new StringBuilder();
				if (difference.getLeft() instanceof Alternation)
					sb.append("(" + getParent().visit(difference.getLeft()) + ")");
				else
					sb.append(getParent().visit(difference.getLeft()));
				sb.append("-");
				if (difference.getRight() instanceof Alternation)
					sb.append("(" + getParent().visit(difference.getRight()) + ")");
				else
					sb.append(getParent().visit(difference.getRight()));
				return sb.toString();
			}
		});
	}
}