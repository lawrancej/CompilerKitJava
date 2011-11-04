package com.joeylawrance.visitor;

public class IdentityVisitor<S> implements Visitor<S, S> {

	@Override
	public S visit(S node) {
		return node;
	}

}
