import java.util.ArrayList;

import com.joeylawrance.visitor.IndexedVisitable;
import com.joeylawrance.visitor.IndexedVisitor;
import com.joeylawrance.visitor.LookupVisitor;
import com.joeylawrance.visitor.ReflectiveVisitor;
import com.joeylawrance.visitor.Visitable;
import com.joeylawrance.visitor.Visitor;

public class LookupVisitorTest {
	public static class SlowPrintVisitor extends ReflectiveVisitor<Void> {
		public Void visit(Component node) {
			for (IndexedVisitable v : node.nodes) {
				this.visit(v);
			}
			return null;
		}
		public Void visit(Leaf node) {
			System.out.print(node.str);
			return null;			
		}
	}
	static class Component implements IndexedVisitable {
		ArrayList<IndexedVisitable> nodes = new ArrayList<IndexedVisitable>();

		@Override
		public int getIndex() {
			return 0;
		}
	}
	static Component component(IndexedVisitable...visitables) {
		Component c = new Component();
		for (IndexedVisitable v : visitables) c.nodes.add(v);
		return c;
	}
	static class Leaf implements IndexedVisitable {
		String str;
		@Override
		public int getIndex() {
			return 1;
		}
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
					for (IndexedVisitable v : node.nodes) {
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
	static class IndexedPrintVisitor extends IndexedVisitor<Void> {
		IndexedPrintVisitor() {
			this.register(new Visitor<Component,Void>() {
				@Override
				public Void visit(Component node) {
					for (IndexedVisitable v : node.nodes) {
						IndexedPrintVisitor.this.visit(v);
					}
					return null;
				}
			});
			this.register(new Visitor<Leaf,Void>() {
				@Override
				public Void visit(Leaf node) {
					System.out.print(node.str);
					return null;
				}
			});
		}
	}
	// Sigh... copy paste :-(
	public static long testSlowSpeed() {
		SlowPrintVisitor sv = new SlowPrintVisitor();
		long before = System.currentTimeMillis();
		sv.visit(bench);
		return (System.currentTimeMillis() - before);		
	}
	public static Component benchmark() {
		Component c = component(leaf("0"));
		for (int i = 1; i < 1000; i++) {
			Leaf[] line = new Leaf[1000];
			line[0] = leaf("0");
			for (int j = 1; j < 1000; j++) {
				line[j] = leaf(" "+(i+j));
			}
			c = component(c,component(line),leaf("\n"));
		}
		return c;
	}
	public static Component bench = benchmark();
	public static long testLookupSpeed() {
		PrintVisitor sv = new PrintVisitor();
		
		long before = System.currentTimeMillis();
		sv.visit(bench);
		return (System.currentTimeMillis() - before);		
	}
	public static long testIndexedSpeed() {
		IndexedPrintVisitor sv = new IndexedPrintVisitor();
		long before = System.currentTimeMillis();
		sv.visit(bench);
		return (System.currentTimeMillis() - before);		
	}
	public static void main(String[] args) {
		long slow, lookup, indexed;
		slow = testSlowSpeed();
		System.gc();
		lookup = testLookupSpeed();
		System.gc();
		indexed = testIndexedSpeed();
		System.out.println(slow + " " + lookup + " " + indexed);
	}
}
