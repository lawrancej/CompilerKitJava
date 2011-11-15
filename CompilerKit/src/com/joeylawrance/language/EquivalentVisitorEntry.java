package com.joeylawrance.language;

import com.joeylawrance.language.parsers.EquivalentExpression;
import com.joeylawrance.visitor.DefaultVisitorEntry;

public class EquivalentVisitorEntry<S,T> extends DefaultVisitorEntry<Parser,S,T> {
	@Override
	public T visit(S node) {
		return getParent().visit(((EquivalentExpression)node).getEquivalent());
	}
}
