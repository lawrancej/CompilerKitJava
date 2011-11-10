package com.joeylawrance.language;

import java.util.HashSet;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.Symbol;
import com.joeylawrance.visitor.DefaultVisitorEntry;
import com.joeylawrance.visitor.Visitor;

/**
 * Matches Context-free grammars using derivatives.
 * References:
 * 1. "Yacc is Dead", by Matt Might and David Darais
 * 2. "Derivative Parsing", by Daniel Spiewak
 */
public class ContextFree extends Regular {
	static final NonterminalListBuilder builder = new NonterminalListBuilder();
	public static Nonterminal nonterminal (String name) {
		return new Nonterminal(name);
	}
	public static CFG cfg(Nonterminal start) {
		return new CFG(start);
	}
	static class FixedPointVisitor extends ContextFreeVisitor<Parser> {
		public Parser visit(EmptySet emptySet) { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }  // Should really be the set containing the empty string
		public Parser visit(Symbol symbol) { return EmptySet.emptySet; }
		//FIXME
		public Parser visit(Alternation alternation) {
			return visit(alternation.getLeft());
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
						Nonterminal result = new Nonterminal(nonterminal.name + "'");
						set.add(nonterminal);
						result.becomes(nonterminal);
						result.becomes(getParent().visit(nonterminal.node));
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
		//FIXME
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
				Nonterminal result = new Nonterminal(nonterminal.name + "'");
				set.add(nonterminal);
				result.becomes(nonterminal);
				result.becomes(visit(nonterminal.node));
				return result;
			}
		}
		public Parser visit(CFG cfg) {
			CFG result = new CFG(visit(cfg.getStart()));
			return result;
		}
		public HashSet<Nonterminal> set = new HashSet<Nonterminal>();
	}
	public static boolean recognize (Parser parser, String string) {
		return Matcher.recognize(parser, string, new ContextFreeDerivativeVisitor(ContextFreeNullableVisitor.getInstance()));
	}
}
