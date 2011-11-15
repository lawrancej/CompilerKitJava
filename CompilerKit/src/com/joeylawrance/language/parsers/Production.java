package com.joeylawrance.language.parsers;

import java.util.HashMap;

import com.joeylawrance.language.Grammar;
import com.joeylawrance.language.Parser;

public class Production extends Grammar implements EquivalentExpression, UnaryOperator {
	private static final HashMap<Parser,Production> flyweight = new HashMap<Parser,Production>();
	private Parser node;
	private Production(Parser rhs) {
		node = rhs;
	}
	public Parser getNode() {
		return node;
	}
	public static Production build(Parser ... nodes) {
		Parser newNode;
		if (nodes.length > 1)
			newNode = Catenation.catenation(nodes);
		else if (nodes.length == 1)
			newNode = nodes[0];
		else newNode = EmptyString.emptyString;
		if (!flyweight.containsKey(newNode)) {
			flyweight.put(newNode, new Production(newNode));
		}
		return flyweight.get(newNode);
	}
	public Parser getEquivalent() {
		return node;
	}
}