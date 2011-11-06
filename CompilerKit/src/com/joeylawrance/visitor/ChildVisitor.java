package com.joeylawrance.visitor;

public interface ChildVisitor<R,S,T> extends Visitor<S, T> { // R is the base node type, S is the node type, T is the return type
	public Visitor<R,T> getParent();
}
