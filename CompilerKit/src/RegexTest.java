import static com.joeylawrance.language.Regular.*;
import com.joeylawrance.language.Parser;

//TODO: make this an honest to goodness jUnit test suite try for 100% branch coverage
public class RegexTest {

	public static void main(String[] args) {
		Parser r = catenation(times(alpha(),3,8), optional(digit()), string("@bridgew.edu"));
		long before = System.currentTimeMillis();
		System.out.println(r);
		System.out.println(r.recognize("somebody@bridgew.edu"));
		System.out.println(r.recognize("somebody@wit.edu"));
		System.out.println(System.currentTimeMillis() - before); // 12ms is better than before
		r = catenation(not(symbol('\n')), string("\n"));
		System.out.println(r);
		System.out.println(r.recognize("abc\n\n"));
		r = difference(alnum(),digit());
		System.out.println(r);
		System.out.println(r.recognize("a"));
	}

}
