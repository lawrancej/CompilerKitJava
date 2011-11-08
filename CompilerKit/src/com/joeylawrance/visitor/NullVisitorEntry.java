package com.joeylawrance.visitor;

public class NullVisitorEntry<BaseNodeType,NodeType,ReturnType> extends DefaultVisitorEntry<BaseNodeType,NodeType,ReturnType>{
	public ReturnType visit(NodeType node) { return null; }
}
