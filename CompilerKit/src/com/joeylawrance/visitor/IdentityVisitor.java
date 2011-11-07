package com.joeylawrance.visitor;

/**
 * Convenience class to register visitors that merely return nodes.
 *
 * @param <BaseNodeType> The node type used in VisitorMap
 * @param <NodeType>     The node type (must be a subclass of BaseNodeType)
 */
public class IdentityVisitor<BaseNodeType,NodeType> extends DefaultVisitorEntry<BaseNodeType,NodeType,BaseNodeType> {

	@SuppressWarnings("unchecked")
	@Override
	public BaseNodeType visit(NodeType node) {
		return (BaseNodeType )node;
	}

}
