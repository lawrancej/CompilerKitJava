package com.joeylawrance.language.parsers;

import com.joeylawrance.language.Parser;

public interface BinaryOperator {
	public Parser getLeft();
	public Parser getRight();
}
