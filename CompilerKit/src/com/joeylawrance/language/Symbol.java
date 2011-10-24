package com.joeylawrance.language;

import com.joeylawrance.language.Regular.Expression;

class Symbol extends Expression {
	final char c;
	public Symbol (char c) { this.c = c; }
}