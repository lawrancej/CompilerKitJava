package com.joeylawrance.language;

import java.util.HashMap;
import java.util.HashSet;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.NonterminalImpl;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.MemoizedVisitorEntry;
import com.joeylawrance.visitor.Visitor;

public class ContextFreeDerivativeVisitor extends RegularDerivativeVisitor {
	private static final HashSet<NonterminalImpl> set = new HashSet<NonterminalImpl>();
	//FIXME
	public ContextFreeDerivativeVisitor(Visitor<Parser, Parser> nullable) {
		super(nullable);
		this.register(NonterminalImpl.class, new DefaultVisitorEntry<Parser,NonterminalImpl,Parser> () {
			public NonterminalImpl visit(NonterminalImpl nonterminal) {
				/*
				Nonterminal result = new Nonterminal (nonterminal.getName() + "'");
				result.becomes(nonterminal);
				System.out.println(result);
				for (Parser production : nonterminal.getProductions()) {
					result.becomes(getParent().visit(production));
				}
				return result;
				*/
				/*
				ContextFreeDerivativeVisitor me = (ContextFreeDerivativeVisitor)this.getParent();
				if (map.containsKey(me.getSymbol()) && map.get(me.getSymbol()).contains(nonterminal))
					return nonterminal;
				else {
					HashSet<Nonterminal> nonterminals;
					Nonterminal result = new Nonterminal (nonterminal.getName());
					nonterminals = map.get(me.getSymbol());
					if (nonterminals == null) {
						nonterminals = new HashSet<Nonterminal>();
						map.put((Character)me.getSymbol(), nonterminals);
					}
					if (!nonterminals.contains(nonterminal)) {
						nonterminals.add(result);
						result.becomes(Alternation.build(nonterminal,visit(nonterminal)));
						return result;
					} else {
						return nonterminal;
					}
				}
				*/
				if (set.contains(nonterminal)) return nonterminal;
				else {
					NonterminalImpl result = new NonterminalImpl(nonterminal.getName() + "'");
					set.add(nonterminal);
					result.becomes(nonterminal);
					for (Parser production : nonterminal.getProductions()) {
						result.becomes(getParent().visit(production));
					}
					return result;
				}
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,Parser>() {
			public Parser visit(CFG cfg) {
				CFG result = new CFG((NonterminalImpl)getParent().visit(cfg.getStart()));
				return result;
			}				
		});
	}
}