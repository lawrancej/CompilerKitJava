package com.joeylawrance.visitor;

import java.util.ArrayList;

/**
 * IndexedVisitor<T>
 * 
 * T is the visitor return type
 * 
 * Example usage:
 * 
 * public StringVisitor extends IndexedVisitor2<String> {
 * 		StringVisitor () {
 * 			register(new Visitor<EmptySet,String>() {
 * 				...
 * 			});
 * 		}
 * }
 */
public class IndexedVisitor2<T> implements Visitor<IndexedVisitable,T> {
	private ArrayList<Visitor<? extends IndexedVisitable, T>> array = new ArrayList<Visitor<?extends IndexedVisitable, T>>();
	public void register(Visitor<? extends IndexedVisitable, T> visitor) {
		array.add(visitor);
	}
	@SuppressWarnings("unchecked")
	@Override
	public T visit(IndexedVisitable node) {
		return ((Visitor<IndexedVisitable, T>)array.get(node.getIndex())).visit(node);
	}

}
