package com.joeylawrance.language;

import com.joeylawrance.language.parsers.CFG;


abstract class ContextFreeVisitor<T> extends RegularVisitor<T> {
	public abstract T visit(Nonterminal nonterminal);
	public abstract T visit(CFG cfg);		
}