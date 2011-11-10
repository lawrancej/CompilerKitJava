package com.joeylawrance.language;

import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.EmptySet;

class ContextFreeCompactionVisitor extends RegularCompactionVisitor {
	public Parser visit(Nonterminal nonterminal) { // FIXME: doesn't handle S->S|lambda
		Parser p = visit(nonterminal.node);
		if (p == EmptySet.emptySet) return EmptySet.emptySet;
		Nonterminal result = new Nonterminal(nonterminal.name);
		result.becomes(p);
		return result;
	}
	public Parser visit(CFG cfg) {
		Parser start = visit(cfg.getStart());
		if (start == EmptySet.emptySet) return EmptySet.emptySet;
		return new CFG((Nonterminal)start);
	}		
}