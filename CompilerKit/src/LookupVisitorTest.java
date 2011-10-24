import java.util.ArrayList;

import com.joeylawrance.visitor.LookupVisitor;
import com.joeylawrance.visitor.Visitable;
import com.joeylawrance.visitor.Visitor;

public class LookupVisitorTest {
	static class Component implements Visitable {
		ArrayList<Visitable> nodes = new ArrayList<Visitable>();
	}
	static Component component(Visitable...visitables) {
		Component c = new Component();
		for (Visitable v : visitables) c.nodes.add(v);
		return c;
	}
	static class Leaf implements Visitable {
		String str;
	}
	static Leaf leaf(String str) {
		Leaf l = new Leaf();
		l.str = str;
		return l;
	}
	static class StringVisitor extends LookupVisitor<Visitable,String> {
		StringVisitor() {
			this.register(Component.class, new Visitor<Component,String>() {
				@Override
				public String visit(Component node) {
					StringBuffer buf = new StringBuffer();
					for (Visitable v : node.nodes) {
						buf.append(StringVisitor.this.visit(v));
					}
					return buf.toString();
				}
			});
			this.register(Leaf.class, new Visitor<Leaf,String>() {
				@Override
				public String visit(Leaf node) {
					return node.str;
				}
			});
		}
	}
	public static void main(String[] args) {
		Component c = component(leaf("hello"),leaf("world"));
		StringVisitor sv = new StringVisitor();
		System.out.println(sv.visit(c));
	}
}
