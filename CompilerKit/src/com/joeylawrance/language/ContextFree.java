package com.joeylawrance.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.joeylawrance.visitor.ReflectiveVisitor;
import com.joeylawrance.visitor.Visitor;

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

		@Override
		public DerivativeVisitor<Object, Parser> getDerivative() {
			return new ContextFreeDerivativeVisitor();
		}
		public Visitor<Object,Parser> getNullable() {
			return ContextFreeNullableVisitor.nullable;
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
	protected static abstract class ContextFreeVisitor<T> extends RegularVisitor<T> {
		public abstract T visit(Nonterminal nonterminal);
		public abstract T visit(CFG cfg);		
	}
	static class NonterminalListBuilder extends ContextFreeVisitor<Void> {
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
	private static final NonterminalListBuilder builder = new NonterminalListBuilder();
	public static class Nonterminal extends Grammar {
		Parser node; String name;
		public Nonterminal (String name) { this.name = name; }
		public void becomes (Parser ...nodes) {
			// Construct new node from node parameter
			Parser newNode;
			if (nodes.length > 1)
				newNode = Catenation.catenation(nodes);
			else if (nodes.length == 1)
				newNode = nodes[0];
			else newNode = EmptyString.emptyString;

			// If the nonterminal doesn't have a node, it's newNode
			// Otherwise, its the existing node or newNode
			if (node == null) {
				node = newNode;
			} else {
				node = Alternation.alternation(node,newNode);
			}
		}
	}
	public static Nonterminal nonterminal (String name) {
		return new Nonterminal(name);
	}
	static class FixedPointVisitor extends ContextFreeVisitor<Parser> {
		public Parser visit(EmptySet emptySet) { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }  // Should really be the set containing the empty string
		public Parser visit(Symbol symbol) { return EmptySet.emptySet; }
		//FIXME
		public Parser visit(Alternation alternation) {
			return visit(alternation.left);
			// return alternation.right.accept(this);
		}
		public Parser visit(Catenation catenation) {
			return null;

		}
		public Parser visit(KleeneClosure kleeneClosure) {
			return null;
		}
		public Parser visit(Nonterminal nonterminal) {
			return null;
		}
		public Parser visit(CFG cfg) {
			return null;
		}
		@Override
		public Parser visit(Complement not) {
			return null;
		}
		@Override
		public Parser visit(Intersection intersection) {
			return null;
		}
	}
	static class ContextFreeDerivativeVisitor extends RegularDerivativeVisitor {
		//FIXME
		public Parser visit(Nonterminal nonterminal) {
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
			return visit(nonterminal.node);
		}
		public Parser visit(CFG cfg) {
			return visit(cfg.start);
		}
		public Map<Character,CFG> map = new HashMap<Character,CFG>();
	}
	static class ContextFreeNullableVisitor extends RegularNullableVisitor {
		static ContextFreeNullableVisitor nullable = new ContextFreeNullableVisitor();
		public Parser visit(Nonterminal nonterminal) {
			return visit(nonterminal.node);
		}
		public Parser visit(CFG cfg) {
			return visit(cfg.start);
		}
	}
	static class Pair<S,T> {
		S left; T right;
		public Pair(S left, T right) { this.left = left; this.right = right; }
		public int hashCode() { return left.hashCode()+right.hashCode(); }
	}
	static class CompactionVisitor extends com.joeylawrance.language.CompactionVisitor {
		public Parser visit(Nonterminal nonterminal) { // FIXME: doesn't handle S->S|lambda
			Parser p = visit(nonterminal.node);
			if (p == EmptySet.emptySet) return EmptySet.emptySet;
			Nonterminal result = new Nonterminal(nonterminal.name);
			result.becomes(p);
			return result;
		}
		public Parser visit(CFG cfg) {
			Parser start = visit(cfg.start);
			if (start == EmptySet.emptySet) return EmptySet.emptySet;
			return new CFG((Nonterminal)start);
		}		
	}
	static class StringVisitor extends com.joeylawrance.language.StringVisitor {
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
