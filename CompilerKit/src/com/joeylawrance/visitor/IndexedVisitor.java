package com.joeylawrance.visitor;

/**
 * IndexedVisitor<T>
 * 
 * T is the visitor return type
 * 
 * Example usage:
 * 
 * public StringVisitor extends IndexedVisitor<String> {
 * 		StringVisitor () {
 * 			register(EMPTYSET, new Visitor<EmptySet,String>() {
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
