package com.joeylawrance.language;
import java.util.HashMap;

/**
 * Matches regular expressions using derivatives.
 * Reference:
 * "Derivatives of Regular expressions", by Janus Brzozowski
 */
public class Regular {
	/**
	 * TODO: implement group capture, forward/backward references, tokens, scanning
	 * TODO: boolean operations on regexes (e.g., not, and)
	 * TODO: reflective visitor is slow :(
	 */

	protected static abstract class Visitor<T> extends ReflectiveVisitor<T> {
		// Primitive regular expressions
		public abstract T visit(EmptySet emptySet);
		public abstract T visit(EmptyString emptyString);
		public abstract T visit(Symbol symbol);
		public abstract T visit(Alternation alternation);
		public abstract T visit(Catenation catenation);
		public abstract T visit(KleeneClosure kleeneClosure);
		
		// Regular expression extensions
		public T visit(PositiveClosure positiveClosure) { return visit(positiveClosure.equivalent); }
		public T visit(Times times) { return visit(times.equivalent); }
	}
	public static final EmptyString emptyString = new EmptyString();
	public static final EmptySet emptySet = new EmptySet();
	static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	public static Symbol symbol (char c) {
		if (!flyweight.containsKey(c))
			flyweight.put(c, new Symbol(c));
		return flyweight.get(c);
	}
	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
	public static Catenation catenation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Catenation (current, regexen[i]);
		}
		return (Catenation) current;
	}
	public static Catenation string (String str) {
		Symbol[] symbols = new Symbol[str.length()];
		for (int i = 0; i < str.length(); i++) {
			symbols[i] = symbol(str.charAt(i));
		}
		return catenation(symbols);
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static Parser positiveClosure (Parser r) {
		return new PositiveClosure(r);
	}
	public static Parser times (Parser r, int k) {
		return new Times(r, k);
	}
	static class NullableVisitor extends Visitor<Parser> {
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }
		public Parser visit(Symbol symbol)           { return emptySet; }
		public Parser visit(Alternation alternation) {
			Parser left = visit(alternation.left);
			Parser right = visit(alternation.right);
			if (left == emptyString || right == emptyString)
				return emptyString;
			else return emptySet;
		}
		public Parser visit(Catenation catenation) {
			Parser left = visit(catenation.left);
			Parser right = visit(catenation.right);
			if (left == emptySet || right == emptySet) {
				return emptySet;
			} else return emptyString;
		}
		public Parser visit(KleeneClosure kleeneClosure) { return emptyString; }
	}
	public static final NullableVisitor nullable = new NullableVisitor();
	static class DerivativeVisitor extends Visitor<Parser> {
		public char c;
		public DerivativeVisitor() {}
		public DerivativeVisitor(char c)             { this.c = c; }
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptySet; }
		public Parser visit(Symbol symbol)           { return (symbol.c == c) ? emptyString : emptySet; }
		public Parser visit(Alternation alternation) {
			return new Alternation (visit(alternation.left), visit(alternation.right));
		}
		public Parser visit(Catenation catenation) {
			return new Alternation (
					new Catenation (visit(catenation.left), catenation.right),
					new Catenation (nullable.visit(catenation.left),visit(catenation.right)));
		}
		public Parser visit(KleeneClosure kleeneClosure) {
			return new Catenation (visit(kleeneClosure.node), kleeneClosure);
		}
	}
	static class CompactionVisitor extends Visitor<Parser> {
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }
		public Parser visit(Symbol symbol)           { return symbol; }
		public Parser visit(Alternation alternation) {
			Parser left = visit(alternation.left);
			Parser right = visit(alternation.right);
			if (left == emptySet) return right;
			else if (right == emptySet) return left;
			else return new Alternation(left, right);
		}
		public Parser visit(Catenation catenation) {
			Parser left = visit(catenation.left);
			Parser right = visit(catenation.right);
			if (left == emptyString) return right;
			else if (left == emptySet) return emptySet;
			else return new Catenation(left, right);
		}
		public Parser visit(KleeneClosure kleeneClosure) {
			return new KleeneClosure(visit(kleeneClosure.node));
		}
	}
	static final CompactionVisitor compactor = new CompactionVisitor();
	static class StringVisitor extends Visitor<String> {
		public String visit(EmptySet emptySet)       { return "{}"; }
		public String visit(EmptyString emptyString) { return "λ"; }
		public String visit(Symbol symbol)           { return "" + symbol.c; }
		public String visit(Alternation alternation) {
			return visit(alternation.left) + "|" + visit(alternation.right);
		}
		public String visit(Catenation catenation) { // TODO:  maybe materialize new classes: {m,n}, character class, .?
			StringBuilder sb = new StringBuilder();
			if (catenation.left instanceof Alternation)
				sb.append("(" + visit(catenation.left) + ")");
			else
				sb.append(visit(catenation.left));
			if (catenation.right instanceof Alternation)
				sb.append("(" + visit(catenation.right) + ")");
			else
				sb.append(visit(catenation.right));
			return sb.toString();
		}
		public String visit(KleeneClosure kleeneClosure) {
			return "(" + visit(kleeneClosure.node) + ")*";
		}
		public String visit(PositiveClosure positiveClosure) {
			return "(" + visit(positiveClosure.node) + ")+";
		}
		public String visit(Times times) {
			return "(" + visit(times.node) + "){" + times.k + "}";
		}
	}
	public static Parser lower() {
		Parser[] regexen = new Parser[26];
		for (int i = 0; i < regexen.length; i++) {
			regexen[i] = symbol((char) ('a' + i));
		}
		return alternation(regexen);
	}
	public static Parser upper() {
		Parser[] regexen = new Parser[26];
		for (int i = 0; i < regexen.length; i++) {
			regexen[i] = symbol((char) ('A' + i));
		}
		return alternation(regexen);
	}
	public static Parser alpha() {
		return alternation(lower(),upper());
	}
	public static Parser digit() {
		return alternation(symbol('0'), symbol('1'), symbol('2'),symbol('3'), symbol('4'),
				symbol('5'), symbol('6'), symbol('7'), symbol('8'), symbol('9'));
	}
	public static Parser parens(Parser parser) {
		return catenation(symbol('('),parser,symbol(')'));
	}
}
