import static com.joeylawrance.language.Regular.*;

import com.joeylawrance.language.Parser;

//TODO: make this an honest to goodness jUnit test suite try for 100% branch coverage
public class RegexTest {

	public static void main(String[] args) {
		Parser r = catenation(times(alpha(),3,8), times(digit(),0,4), string("@bridgew.edu"));
		long before = System.currentTimeMillis();
		System.out.println(r);
		for (int i = 0; i < 10000; i++) {
			recognize(r,"somebody"+i+"@bridgew.edu");
			recognize(r,"somebody"+i+"@wit.edu");
		}
		System.out.println(System.currentTimeMillis() - before); // 125ms for 10000 iterations is MUCH better than before
		r = catenation(not(symbol('\n')), string("\n"));
		System.out.println(r);
		System.out.println(recognize(r,"abc\n\n"));
		r = difference(alnum(),digit());
		System.out.println(r);
		System.out.println(recognize(r,"a"));
	}

}
