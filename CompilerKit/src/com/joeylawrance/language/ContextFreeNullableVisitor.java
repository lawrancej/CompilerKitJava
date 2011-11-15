package com.joeylawrance.language;

import java.util.HashMap;

import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.Production;
import com.joeylawrance.visitor.DefaultVisitorEntry;

class ContextFreeNullableVisitor extends RegularNullableVisitor {
	private static ContextFreeNullableVisitor nullable = new ContextFreeNullableVisitor();
	public HashMap<Nonterminal,Parser> map = new HashMap<Nonterminal, Parser>();
	public static RegularNullableVisitor getInstance() { return nullable; }
	ContextFreeNullableVisitor() {
		super();
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,Parser>() {
			public Parser visit(Nonterminal nonterminal) {
				if (!map.containsKey(nonterminal)) {
					map.put(nonterminal, getParent().visit(nonterminal.getNode())); // this is where we run into trouble: it's visiting before we can put it in the map. to fix it, we will make an anonymous visitor!
				}
				return map.get(nonterminal);
			}			
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,Parser>() {
			public Parser visit(CFG cfg) {
				return getParent().visit(cfg.getStart());
			}
		});
		this.register(Production.class, new EquivalentVisitorEntry<Production,Parser>());
	}
}