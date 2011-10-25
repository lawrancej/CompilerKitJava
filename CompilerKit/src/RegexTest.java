import static com.joeylawrance.language.Regular.*;

import com.joeylawrance.language.Parser;

public class RegexTest {

	public static void main(String[] args) {
		long before = System.currentTimeMillis();
		Parser r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(r);
		System.out.println(r.recognize("somebody@bridgew.edu"));
		System.out.println(r.recognize("somebody@wit.edu"));
		System.out.println(System.currentTimeMillis() - before); // 144ms is bad // 37ms is better
	}

}
