package com.joeylawrance.language;


import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.CFG;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.Complement;
import com.joeylawrance.language.parsers.EmptySet;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Intersection;
import com.joeylawrance.language.parsers.KleeneClosure;
import com.joeylawrance.language.parsers.NonterminalImpl;
import com.joeylawrance.language.parsers.Symbol;

/**
 * Matches Context-free grammars using derivatives.
 * References:
 * 1. "Yacc is Dead", by Matt Might and David Darais
 * 2. "Derivative Parsing", by Daniel Spiewak
 */
public class ContextFree extends Regular {
	static final NonterminalListBuilder builder = new NonterminalListBuilder();
	public static Nonterminal nonterminal (String name) {
		return new NonterminalImpl(name);
	}
	public static CFG cfg(Nonterminal start) {
		return new CFG(start);
	}
	static class FixedPointVisitor extends RegularVisitor<Parser> {
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
		public Parser visit(NonterminalImpl nonterminal) {
			return null;
		}
		public Parser visit(CFG cfg) {
			return null;
		}
		public Parser visit(Complement not) {
			return null;
		}
		public Parser visit(Intersection intersection) {
			return null;
		}
	}
	public static boolean recognize (Parser parser, String string) {
		return Matcher.recognize(parser, string, new ContextFreeDerivativeVisitor(ContextFreeNullableVisitor.getInstance()));
	}
}
