package com.joeylawrance.visitor;

import java.util.HashMap;

/**
 * MemoizedVisitorEntry is a wrapper around VisitorEntries that caches results.
 *
 * @param <BaseNodeType> The base node type
 * @param <NodeType>     The specific node type
 * @param <ReturnType>   The return type
 */
public class MemoizedVisitorEntry<BaseNodeType, NodeType, ReturnType> implements VisitorEntry<BaseNodeType, NodeType, ReturnType> {
	// table maps the current state and node type to the return type
	private static HashMap<Object,HashMap<Object, Object>> table = new HashMap<Object, HashMap<Object, Object>>();
	private VisitorMap<BaseNodeType, ReturnType> parent;
	
	private VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor;
	
	public MemoizedVisitorEntry(VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor) {
		this.visitor = visitor;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ReturnType visit(NodeType node) {
		HashMap<Object,Object> memo;
		Object state = parent.getState();
		if (table.containsKey(state)) {
			memo = table.get(state);
		} else {
			memo = new HashMap<Object,Object>();
			table.put(state, memo);
		}
		if (memo.containsKey(node)) {
			return (ReturnType) memo.get(node);
		} else {
			ReturnType result = visitor.visit(node);
			memo.put(node, result);
			return result;
		}
	}
	public Visitor<BaseNodeType, ReturnType> getParent() {
		return parent;
	}
	public void setParent(Visitor<BaseNodeType, ReturnType> parent) {
		this.parent = (VisitorMap<BaseNodeType, ReturnType>)parent;
		visitor.setParent(parent);
	}
}
