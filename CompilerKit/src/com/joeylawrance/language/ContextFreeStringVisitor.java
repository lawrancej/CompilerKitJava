package com.joeylawrance.language;

import com.joeylawrance.visitor.DefaultVisitorEntry;

class ContextFreeStringVisitor extends RegularStringVisitor {
	ContextFreeStringVisitor() {
		super();
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,String>() {
			public String visit(Nonterminal nonterminal) {
				return nonterminal.name;
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,String>() {
			public String visit(CFG cfg) {
				ContextFree.builder.visit(cfg);
				StringBuilder sb = new StringBuilder();
				for (Nonterminal nonterm : cfg.nonterminals) {
					sb.append(nonterm.name + " -> ");
					sb.append(getParent().visit(nonterm.node));
					sb.append("\n");
				}
				return sb.toString();
			}
		});
	}
}