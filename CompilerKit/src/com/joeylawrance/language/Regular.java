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
	 * 
	 */
	/**
	 * Abstract superclass for all parser combinator classes.
	 * Uses the Composite and Visitor design patterns.
	 */
	protected static abstract class Parser {
		public abstract ReflectiveVisitor<String> getPrinter();
		public String toString () {
			return getPrinter().visit(this);
		}
		public boolean recognize (CharSequence str) {
			Parser parser = this;
			DerivativeVisitor derivative = new DerivativeVisitor();
			for (int i = 0; i < str.length(); i++) {
				derivative.c = str.charAt(i);
				parser = derivative.visit(parser);
				parser = compactor.visit(parser);
			}
			return nullable.visit(parser) == emptyString;
		}
	}
	public static abstract class Visitor<T> extends ReflectiveVisitor<T> {
		// Primitive regular expressions
		public abstract T visit(EmptySet emptySet);
		public abstract T visit(EmptyString emptyString);
		public abstract T visit(Symbol symbol);
		public abstract T visit(Alternation alternation);
		public abstract T visit(Catenation catenation);
		public abstract T visit(KleeneClosure kleeneClosure);
		
		// Regular expression extensions
		public T visit(PositiveClosure positiveClosure) { return visit(positiveClosure.equivalent); }
	}
	public static class Expression extends Parser {
		public ReflectiveVisitor<String> getPrinter() {
			return new StringVisitor();
		}
	}
	public static class EmptyString extends Expression {}
	public static final EmptyString emptyString = new EmptyString();
	public static class EmptySet extends Expression {}
	public static final EmptySet emptySet = new EmptySet();
	public static class Symbol extends Expression {
		final char c;
		public Symbol (char c) { this.c = c; }
	}
	public static final HashMap<Character, Symbol> flyweight = new HashMap<Character, Symbol>();
	public static Symbol symbol (char c) {
		if (!flyweight.containsKey(c))
			flyweight.put(c, new Symbol(c));
		return flyweight.get(c);
	}
	public static class BinaryOperator extends Expression {
		Parser left, right;		
		public BinaryOperator (Parser left, Parser right) { this.left = left; this.right = right; }
	}
	public static class Alternation extends BinaryOperator {
		public Alternation(Parser left, Parser right) { super(left, right); }
	}
	public static Alternation alternation (Parser ... regexen) {
		Parser current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
	public static class Catenation extends BinaryOperator {
		public Catenation(Parser left, Parser right) { super(left, right); }
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
	public static class KleeneClosure extends Expression {
		Parser node;
		public KleeneClosure (Parser node) { this.node = node; }
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static class PositiveClosure extends Expression {
		Parser equivalent;
		Parser node;
		public PositiveClosure (Parser node) {
			this.equivalent = catenation(node, kleeneClosure(node));
			this.node = node;
		}
	}
	public static Parser positiveClosure (Parser r) {
		return new PositiveClosure(r);
	}
	public static class NullableVisitor extends Visitor<Parser> {
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
	public static final class Pair<A,B> { public A a; public B b; }
	public static class DerivativeVisitor extends Visitor<Parser> {
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
	protected static class CompactionVisitor extends Visitor<Parser> {
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
	public static final CompactionVisitor compactor = new CompactionVisitor();
	protected static class StringVisitor extends Visitor<String> {
		public String visit(EmptySet emptySet)       { return "{}"; }
		public String visit(EmptyString emptyString) { return "λ"; }
		public String visit(Symbol symbol)           { return "" + symbol.c; }
		public String visit(Alternation alternation) {
			return visit(alternation.left) + "|" + visit(alternation.right);
		}
		public String visit(Catenation catenation) { // TODO:  maybe materialize new classes: {m,n}, character class, .?
			StringBuilder sb = new StringBuilder();
			if (isAlternation(catenation.left))
				sb.append("(" + visit(catenation.left) + ")");
			else
				sb.append(visit(catenation.left));
			if (isAlternation(catenation.right))
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
	}
	private static boolean isAlternation(Parser p) {return p instanceof Alternation; }
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
	public static void main (String[] args) {
		Parser r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(r);
		System.out.println(r.recognize("somebody@bridgew.edu"));
		System.out.println(r.recognize("somebody@wit.edu"));
	}
}
