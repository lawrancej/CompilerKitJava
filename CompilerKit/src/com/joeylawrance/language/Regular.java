package com.joeylawrance.language;
import java.util.HashMap;

/**
 * Matches regular expressions using derivatives.
 * Reference:
 * "Derivatives of Regular expressions", by Janus Brzozowski
 */
public class Regular {
	/**
	 * TODO: implement group capture
	 * TODO: boolean operations on regexes (e.g., not, and)
	 * TODO: reflective visitor
	 * 
	 */
	/**
	 * Abstract superclass for all parser combinator classes.
	 * Uses the Composite and Visitor design patterns.
	 */
	protected static abstract class Parser {
		public abstract <T> T accept (Visitor<T> v);
		public String toString () {
			return this.accept(printer);
		}
		public boolean recognize (CharSequence str) {
			Parser parser = this;
			DerivativeVisitor derivative = new DerivativeVisitor();
			for (int i = 0; i < str.length(); i++) {
				derivative.c = str.charAt(i);
				parser = parser.accept(derivative);
				parser = parser.accept(compactor);
			}
			return parser.accept(nullable) == emptyString;
		}
	}
	public interface Visitor<T> {
		T visit(EmptySet emptySet);
		T visit(EmptyString emptyString);
		T visit(Symbol symbol);
		T visit(Alternation alternation);
		T visit(Catenation catenation);
		T visit(KleeneClosure kleeneClosure);
	}
	public static class EmptyString extends Parser {
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static final EmptyString emptyString = new EmptyString();
	public static class EmptySet extends Parser {
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static final EmptySet emptySet = new EmptySet();
	public static class Symbol extends Parser {
		char c;
		public Symbol (char c) { this.c = c; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	public static Symbol symbol (char c) {
		if (!flyweight.containsKey(c))
			flyweight.put(c, new Symbol(c));
		return flyweight.get(c);
	}
	public static class Alternation extends Parser {
		Parser left, right;
		public Alternation (Parser left, Parser right) { this.left = left; this.right = right; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
	public static class Catenation extends Parser {
		Parser left, right;
		public Catenation (Parser left, Parser right) { this.left = left; this.right = right; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
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
	public static class KleeneClosure extends Parser {
		Parser node;
		public KleeneClosure (Parser node) { this.node = node; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static class NullableVisitor implements Visitor<Parser> {
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }
		public Parser visit(Symbol symbol)           { return emptySet; }
		public Parser visit(Alternation alternation) {
			Parser left = alternation.left.accept(this);
			Parser right = alternation.right.accept(this);
			if (left == emptyString || right == emptyString)
				return emptyString;
			else return emptySet;
		}
		public Parser visit(Catenation catenation) {
			Parser left = catenation.left.accept(this);
			Parser right = catenation.right.accept(this);
			if (left == emptySet || right == emptySet) {
				return emptySet;
			} else return emptyString;
		}
		public Parser visit(KleeneClosure kleeneClosure) { return emptyString; }
	}
	public static final NullableVisitor nullable = new NullableVisitor();
	public static final class Pair<A,B> { public A a; public B b; }
	public static class DerivativeVisitor implements Visitor<Parser> {
		public char c;
		public DerivativeVisitor() {}
		public DerivativeVisitor(char c)             { this.c = c; }
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptySet; }
		public Parser visit(Symbol symbol)           { return (symbol.c == c) ? emptyString : emptySet; }
		public Parser visit(Alternation alternation) {
			return new Alternation (alternation.left.accept(this), alternation.right.accept(this));
		}
		public Parser visit(Catenation catenation) {
			return new Alternation (
					new Catenation (catenation.left.accept(this), catenation.right),
					new Catenation (catenation.left.accept(nullable),catenation.right.accept(this)));
		}
		public Parser visit(KleeneClosure kleeneClosure) {
			return new Catenation (kleeneClosure.node.accept(this), kleeneClosure);
		}
	}
	protected static class CompactionVisitor implements Visitor<Parser> {
		public Parser visit(EmptySet emptySet)       { return emptySet; }
		public Parser visit(EmptyString emptyString) { return emptyString; }
		public Parser visit(Symbol symbol)           { return symbol; }
		public Parser visit(Alternation alternation) {
			Parser left = alternation.left.accept(this);
			Parser right = alternation.right.accept(this);
			if (left == emptySet) return right;
			else if (right == emptySet) return left;
			else return new Alternation(left, right);
		}
		public Parser visit(Catenation catenation) {
			Parser left = catenation.left.accept(this);
			Parser right = catenation.right.accept(this);
			if (left == emptyString) return right;
			else if (left == emptySet) return emptySet;
			else return new Catenation(left, right);
		}
		public Parser visit(KleeneClosure kleeneClosure) {
			return new KleeneClosure(kleeneClosure.node.accept(this));
		}
	}
	public static final CompactionVisitor compactor = new CompactionVisitor();
	protected static class StringVisitor implements Visitor<String> {
		public String visit(EmptySet emptySet)       { return "{}"; }
		public String visit(EmptyString emptyString) { return "λ"; }
		public String visit(Symbol symbol)           { return "" + symbol.c; }
		public String visit(Alternation alternation) {
			return alternation.left.accept(this) + "|" + alternation.right.accept(this);
		}
		public String visit(Catenation catenation) {
			StringBuilder sb = new StringBuilder();
			if (isAlternation(catenation.left))
				sb.append("(" + catenation.left.accept(this) + ")");
			else
				sb.append(catenation.left.accept(this));
			if (isAlternation(catenation.right))
				sb.append("(" + catenation.right.accept(this) + ")");
			else
				sb.append(catenation.right.accept(this));
			return sb.toString();
		}
		public String visit(KleeneClosure kleeneClosure) {
			return "(" + kleeneClosure.node.accept(this) + ")*";
		}
	}
	protected static StringVisitor printer = new StringVisitor();
	private static boolean isAlternation(Parser p) {return p instanceof Alternation; }
	public static Parser alpha() {
		Parser[] regexen = new Parser[52];
		for (int i = 0; i < 26; i++) {
			regexen[i] = symbol((char) ('a' + i));
			regexen[i+26] = symbol((char) ('A' + i));
		}
		return alternation(regexen);
	}
	public static Parser digit() {
		return alternation(symbol('0'), symbol('1'), symbol('2'),symbol('3'), symbol('4'),
				symbol('5'), symbol('6'), symbol('7'), symbol('8'), symbol('9'));
	}
	public static Parser parens(Parser parser) {
		return catenation(symbol('('),parser,symbol(')'));
	}
	public static Parser positiveClosure (Parser r) {
		return catenation(r, kleeneClosure(r));
	}
	public static void main (String[] args) {
		Parser r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(r.recognize("somebody@wit.edu"));
	}
}
