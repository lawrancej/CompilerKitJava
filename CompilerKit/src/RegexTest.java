import static com.joeylawrance.language.Regular.*;

public class RegexTest {

	public static void main(String[] args) {
		Parser r = catenation(positiveClosure(alpha()), kleeneClosure(digit()), string("@bridgew.edu"));
		System.out.println(r);
		System.out.println(r.recognize("somebody@bridgew.edu"));
		System.out.println(r.recognize("somebody@wit.edu"));
	}

}
