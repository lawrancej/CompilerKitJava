package com.joeylawrance.language;

import java.util.ArrayList;

import com.joeylawrance.language.parsers.Alternation;
import com.joeylawrance.language.parsers.Catenation;
import com.joeylawrance.language.parsers.EmptyString;
import com.joeylawrance.language.parsers.Production;

// TODO: becomes should be a static method inside of contextfree. 
// also, have a separate node type, production, equivalent to alternation, that allows us to show each production on its own line
public class Nonterminal extends Grammar {
	private ArrayList<Production> productions = new ArrayList<Production>();
	private Parser node;
	private String name;
	public Nonterminal (String name) { this.name = name; }
	public void becomes (Parser ... nodes) {
		// Construct new node from node parameter
		Production newNode = Production.build(nodes);

		// If the nonterminal doesn't have a node, it's newNode
		// Otherwise, its the existing node or newNode
		if (getNode() == null) {
			this.node = newNode;
		} else {
			this.node = Alternation.alternation(getNode(),newNode);
		}
	}
	public Parser getNode() {
		return node;
	}
	public String getName() {
		return name;
	}
}