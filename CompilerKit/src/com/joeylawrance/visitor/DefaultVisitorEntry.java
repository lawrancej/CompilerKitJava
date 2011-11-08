package com.joeylawrance.visitor;

/**
 * Default Visitor Entry implements the getParent() and setParent(Visitor) methods
 *
 * @param <BaseNodeType> The base node type
 * @param <NodeType>     The specific node type
 * @param <ReturnType>   The return type
 */
public abstract class DefaultVisitorEntry<BaseNodeType,NodeType,ReturnType> implements VisitorEntry<BaseNodeType, NodeType, ReturnType> {
	Visitor<BaseNodeType,ReturnType> parent;
	public Visitor<BaseNodeType, ReturnType> getParent() {
		return parent;
	}
	public void setParent(Visitor<BaseNodeType, ReturnType> parent) {
		this.parent = parent;
	}
}
