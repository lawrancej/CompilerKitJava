package com.joeylawrance.visitor;

import java.util.HashMap;

// TODO: make a MemoizedVisitorEntry class that handles side effects
/**
 * MemoizedVisitorEntry is a wrapper around VisitorEntries that caches results.
 * It assumes the visit computation has no side-effects.
 *
 * @param <BaseNodeType> The base node type
 * @param <NodeType>     The specific node type
 * @param <ReturnType>   The return type
 */
public class MemoizedVisitorEntry<BaseNodeType, NodeType, ReturnType> implements VisitorEntry<BaseNodeType, NodeType, ReturnType> {
	private static HashMap<Object, Object> table = new HashMap<Object, Object>();
	
	private VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor;
	public MemoizedVisitorEntry(VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor) {
		this.visitor = visitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReturnType visit(NodeType node) {
		if (table.containsKey(node)) {
			return (ReturnType) table.get(node);
		} else {
			ReturnType result = visitor.visit(node);
			table.put(node, result);
			return result;
		}
	}

	@Override
	public Visitor<BaseNodeType, ReturnType> getParent() {
		return visitor.getParent();
	}

	@Override
	public void setParent(Visitor<BaseNodeType, ReturnType> parent) {
		visitor.setParent(parent);
	}

}
