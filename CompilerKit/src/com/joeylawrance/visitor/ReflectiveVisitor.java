package com.joeylawrance.visitor;

import java.lang.reflect.Method;


public abstract class ReflectiveVisitor<T> implements Visitor<Object, T> {
	@SuppressWarnings("unchecked")
	public T visit(Object object)
	{
		try {
			Method m = getClass().getMethod("visit", new Class[] {object.getClass()});
			m.setAccessible(true);
			return (T) m.invoke(this, new Object[] {object});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
