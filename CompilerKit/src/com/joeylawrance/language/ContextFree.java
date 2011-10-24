package com.joeylawrance.language;

import java.util.ArrayList;

/**
 * Matches Context-free grammars using derivatives.
 * References:
 * 1. "Yacc is Dead", by Matt Might and David Darais
 * 2. "Derivative Parsing", by Daniel Spiewak
 */
public class ContextFree extends Regular {
	// FIXME: nonterminals
	static class Grammar extends Parser {
		public ReflectiveVisitor<String> getPrinter() {
			return new StringVisitor();
		}
	}
	/**
	 * Publicly-available context-free grammar class.
	 */
	public static class CFG extends Grammar {
		private Nonterminal start;
		private ArrayList <Nonterminal> nonterminals;
		public CFG(Nonterminal start) {
			this.start = start;
		}
	}
	protected static abstract class Visitor<T> extends com.joeylawrance.language.Regular.Visitor<T> {
		public abstract T visit(Nonterminal nonterminal);
		public abstract T visit(CFG cfg);		
	}
	static class NonterminalListBuilder extends Visitor<Void> {
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
	}
	private static final NonterminalListBuilder builder = new NonterminalListBuilder();
	public static class Nonterminal extends Grammar {
		Parser node; String name;
		public Nonterminal (String name) { this.name = name; }
		public void becomes (Parser ...nodes) {
			// Construct new node from node parameter
			Parser newNode;
			if (nodes.length > 1)
				newNode = catenation(nodes);
			else if (nodes.length == 1)
				newNode = nodes[0];
			else newNode = emptyString;
			
			// If the nonterminal doesn't have a node, it's newNode
			// Otherwise, its the existing node or newNode
			if (node == null) {
				node = newNode;
			} else {
				node = alternation(node,newNode);
			}
		}
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static Nonterminal nonterminal (String name) {
		return new Nonterminal(name);
	}
	static class FixedPointVisitor extends Visitor<Parser> {
		public Parser visit(EmptySet emptySet) { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }  // Should really be the set containing the empty string
		public Parser visit(Symbol symbol) { return emptySet; }
		//FIXME
		public Parser visit(Alternation alternation) {
			return visit(alternation.left);
			// return alternation.right.accept(this);
		}
		public Parser visit(Catenation catenation) {
			// TODO Auto-generated method stub
			return null;
			
		}
		public Parser visit(KleeneClosure kleeneClosure) {
			// TODO Auto-generated method stub
			return null;
		}
		public Parser visit(Nonterminal nonterminal) {
			// TODO Auto-generated method stub
			return null;
		}
		public Parser visit(CFG cfg) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	static class DerivativeVisitor extends com.joeylawrance.language.Regular.DerivativeVisitor {
		//FIXME
		public Parser visit(Nonterminal nonterminal) {
			Nonterminal result = new Nonterminal (nonterminal.name);
			result.becomes(visit(nonterminal));
			return result; 
		}
		public Parser visit(CFG cfg) {
			return visit(cfg.start);
		}
//		public HashMap<Pair<Character,Nonterminal>,Nonterminal> map = new HashMap<Pair<Character,Nonterminal>,Nonterminal>();
	}
	static class CompactionVisitor extends com.joeylawrance.language.Regular.CompactionVisitor {
		public Parser visit(Nonterminal nonterminal) { // FIXME: doesn't handle S->S|lambda
			Parser p = visit(nonterminal.node);
			if (p == emptySet) return emptySet;
			Nonterminal result = new Nonterminal(nonterminal.name);
			result.becomes(p);
			return result;
		}
		public Parser visit(CFG cfg) {
			Parser start = visit(cfg.start);
			if (start == emptySet) return emptySet;
			return new CFG((Nonterminal)start);
		}		
	}
	static class StringVisitor extends com.joeylawrance.language.Regular.StringVisitor {
		public String visit(Nonterminal nonterminal) {
			return nonterminal.name;
		}
		public String visit(CFG cfg) {
			builder.visit(cfg);
			StringBuilder sb = new StringBuilder();
			for (Nonterminal nonterm : cfg.nonterminals) {
				sb.append(nonterm.name + " -> ");
				sb.append(visit(nonterm.node));
				sb.append("\n");
			}
			return sb.toString();
		}		
	}
}
