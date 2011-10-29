package com.joeylawrance.visitor;

public interface Visitor <S,T> {
	public <U extends T> U visit (S node);
}
