import java.util.ArrayList;
import java.util.HashMap;

/**
 * Matches regular expressions and context-free grammars using derivatives.
 * References:
 * 1. "Yacc is Dead", by Matt Might and David Darais
 * 2. "Derivative Parsing", by Daniel Spiewak
 * 3. "Derivatives of Regular expressions", by Janus Brzozowski
 */
public class Language {
	/**
	 * FIXME: Nonterminals
	 * TODO: implement group capture, boolean operations on regexes
	 */
	/**
	 * Abstract superclass for all parser combinator classes.
	 * Uses the Composite and Visitor design patterns to achieve laziness.
	 */
	public static abstract class Parser {
		<T> T accept (Visitor<T> v) { return null; }
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
	/**
	 * Publicly-available regular expression class.
	 */
	public static abstract class Regex extends Parser {
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	/**
	 * Publicly-available context-free grammar class.
	 */
	public static class CFG extends Parser {
		private Nonterminal start;
		private ArrayList <Nonterminal> nonterminals = new ArrayList<Nonterminal>();
		public CFG(Nonterminal start) {
			this.start = start;
		}
		public <T> T accept(Visitor<T> v) {
			builder.visit(this);
			return v.visit(this);
		}
	}
	private static class NonterminalListBuilder implements Visitor<Void> {
		CFG grammar;
		public Void visit(EmptySet emptySet) { return null; }
		public Void visit(EmptyString emptyString) { return null; }
		public Void visit(Symbol symbol) { return null; }
		public Void visit(Alternation alternation) {
			alternation.left.accept(this);
			alternation.right.accept(this);
			return null;
		}
		public Void visit(Catenation catenation) {
			catenation.left.accept(this);
			catenation.right.accept(this);
			return null;
		}
		public Void visit(KleeneClosure kleeneClosure) {
			kleeneClosure.node.accept(this);
			return null;
		}
		public Void visit(Nonterminal nonterminal) {
			// Halt on a rule like: S -> S
			if (!grammar.nonterminals.contains(nonterminal)) {
				grammar.nonterminals.add(nonterminal);
				nonterminal.node.accept(this);
			}
			return null;
		}
		public Void visit(Regex regex) {
			return null;
		}
		public Void visit(CFG cfg) {
			grammar = cfg;
			cfg.start.accept(this);
			return null;
		}
	}
	private static final NonterminalListBuilder builder = new NonterminalListBuilder();
	public interface Visitor<T> {
		T visit(EmptySet emptySet);
		T visit(EmptyString emptyString);
		T visit(Symbol symbol);
		T visit(Alternation alternation);
		T visit(Catenation catenation);
		T visit(KleeneClosure kleeneClosure);
		T visit(Nonterminal nonterminal);
		T visit(Regex regex);
		T visit(CFG cfg);
	}
	public static class EmptyString extends Regex {
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static final EmptyString emptyString = new EmptyString();
	public static class EmptySet extends Regex {
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static final EmptySet emptySet = new EmptySet();
	public static class Symbol extends Regex {
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
	public static class Alternation extends Regex {
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
	public static class Catenation extends Regex {
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
	public static class KleeneClosure extends Regex {
		Parser node;
		public KleeneClosure (Parser node) { this.node = node; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static KleeneClosure kleeneClosure (Parser regex) {
		return new KleeneClosure(regex);
	}
	public static class Nonterminal extends Parser {
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
		public Parser visit(Nonterminal nonterminal) { return emptySet; } //FIXME
		public Parser visit(Regex regex) { return null; } // FIXME
		public Parser visit(CFG cfg) { return cfg.start.accept(this); }
	}
	public static final NullableVisitor nullable = new NullableVisitor();
	public static final class Pair<A,B> { public A a; public B b; }
	public static class DerivativeVisitor implements Visitor<Parser> {
		public char c;
		public HashMap<Pair<Character,Nonterminal>,Nonterminal> map = new HashMap<Pair<Character,Nonterminal>,Nonterminal>();
		public DerivativeVisitor() {}
		public DerivativeVisitor(char c)           { this.c = c; }
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
		//FIXME
		public Parser visit(Nonterminal nonterminal) {
			Nonterminal result = new Nonterminal (nonterminal.name);
			result.becomes(nonterminal.accept(this));
			return result; 
		}
		public Parser visit(Regex regex) {
			return null;
		}
		public Parser visit(CFG cfg) {
			return cfg.start.accept(this);
		}
	}
	private static class CompactionVisitor implements Visitor<Parser> {
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
		public Parser visit(Nonterminal nonterminal) { // FIXME: doesn't handle S->S|lambda
			Parser p = nonterminal.node.accept(this);
			if (p == emptySet) return emptySet;
			Nonterminal result = new Nonterminal(nonterminal.name);
			result.becomes(p);
			return result;
		}
		public Parser visit(Regex regex) {
			return null;
		}
		public Parser visit(CFG cfg) {
			Parser start = cfg.start.accept(this);
			if (start == emptySet) return emptySet;
			return new CFG((Nonterminal)start);
		}
	}
	public static final CompactionVisitor compactor = new CompactionVisitor();
	private static class StringVisitor implements Visitor<String> {
		public String visit(EmptySet emptySet)       { return "{}"; }
		public String visit(EmptyString emptyString) { return "λ"; }
		public String visit(Symbol symbol)           { return "" + symbol.c; }
		public String visit(Alternation alternation) {
			return "(" + alternation.left.accept(this) + "|" + alternation.right.accept(this) + ")";
		}
		public String visit(Catenation catenation) {
			return catenation.left.accept(this) + catenation.right.accept(this);
		}
		public String visit(KleeneClosure kleeneClosure) {
			return "(" + kleeneClosure.node.accept(this) + ")*";
		}
		public String visit(Nonterminal nonterminal) {
			return nonterminal.name;
		}
		public String visit(Regex regex) {
			return null;
		}
		public String visit(CFG cfg) {
			StringBuilder sb = new StringBuilder();
			for (Nonterminal nonterm : cfg.nonterminals) {
				sb.append(nonterm.name + " -> ");
				sb.append(nonterm.node.accept(this));
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	public static final StringVisitor printer = new StringVisitor();
	public static Parser alpha() {
		Parser[] regexen = new Parser[52];
		for (int i = 0; i < regexen.length; i++) {
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
		Nonterminal value = nonterminal("value");
		Nonterminal product = nonterminal("product");
		Nonterminal sum = nonterminal("sum");
		Nonterminal expr = nonterminal ("expr");
		CFG formula = new CFG(expr);

		value.becomes(positiveClosure(digit()));
		value.becomes(parens(expr));
		product.becomes(value, kleeneClosure(catenation(alternation(symbol('*'),symbol('/')), value)));
		sum.becomes(product, kleeneClosure(catenation(alternation(symbol('+'),symbol('-')),product)));
		expr.becomes(sum);
		System.out.println(formula);
		
		
		Nonterminal s = nonterminal("S");
		CFG cfg = new CFG(s);
		s.becomes(s,parens(s));
		s.becomes();
//		Parser r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(cfg);
//		System.out.println(s.recognize("()"));
	}
}
