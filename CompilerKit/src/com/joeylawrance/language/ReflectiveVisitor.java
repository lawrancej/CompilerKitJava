package com.joeylawrance.language;

import java.lang.reflect.Method;

import com.joeylawrance.visitor.Visitor;

abstract class ReflectiveVisitor<T> implements Visitor<Object, T> {
	@SuppressWarnings("unchecked")
	public T visit(Object object)
	{
		try {
			Method m = getClass().getMethod("visit", new Class[] {object.getClass()});
			return (T) m.invoke(this, new Object[] {object});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
