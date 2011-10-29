package com.joeylawrance.language;

import com.joeylawrance.visitor.Visitor;

interface DerivativeVisitor<S,T> extends Visitor<S, T> {
	public char getSymbol();
	public void setSymbol(char c);
	public Visitor<S,T> getNullable();
}
