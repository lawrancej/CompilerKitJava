import java.util.HashMap;


public class RegularLanguage {
	/**
	 * TODO: implement group capture, boolean operations on regexes
	 * @author lawrance
	 *
	 */
	public static abstract class Regex {
		<T> T accept (Visitor<T> v) { return null; }
		public String toString () {
			return this.accept(printer);
		}
		public boolean recognize (CharSequence str) {
			Regex regex = this;
			DerivativeVisitor derivative = new DerivativeVisitor();
			for (int i = 0; i < str.length(); i++) {
				derivative.c = str.charAt(i);
				regex = regex.accept(derivative);
				regex = regex.accept(compactor);
			}
			return regex.accept(nullable) == emptyString;
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
		Regex left, right;
		public Alternation (Regex left, Regex right) { this.left = left; this.right = right; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static Alternation alternation (Regex ... regexen) {
		Regex current = regexen[0];
		for (int i = 1; i < regexen.length; i++) {
			current = new Alternation (current, regexen[i]);
		}
		return (Alternation) current;
	}
	public static class Catenation extends Regex {
		Regex left, right;
		public Catenation (Regex left, Regex right) { this.left = left; this.right = right; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static Catenation catenation (Regex ... regexen) {
		Regex current = regexen[0];
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
		Regex node;
		public KleeneClosure (Regex node) { this.node = node; }
		public <T> T accept(Visitor<T> v) { return v.visit(this); }
	}
	public static KleeneClosure kleeneClosure (Regex regex) {
		return new KleeneClosure(regex);
	}
	public static class NullableVisitor implements Visitor<Regex> {
		public Regex visit(EmptySet emptySet)       { return emptySet; }
		public Regex visit(EmptyString emptyString) { return emptyString; }
		public Regex visit(Symbol symbol)           { return emptySet; }
		public Regex visit(Alternation alternation) {
			Regex left = alternation.left.accept(this);
			Regex right = alternation.right.accept(this);
			if (left == emptyString || right == emptyString)
				return emptyString;
			else return emptySet;
		}
		public Regex visit(Catenation catenation) {
			Regex left = catenation.left.accept(this);
			Regex right = catenation.right.accept(this);
			if (left == emptySet || right == emptySet) {
				return emptySet;
			} else return emptyString;
		}
		public Regex visit(KleeneClosure kleeneClosure) { return emptyString; }
	}
	public static final NullableVisitor nullable = new NullableVisitor();
	public static class DerivativeVisitor implements Visitor<Regex> {
		public char c;
		public DerivativeVisitor() {}
		public DerivativeVisitor(char c)           { this.c = c; }
		public Regex visit(EmptySet emptySet)       { return emptySet; }
		public Regex visit(EmptyString emptyString) { return emptySet; }
		public Regex visit(Symbol symbol)           { return (symbol.c == c) ? emptyString : emptySet; }
		public Regex visit(Alternation alternation) {
			return new Alternation (alternation.left.accept(this), alternation.right.accept(this));
		}
		public Regex visit(Catenation catenation) {
			return new Alternation (
					new Catenation (catenation.left.accept(this), catenation.right),
					new Catenation (catenation.left.accept(nullable),catenation.right.accept(this)));
		}
		public Regex visit(KleeneClosure kleeneClosure) {
			return new Catenation (kleeneClosure.node.accept(this), kleeneClosure);
		}
	}
	public static class CompactionVisitor implements Visitor<Regex> {
		public Regex visit(EmptySet emptySet)       { return emptySet; }
		public Regex visit(EmptyString emptyString) { return emptyString; }
		public Regex visit(Symbol symbol)           { return symbol; }
		public Regex visit(Alternation alternation) {
			Regex left = alternation.left.accept(this);
			Regex right = alternation.right.accept(this);
			if (left == emptySet) return right;
			else if (right == emptySet) return left;
			else return new Alternation(left, right);
		}
		public Regex visit(Catenation catenation) {
			Regex left = catenation.left.accept(this);
			Regex right = catenation.right.accept(this);
			if (left == emptyString) return right;
			else if (left == emptySet) return emptySet;
			else return new Catenation(left, right);
		}
		public Regex visit(KleeneClosure kleeneClosure) {
			return new KleeneClosure(kleeneClosure.node.accept(this));
		}
	}
	public static final CompactionVisitor compactor = new CompactionVisitor();
	public static class StringVisitor implements Visitor<String> {
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
	}
	private static final StringVisitor printer = new StringVisitor();
	public static Regex alpha() {
		Regex[] regexen = new Regex[26];
		for (int i = 0; i < regexen.length; i++) {
			regexen[i] = symbol((char) ('a' + i));
		}
		return alternation(regexen);
	}
	public static Regex digit() {
		return alternation(symbol('0'), symbol('1'), symbol('2'),symbol('3'), symbol('4'),
				symbol('5'), symbol('6'), symbol('7'), symbol('8'), symbol('9'));
	}
	public static Regex positiveClosure (Regex r) {
		return catenation(r, kleeneClosure(r));
	}
	public static void main (String[] args) {
		Regex r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(r);
		System.out.println(r.recognize("sjung@gmail.com"));
	}
}
