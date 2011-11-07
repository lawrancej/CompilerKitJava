package com.joeylawrance.visitor;

import java.util.HashMap;

/**
 * LookupVisitor<S,T>
 * 
 * S is the Visitable type root
 * T is the visitor return type
 * TODO: change parameter type names to readable things, not just single letters
 * TODO: it'd be nice to be able to register multiple classes for the same child visitor
 * 
 * Example usage:
 * 
 * public StringVisitor extends VisitorMap<Parser,String> {
 * 		StringVisitor () {
 * 			register(EmptySet.class, new VisitorEntry<Parser,EmptySet,String>() {
 * 				...
 * 			});
 * 		}
 * }
 */
public class VisitorMap<S,T> implements Visitor<S,T> {
	private HashMap <Class<? extends S>, VisitorEntry<S,? extends S,T>> map = new HashMap <Class<? extends S>, VisitorEntry<S,? extends S,T>>();
	public <R extends S> void register(Class<R> klass, VisitorEntry<S,R,T> visitor) {
		visitor.setParent(this);
		map.put(klass,visitor);
	}
	@SuppressWarnings("unchecked")
	@Override
	public T visit(S node) {
		return ((Visitor<S,T>)map.get(node.getClass())).visit(node);
	}
}

//Visitor provides a visit method
// Lookup Visitor also provides a visit method
// Things being visited do not have visit methods