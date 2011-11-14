import static com.joeylawrance.language.Regular.*;

import com.joeylawrance.language.Parser;

//TODO: make this an honest to goodness jUnit test suite try for 100% branch coverage
public class RegexTest {

	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		Parser r = catenation(times(alpha(),3,8), positiveClosure(digit()), string("@bridgew.edu"));
		System.gc();
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		System.out.println(r);
		int i;
		long before = System.currentTimeMillis();
		for (i = 0; i < 10000; i++) {
			if (!recognize(r,"somebody"+i+"@bridgew.edu")) break;
			if (recognize(r,"somebody"+i+"@wit.edu")) break;
			if (!recognize(r,"somebody"+i+"@bridgew.edu")) break;
			if (recognize(r,"somebody"+i+"@wit.edu")) break;
		}
		System.out.println(System.currentTimeMillis() - before); // 125ms for 10000 iterations is MUCH better than before
		System.gc();
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		System.out.println(i);
		r = catenation(not(symbol('\n')), string("\n"));
		System.out.println(r);
		System.out.println(recognize(r,"abc\n\n"));
		r = difference(alnum(),digit());
		System.out.println(r);
		System.out.println(recognize(r,"a"));
	}

}
