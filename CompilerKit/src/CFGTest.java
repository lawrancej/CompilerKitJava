import static com.joeylawrance.language.ContextFree.*;

import com.joeylawrance.language.Nonterminal;
import com.joeylawrance.language.Parser;


// TODO: make this an honest to goodness jUnit test suite try for 100% branch coverage
public class CFGTest {
	public static void main(String[] args) {
		Nonterminal value = nonterminal("value");
		Nonterminal product = nonterminal("product");
		Nonterminal sum = nonterminal("sum");
		Nonterminal expr = nonterminal ("expr");
		Parser formula = cfg(expr);

		value.becomes(positiveClosure(digit()));
		value.becomes(parens(expr));
		product.becomes(value, kleeneClosure(catenation(alternation(symbol('*'),symbol('/')), value)));
		sum.becomes(product, kleeneClosure(catenation(alternation(symbol('+'),symbol('-')),product)));
		expr.becomes(sum);
		System.out.println(formula);
/*
		Nonterminal list = nonterminal("List");
		CFG listCFG = new CFG(list);
		list.becomes(catenation(list,symbol('x')));
		list.becomes(symbol('x'));
		System.out.println(listCFG);
		System.out.println(recognize(listCFG,"x")); */
		Nonterminal s = nonterminal("S"); 
		Parser cfg = cfg(s);
		s.becomes(s,parens(s));
		s.becomes();
		System.out.println(cfg);
		System.out.println(recognize(cfg,"()")); // should return false
	}

}
