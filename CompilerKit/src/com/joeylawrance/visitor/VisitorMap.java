package com.joeylawrance.visitor;

import java.util.HashMap;

// TODO: it'd be nice to be able to register multiple classes for the same child visitor

/**
 * LookupVisitor<BaseNodeType,ReturnType>
 * 
 * BaseNodeType is the is Visitable node type
 * ReturnType is what the visit method returns
 * 
 * Example usage:
 * 
 * public StringVisitor extends VisitorMap<Parser,String> {
 * 		StringVisitor () {
 * 			register(EmptySet.class, new VisitorEntry<Parser,EmptySet,String>() {
 * 				...
 * 			});
 * 		}
 * }
 */
public class VisitorMap<BaseNodeType,ReturnType> implements Visitor<BaseNodeType,ReturnType> {
	private HashMap <Class<? extends BaseNodeType>, VisitorEntry<BaseNodeType,? extends BaseNodeType,ReturnType>> map = new HashMap <Class<? extends BaseNodeType>, VisitorEntry<BaseNodeType,? extends BaseNodeType,ReturnType>>();
	public <NodeType extends BaseNodeType> void register(Class<NodeType> klass, VisitorEntry<BaseNodeType,NodeType,ReturnType> visitor) {
		visitor.setParent(this);
		map.put(klass,visitor);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ReturnType visit(BaseNodeType node) {
		return ((Visitor<BaseNodeType,ReturnType>)map.get(node.getClass())).visit(node);
	}
}
