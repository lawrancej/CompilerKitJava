package com.joeylawrance.language;

public interface Nonterminal extends Parser {
	public void becomes (Parser ... nodes);

	public Iterable<Parser> getProductions();
	public Parser getNode();
	public String getName();
}
