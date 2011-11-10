package com.joeylawrance.language.parsers;

import java.util.ArrayList;

import com.joeylawrance.language.Grammar;
import com.joeylawrance.language.Nonterminal;


/**
 * Publicly-available context-free grammar class.
 */
public class CFG extends Grammar {
	private Nonterminal start;
	private ArrayList <Nonterminal> nonterminals;
	public CFG(Nonterminal start) {
		this.start = start;
	}
	public Nonterminal getStart() {
		return start;
	}
	public ArrayList <Nonterminal> getNonterminals() {
		return nonterminals;
	}
	public void setNonterminals(ArrayList <Nonterminal> nonterminals) {
		this.nonterminals = nonterminals;
	}
}