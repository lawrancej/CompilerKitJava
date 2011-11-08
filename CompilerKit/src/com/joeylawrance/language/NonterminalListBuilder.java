package com.joeylawrance.language;

import java.util.ArrayList;

import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.NullVisitorEntry;

// FIXME: TODO: this is preventing CFGTest from working
class NonterminalListBuilder extends ContextFreeVisitor<Void> {
	CFG grammar;
	public NonterminalListBuilder() {
		super();
		this.register(EmptySet.class, new NullVisitorEntry<Parser,EmptySet,Void>());
		this.register(EmptyString.class, new NullVisitorEntry<Parser,EmptyString,Void>());
		this.register(Symbol.class, new NullVisitorEntry<Parser,Symbol,Void>());
		// TODO: It'd be nice to be able to reuse visitor entries as long as they have a common base type
		this.register(Alternation.class, new DefaultVisitorEntry<Parser,Alternation,Void>() {
			public Void visit(Alternation node) {
				getParent().visit(node.left);
				getParent().visit(node.right);
				return null;
			}
		});		
		this.register(Catenation.class, new DefaultVisitorEntry<Parser,Catenation,Void>() {
			public Void visit(Catenation node) {
				getParent().visit(node.left);
				getParent().visit(node.right);
				return null;
			}
		});
		this.register(Intersection.class, new DefaultVisitorEntry<Parser,Intersection,Void>() {
			public Void visit(Intersection node) {
				getParent().visit(node.left);
				getParent().visit(node.right);
				return null;
			}
		});		
		this.register(Difference.class, new DefaultVisitorEntry<Parser,Difference,Void>() {
			public Void visit(Difference node) {
				getParent().visit(node.left);
				getParent().visit(node.right);
				return null;
			}
		});
		this.register(Complement.class, new DefaultVisitorEntry<Parser,Complement,Void>() {
			public Void visit(Complement not) {
				getParent().visit(not.node);
				return null;
			}
		});		
		this.register(KleeneClosure.class, new DefaultVisitorEntry<Parser,KleeneClosure,Void>() {
			public Void visit(KleeneClosure kleeneClosure) {
				getParent().visit(kleeneClosure.node);
				return null;
			}
		});
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,Void>() {
			public Void visit(Nonterminal nonterminal) {
				// Halt on a rule like: S -> S
				if (!grammar.nonterminals.contains(nonterminal)) {
					grammar.nonterminals.add(nonterminal);
					getParent().visit(nonterminal.node);
				}
				return null;
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,Void>() {
			public Void visit(CFG cfg) {
				grammar = cfg;
				grammar.nonterminals = new ArrayList<Nonterminal>();
				getParent().visit(cfg.start);
				return null;
			}
		});
	}
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