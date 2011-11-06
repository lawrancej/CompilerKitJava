package com.joeylawrance.language;

import com.joeylawrance.visitor.ChildVisitor;

public abstract class EquivalentExpressionVisitor<T> implements ChildVisitor<Parser, EquivalentExpression, T> {

	@Override
	public T visit(EquivalentExpression node) {
		return getParent().visit(node.getEquivalent());
	}
	
}
