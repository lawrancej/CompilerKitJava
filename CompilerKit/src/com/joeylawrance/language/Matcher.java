package com.joeylawrance.language;

public class Matcher {
	public static boolean recognize (Parser parser, CharSequence str) { // TODO: Move outside to Match class, because of visibility issues and besides, this wont work with Regular and Context-Free
		DerivativeVisitor<Parser,Parser> derivative = parser.getDerivative();
		for (int i = 0; i < str.length(); i++) {
			derivative.setSymbol(str.charAt(i));
//			System.out.print("Symbol:" + derivative.getSymbol());
//			System.out.println("before: " + parser);
			parser = derivative.visit(parser);
//			System.out.println("after: " + parser);
			if (parser == EmptySet.emptySet) break;
		}
		return derivative.getNullable().visit(parser) == EmptyString.emptyString;
	}
}
