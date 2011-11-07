package com.joeylawrance.visitor;

/**
 * Visitors visit nodes and return a value.
 *
 * @param <NodeType>    The type of the node
 * @param <ReturnType>  The return type
 */
public interface Visitor <NodeType,ReturnType> {
	/**
	 * The visit method visits the node and returns a result
	 * @param node The node to visit
	 * @return     Whatever the visitor should return
	 */
	public ReturnType visit (NodeType node);
}
