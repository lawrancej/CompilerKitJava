package com.joeylawrance.language;

import java.util.ArrayList;


class NonterminalListBuilder extends ContextFreeVisitor<Void> {
	CFG grammar;
	public Void visit(EmptySet emptySet) { return null; }
	public Void visit(EmptyString emptyString) { return null; }
	public Void visit(Symbol symbol) { return null; }
	public Void visit(Alternation alternation) {
		visit(alternation.left);
		visit(alternation.right);
		return null;
	}
	public Void visit(Catenation catenation) {
		visit(catenation.left);
		visit(catenation.right);
		return null;
	}
	public Void visit(KleeneClosure kleeneClosure) {
		visit(kleeneClosure.node);
		return null;
	}
	public Void visit(Complement not) {
		visit(not.node);
		return null;
	}
	public Void visit(Nonterminal nonterminal) {
		// Halt on a rule like: S -> S
		if (!grammar.nonterminals.contains(nonterminal)) {
			grammar.nonterminals.add(nonterminal);
			visit(nonterminal.node);
		}
		return null;
	}
	public Void visit(CFG cfg) {
		grammar = cfg;
		grammar.nonterminals = new ArrayList<Nonterminal>();
		visit(cfg.start);
		return null;
	}
	@Override
	public Void visit(Intersection intersection) {
		visit(intersection.left);
		visit(intersection.right);
		return null;
	}
}