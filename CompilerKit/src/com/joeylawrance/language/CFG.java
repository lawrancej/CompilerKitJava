package com.joeylawrance.language;

import java.util.ArrayList;


/**
 * Publicly-available context-free grammar class.
 */
public class CFG extends Grammar {
	Nonterminal start;
	ArrayList <Nonterminal> nonterminals;
	public CFG(Nonterminal start) {
		this.start = start;
	}
}