package com.joeylawrance.language;

import com.joeylawrance.language.parsers.EquivalentExpressionImpl;
import com.joeylawrance.visitor.VisitorEntry;

public abstract class EquivalentExpressionVisitor<T> implements VisitorEntry<Parser, EquivalentExpressionImpl, T> {

	@Override
	public T visit(EquivalentExpressionImpl node) {
		return getParent().visit(node.getEquivalent());
	}
	
}
