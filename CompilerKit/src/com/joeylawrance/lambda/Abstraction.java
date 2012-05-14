package com.joeylawrance.lambda;

import java.util.HashMap;

// λx.t
public class Abstraction implements Term {
	private static final HashMap<Variable,HashMap<Term,Abstraction>> map = new HashMap<Variable,HashMap<Term,Abstraction>>();
	private Variable variable;
	private Term term;
	private Abstraction(Variable x, Term t) {
		variable = x;
		term = t;
	}
	public Variable getVariable() {
		return variable;
	}
	public Term getTerm() {
		return term;
	}
	public static Abstraction build(Variable x, Term t) {
		HashMap<Term,Abstraction> varMap;
		if (!map.containsKey(x)) {
			varMap = new HashMap<Term,Abstraction>();
			map.put(x, varMap);
		} else {
			varMap = map.get(x);
		}
		if (!varMap.containsKey(t)) {
			varMap.put(t, new Abstraction(x,t));
		}
		return varMap.get(t);
	}
}
