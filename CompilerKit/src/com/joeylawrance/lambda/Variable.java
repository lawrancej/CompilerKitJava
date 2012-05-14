package com.joeylawrance.lambda;

import java.util.HashMap;

// A variable
public class Variable implements Term {
	private static HashMap<String,Variable> terms = new HashMap<String, Variable>();
	private String name;
	private Variable (String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static Variable build(String name) {
		if (!terms.containsKey(name)) {
			terms.put(name, new Variable(name));
		}
		return terms.get(name);
	}
}
