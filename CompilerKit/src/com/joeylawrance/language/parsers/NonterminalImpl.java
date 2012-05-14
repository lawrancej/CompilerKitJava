package com.joeylawrance.language.parsers;

import java.util.ArrayList;

import com.joeylawrance.language.Grammar;
import com.joeylawrance.language.Parser;
import com.joeylawrance.language.Nonterminal;

// TODO: becomes should be a static method inside of contextfree. 
// also, have a separate node type, production, equivalent to alternation, that allows us to show each production on its own line
public class NonterminalImpl extends Grammar implements Nonterminal {
	private ArrayList<Parser> productions = new ArrayList<Parser>();
	private String name;
	public NonterminalImpl (String name) { this.name = name; }
	public void becomes (Parser ... nodes) {
		// Construct new node from node parameter
		Parser newNode;
		if (nodes.length > 1)
			newNode = Catenation.catenation(nodes);
		else if (nodes.length == 1)
			newNode = nodes[0];
		else newNode = EmptyString.emptyString;
		productions.add(newNode);
	}
	public Iterable<Parser> getProductions() {
		return productions;
	}
	public Parser getNode() {
		return Alternation.alternation(productions.toArray(new Parser[productions.size()]));
	}
	public String getName() {
		return name;
	}
}