package com.joeylawrance.language;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Symbol;

class RegularCompactionVisitor extends RegularVisitor<Parser> {
	static final RegularCompactionVisitor compactor = new RegularCompactionVisitor();
	public Parser visit(EmptySet emptySet)       { return emptySet; }
	public Parser visit(EmptyString emptyString) { return emptyString; }
	public Parser visit(Symbol symbol)           { return symbol; }
	public Parser visit(Alternation alternation) {
		Parser left = visit(alternation.getLeft());
		Parser right = visit(alternation.getRight());
		if (left == EmptySet.emptySet) return right;
		else if (right == EmptySet.emptySet) return left;
		else return new Alternation(left, right);
	}
	public Parser visit(Catenation catenation) {
		Parser left = visit(catenation.getLeft());
		Parser right = visit(catenation.getRight());
		if (left == EmptyString.emptyString) return right;
		else if (left == EmptySet.emptySet) return EmptySet.emptySet;
		else return new Catenation(left, right);
	}
	public Parser visit(KleeneClosure kleeneClosure) {
		return new KleeneClosure(visit(kleeneClosure.getNode()));
	}
	public Parser visit(Complement complement) {
		return new Complement(visit(complement.getNode()));
	}
	public Parser visit(Intersection intersection) {
		Parser left = visit(intersection.getLeft());
		Parser right = visit(intersection.getRight());
		if (left == right) return right;
		else return new Intersection(left, right);
	}
}