package com.joeylawrance.language;


abstract class ContextFreeVisitor<T> extends RegularVisitor<T> {
	public abstract T visit(Nonterminal nonterminal);
	public abstract T visit(CFG cfg);		
}