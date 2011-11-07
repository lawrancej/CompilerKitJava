package com.joeylawrance.visitor;

public abstract class DefaultVisitorEntry<R,S,T> implements VisitorEntry<R, S, T> {
	Visitor<R,T> parent;
	@Override
	public Visitor<R, T> getParent() {
		return parent;
	}

	@Override
	public void setParent(Visitor<R, T> parent) {
		this.parent = parent;
	}

}
