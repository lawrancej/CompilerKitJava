package com.joeylawrance.language;

import com.joeylawrance.visitor.VisitorEntry;

public abstract class EquivalentExpressionVisitor<T> implements VisitorEntry<Parser, EquivalentExpression, T> {

	@Override
	public T visit(EquivalentExpression node) {
		return getParent().visit(node.getEquivalent());
	}
	
}
