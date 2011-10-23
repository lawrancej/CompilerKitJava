import static com.joeylawrance.language.ContextFree.*;

public class CFGTest {
	public static void main(String[] args) {
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
		System.out.println(cfg);
	}

}
