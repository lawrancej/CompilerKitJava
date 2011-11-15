package com.joeylawrance.language;

import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.Production;
import com.joeylawrance.visitor.DefaultVisitorEntry;

class ContextFreeStringVisitor extends RegularStringVisitor {
	ContextFreeStringVisitor() {
		super();
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,String>() {
			public String visit(Nonterminal nonterminal) {
				return nonterminal.getName();
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,String>() {
			public String visit(CFG cfg) {
				ContextFree.builder.visit(cfg);
				StringBuilder sb = new StringBuilder();
				for (Nonterminal nonterm : cfg.getNonterminals()) {
					sb.append(nonterm.getName() + " -> ");
					sb.append(getParent().visit(nonterm.getNode()));
					sb.append("\n");
				}
				return sb.toString();
			}
		});
		this.register(Production.class, new EquivalentVisitorEntry<Production,String>());
	}
}