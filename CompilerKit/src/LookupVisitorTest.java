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
	static class PrintVisitor extends LookupVisitor<Visitable,Void> {
		PrintVisitor() {
			this.register(Component.class, new Visitor<Component,Void>() {
				@Override
				public Void visit(Component node) {
					for (Visitable v : node.nodes) {
						PrintVisitor.this.visit(v);
					}
					return null;
				}
			});
			this.register(Leaf.class, new Visitor<Leaf,Void>() {
				@Override
				public Void visit(Leaf node) {
					System.out.print(node.str);
					return null;
				}
			});
		}
	}
	public static void main(String[] args) {
		long before = System.currentTimeMillis();
		Component c = component(leaf("0"));
		PrintVisitor sv = new PrintVisitor();
		sv.visit(c);
		for (int i = 1; i < 1000; i++) {
			Leaf[] line = new Leaf[1000];
			line[0] = leaf("0");
			for (int j = 1; j < 1000; j++) {
				line[j] = leaf(" "+(i+j));
			}
			c = component(c,component(line),leaf("\n"));
		}
		sv.visit(c);
		System.out.println(System.currentTimeMillis() - before);
	}
}
