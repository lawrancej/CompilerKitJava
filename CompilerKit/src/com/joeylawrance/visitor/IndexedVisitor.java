package com.joeylawrance.visitor;

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
	private Visitor<? extends IndexedVisitable, T>[] array;
	@SuppressWarnings("unchecked")
	public IndexedVisitor(int size) { array = new Visitor[size]; }
	public void register(int index, Visitor<? extends IndexedVisitable, T> visitor) {
		array[index] = visitor;
	}
	@SuppressWarnings("unchecked")
	@Override
	public T visit(IndexedVisitable node) {
		return ((Visitor<IndexedVisitable, T>)array[node.getIndex()]).visit(node);
	}

}
