package com.joeylawrance.visitor;

public class FixedPointVisitor<BaseNodeType,NodeType,ReturnType> extends DefaultVisitorEntry<BaseNodeType,NodeType,ReturnType> {
	VisitorEntry<BaseNodeType,NodeType,ReturnType> visitor;
	public FixedPointVisitor (VisitorEntry<BaseNodeType,NodeType,ReturnType> visitor) {
		this.visitor = visitor;
	}
	@Override
	public ReturnType visit(NodeType node) {
		return visitor.visit(node);
	}
}
