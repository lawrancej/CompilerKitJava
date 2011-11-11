package com.joeylawrance.language;

import java.util.ArrayList;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.Difference;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.NullVisitorEntry;

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
				getParent().visit(node.getLeft());
				getParent().visit(node.getRight());
				return null;
			}
		});		
		this.register(Catenation.class, new DefaultVisitorEntry<Parser,Catenation,Void>() {
			public Void visit(Catenation node) {
				getParent().visit(node.getLeft());
				getParent().visit(node.getRight());
				return null;
			}
		});
		this.register(Intersection.class, new DefaultVisitorEntry<Parser,Intersection,Void>() {
			public Void visit(Intersection node) {
				getParent().visit(node.getLeft());
				getParent().visit(node.getRight());
				return null;
			}
		});		
		this.register(Difference.class, new DefaultVisitorEntry<Parser,Difference,Void>() {
			public Void visit(Difference node) {
				getParent().visit(node.getLeft());
				getParent().visit(node.getRight());
				return null;
			}
		});
		this.register(Complement.class, new DefaultVisitorEntry<Parser,Complement,Void>() {
			public Void visit(Complement not) {
				getParent().visit(not.getNode());
				return null;
			}
		});		
		this.register(KleeneClosure.class, new DefaultVisitorEntry<Parser,KleeneClosure,Void>() {
			public Void visit(KleeneClosure kleeneClosure) {
				getParent().visit(kleeneClosure.getNode());
				return null;
			}
		});
		this.register(Nonterminal.class, new DefaultVisitorEntry<Parser,Nonterminal,Void>() {
			public Void visit(Nonterminal nonterminal) {
				// Halt on a rule like: S -> S
				if (!grammar.getNonterminals().contains(nonterminal)) {
					grammar.getNonterminals().add(nonterminal);
					getParent().visit(nonterminal.getNode());
				}
				return null;
			}
		});
		this.register(CFG.class, new DefaultVisitorEntry<Parser,CFG,Void>() {
			public Void visit(CFG cfg) {
				grammar = cfg;
				getParent().visit(cfg.getStart());
				return null;
			}
		});
	}
	public Void visit(EmptySet emptySet) { return null; }
	public Void visit(EmptyString emptyString) { return null; }
	public Void visit(Symbol symbol) { return null; }
	public Void visit(Alternation alternation) {
		visit(alternation.getLeft());
		visit(alternation.getRight());
		return null;
	}
	public Void visit(Catenation catenation) {
		visit(catenation.getLeft());
		visit(catenation.getRight());
		return null;
	}
	public Void visit(KleeneClosure kleeneClosure) {
		visit(kleeneClosure.getNode());
		return null;
	}
	public Void visit(Complement not) {
		visit(not.getNode());
		return null;
	}
	public Void visit(Nonterminal nonterminal) {
		// Halt on a rule like: S -> S
		if (!grammar.getNonterminals().contains(nonterminal)) {
			grammar.getNonterminals().add(nonterminal);
			visit(nonterminal.getNode());
		}
		return null;
	}
	public Void visit(CFG cfg) {
		grammar = cfg;
		visit(cfg.getStart());
		return null;
	}
	@Override
	public Void visit(Intersection intersection) {
		visit(intersection.getLeft());
		visit(intersection.getRight());
		return null;
	}
}