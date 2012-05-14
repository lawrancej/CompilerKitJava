package com.joeylawrance.lambda;

import java.util.HashMap;

// t s
public class Application implements Term {
	private static final HashMap<Term, HashMap<Term, Application>> map = new HashMap<Term, HashMap<Term, Application>>();
	private Term t, s;
	private Application (Term t, Term s) {
		this.t = t;
		this.s = s;
	}
	public Term getLeft() {
		return t;
	}
	public Term getRight() {
		return s;
	}
	public static Application build(Term t, Term s) {
		HashMap<Term,Application> termMap;
		if (!map.containsKey(t)) {
			termMap = new HashMap<Term,Application> ();
			map.put(t, termMap);
		} else {
			termMap = map.get(t);
		}
		if (!termMap.containsKey(s)) {
			termMap.put(s, new Application(t, s));
		}
		return termMap.get(s);
	}
}
