package com.joeylawrance.language;

import java.util.HashMap;

class ContextFreeNullableVisitor extends RegularNullableVisitor {
	static ContextFreeNullableVisitor nullable = new ContextFreeNullableVisitor();
	public HashMap<Nonterminal,Parser> map = new HashMap<Nonterminal, Parser>();
	public Parser visit(Nonterminal nonterminal) {
		if (!map.containsKey(nonterminal)) {
			map.put(nonterminal, visit(nonterminal.node)); // this is where we run into trouble: it's visiting before we can put it in the map. to fix it, we will make an anonymous visitor!
		}
		return map.get(nonterminal);
	}
	public Parser visit(CFG cfg) {
		return visit(cfg.start);
	}
}