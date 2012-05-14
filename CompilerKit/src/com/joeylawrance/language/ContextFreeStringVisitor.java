package com.joeylawrance.language;

import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.NonterminalImpl;
import com.joeylawrance.visitor.DefaultVisitorEntry;

class ContextFreeStringVisitor extends RegularStringVisitor {
	ContextFreeStringVisitor() {
		super();
		this.register(NonterminalImpl.class, new DefaultVisitorEntry<Parser,NonterminalImpl,String>() {
			public String visit(NonterminalImpl nonterminal) {
				return nonterminal.getName();
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,String>() {
			public String visit(CFG cfg) {
				ContextFree.builder.visit(cfg);
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (Nonterminal nonterm : cfg.getNonterminals()) {
					for (Parser p : nonterm.getProductions()) {
						if (first)
							sb.append(nonterm.getName() + " -> ");
						else {
							for (char c : nonterm.getName().toCharArray()) sb.append(' ');
							sb.append("  | ");
						}
						first = false;
						sb.append(getParent().visit(p));
						sb.append("\n");
					}
					first = true;
				}
				return sb.toString();
			}
		});
	}
}