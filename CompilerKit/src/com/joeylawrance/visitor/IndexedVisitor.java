package com.joeylawrance.visitor;

import java.util.ArrayList;

/**
 * IndexedVisitor<S,T>
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
public class IndexedVisitor<T> implements Visitor<IndexedVisitable,T> {
	private ArrayList<Visitor<? extends IndexedVisitable, T>> array = new ArrayList<Visitor<? extends IndexedVisitable, T>>();
	public void register(Visitor<? extends IndexedVisitable, T> visitor) {
		array.add(visitor);
	}
	@SuppressWarnings("unchecked")
	@Override
	public T visit(IndexedVisitable node) {
		return ((Visitor<IndexedVisitable, T>)array.get(node.getIndex())).visit(node);
	}

}
