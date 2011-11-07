package com.joeylawrance.visitor;

public interface VisitorEntry<BaseNodeType,NodeType,ReturnType> extends Visitor<NodeType, ReturnType> {
	public Visitor<BaseNodeType,ReturnType> getParent();
	public void setParent(Visitor<BaseNodeType,ReturnType> parent);
}
