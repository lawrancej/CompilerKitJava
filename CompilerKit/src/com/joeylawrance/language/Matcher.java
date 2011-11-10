package com.joeylawrance.language;

class Matcher {
	public static boolean recognize (Parser parser, CharSequence str, DerivativeVisitor<Parser,Parser> derivative) {
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
