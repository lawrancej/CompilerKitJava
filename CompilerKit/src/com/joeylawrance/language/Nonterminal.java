package com.joeylawrance.language;

// TODO: becomes should be a static method inside of contextfree. 
// also, have a separate node type, production, equivalent to alternation, that allows us to show each production on its own line
public class Nonterminal extends Grammar {
	Parser node; String name;
	public Nonterminal (String name) { this.name = name; }
	public void becomes (Parser ...nodes) {
		// Construct new node from node parameter
		Parser newNode;
		if (nodes.length > 1)
			newNode = Catenation.catenation(nodes);
		else if (nodes.length == 1)
			newNode = nodes[0];
		else newNode = EmptyString.emptyString;

		// If the nonterminal doesn't have a node, it's newNode
		// Otherwise, its the existing node or newNode
		if (node == null) {
			node = newNode;
		} else {
			node = Alternation.alternation(node,newNode);
		}
	}
}