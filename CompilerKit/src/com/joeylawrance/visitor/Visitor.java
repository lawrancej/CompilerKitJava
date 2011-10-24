package com.joeylawrance.visitor;

public abstract class Visitor <S,T> { 
	S root;
	public abstract T visit (S node);
}
