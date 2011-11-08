package com.joeylawrance.visitor;

import java.util.HashMap;

/**
 * MemoizedVisitorEntry is a wrapper around VisitorEntries that caches results.
 * It assumes that the visit method has no side effects, and the state (if any) can be parameterized to one object.
 *
 * @param <BaseNodeType> The base node type
 * @param <NodeType>     The specific node type
 * @param <ReturnType>   The return type
 */
public class MemoizedVisitorEntry<BaseNodeType, NodeType, ReturnType> implements VisitorEntry<BaseNodeType, NodeType, ReturnType> {
	private static HashMap<Object,HashMap<Object, Object>> table = new HashMap<Object, HashMap<Object, Object>>();
	private static Object state;
	private VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor;
	
	public MemoizedVisitorEntry(VisitorEntry<BaseNodeType, NodeType, ReturnType> visitor) {
		this.visitor = visitor;
	}
	public static void setState(Object state) {
		MemoizedVisitorEntry.state = state; // FIXME: not thread safe :( // you can fix by having a hash map of visitors to states
	}
	@SuppressWarnings("unchecked")
	@Override
	public ReturnType visit(NodeType node) {
		HashMap<Object,Object> memo;
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

	@Override
	public Visitor<BaseNodeType, ReturnType> getParent() {
		return visitor.getParent();
	}

	@Override
	public void setParent(Visitor<BaseNodeType, ReturnType> parent) {
		visitor.setParent(parent);
	}

}
