package com.joeylawrance.visitor;

import java.util.HashMap;

/**
 * LookupVisitor<S,T>
 * 
 * S is the Visitable type root
 * T is the visitor return type
 * 
 * Example usage:
 * 
 * public StringVisitor extends LookupVisitor<Parser,String> {
 * 		StringVisitor () {
 * 			register(EmptySet.class, new Visitor<EmptySet,String>() {
 * 				...
 * 			});
 * 		}
 * }
 */
public class LookupVisitor<S,T> implements Visitor<S,T> {
	private HashMap <Class<? extends S>, Visitor<? extends S,T>> map = new HashMap <Class<? extends S>, Visitor<? extends S,T>>();
	public void register(Class<? extends S> klass, Visitor<? extends S,T> visitable) { map.put(klass, visitable); }
	public T visit(S node) {
		return ((Visitor<S,T>)map.get(node.getClass())).visit(node);
	}
}

//Visitor provides a visit method
// Lookup Visitor also provides a visit method
// Things being visited do not have visit methods