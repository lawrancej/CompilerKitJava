package com.joeylawrance.visitor;

public interface Visitor <S,T> {
	public T visit (S node); // Maybe make this ? extends T
}
