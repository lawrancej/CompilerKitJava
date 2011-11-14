package com.joeylawrance.language;

import java.util.HashSet;

import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.Visitor;

public class ContextFreeDerivativeVisitor extends RegularDerivativeVisitor {
	//FIXME
	public ContextFreeDerivativeVisitor(Visitor<Parser, Parser> nullable) {
		super(nullable);
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,Parser> () {
			public Nonterminal visit(Nonterminal nonterminal) {
				/*			if (map.containsKey(this.getSymbol()) && map.get(this.getSymbol()).nonterminals.contains(nonterminal))
								return nonterminal;
							else {
								HashSet<Nonterminal> nonterminals;
								Nonterminal result = new Nonterminal (nonterminal.name);
								nonterminals = map.get(this.getSymbol());
								if (nonterminals == null) {
									nonterminals = new HashSet<Nonterminal>();
									map.put((Character)this.getSymbol(), nonterminals);
								}
								if (!nonterminals.contains(nonterminal)) {
									nonterminals.add(result);
									result.becomes(new Alternation(nonterminal,visit(nonterminal)));
									return result;
								} else {
									return nonterminal;
								}
							}*/
				if (set.contains(nonterminal)) return nonterminal;
				else {
					Nonterminal result = new Nonterminal(nonterminal.getName() + "'");
					set.add(nonterminal);
					result.becomes(nonterminal);
					result.becomes(getParent().visit(nonterminal.getNode()));
					return result;
				}
			}				
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,Parser>() {
			public Parser visit(CFG cfg) {
				CFG result = new CFG((Nonterminal)getParent().visit(cfg.getStart()));
				return result;
			}				
		});
	}
	public HashSet<Nonterminal> set = new HashSet<Nonterminal>();
}