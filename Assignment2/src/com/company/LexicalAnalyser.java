package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.NoSuchElementException;

public class LexicalAnalyser {

	public static List<Token> analyse(String sourceCode) throws LexicalException {
		// Turn the input String into a list of Tokens!
		String[] splitList = sourceCode.split(" ");
		List<Token> tokenList = new ArrayList<Token>();
		List<Token> furtherSplitList = new ArrayList<Token>(); // TODO 
		for (String s : splitList) {
			try {
				tokenList.add(LexicalAnalyser.tokenFromString(s).get());
			}
			catch (NoSuchElementException e) {
				// tokenList.add(Optional.empty());
			}
		}
		return tokenList;
		// return Collections.emptyList();
	}

	private static Optional<Token> tokenFromString(String t) {
		Optional<Token.TokenType> type = tokenTypeOf(t);
		if (type.isPresent())
			return Optional.of(new Token(type.get(), t));
		return Optional.empty();
	}

	private static Optional<Token.TokenType> tokenTypeOf(String t) {
		switch (t) {
		case "public":
			return Optional.of(Token.TokenType.PUBLIC);
		case "class":
			return Optional.of(Token.TokenType.CLASS);
		case "static":
			return Optional.of(Token.TokenType.STATIC);
		case "main":
			return Optional.of(Token.TokenType.MAIN);
		case "{":
			return Optional.of(Token.TokenType.LBRACE);
		case "void":
			return Optional.of(Token.TokenType.VOID);
		case "(":
			return Optional.of(Token.TokenType.LPAREN);
		case "String[]":
			return Optional.of(Token.TokenType.STRINGARR);
		case "args":
			return Optional.of(Token.TokenType.ARGS);
		case ")":
			return Optional.of(Token.TokenType.RPAREN);
		case "int":
		case "char":
		case "boolean":
			return Optional.of(Token.TokenType.TYPE);
		case "=":
			return Optional.of(Token.TokenType.ASSIGN);
		case ";":
			return Optional.of(Token.TokenType.SEMICOLON);
		case "if":
			return Optional.of(Token.TokenType.IF);
		case "for":
			return Optional.of(Token.TokenType.FOR);
		case "while":
			return Optional.of(Token.TokenType.WHILE);
		case "==":
			return Optional.of(Token.TokenType.EQUAL);
		case "+":
			return Optional.of(Token.TokenType.PLUS);
		case "-":
			return Optional.of(Token.TokenType.MINUS);
		case "*":
			return Optional.of(Token.TokenType.TIMES);
		case "/":
			return Optional.of(Token.TokenType.DIVIDE);
		case "%":
			return Optional.of(Token.TokenType.MOD);
		case "}":
			return Optional.of(Token.TokenType.RBRACE);
		case "else":
			return Optional.of(Token.TokenType.ELSE);
		case "System.out.println":
			return Optional.of(Token.TokenType.PRINT);
		case "||":
			return Optional.of(Token.TokenType.OR);
		case "&&":
			return Optional.of(Token.TokenType.AND);
		case "true":
			return Optional.of(Token.TokenType.TRUE);
		case "false":
			return Optional.of(Token.TokenType.FALSE);
		}
		System.out.println(t);
		if (t.matches("\\d+")) {
			System.out.print(t);
			return Optional.of(Token.TokenType.NUM);
		}
		if (Character.isAlphabetic(t.charAt(0)) && t.matches("[\\d|\\w]+")) {
			return Optional.of(Token.TokenType.ID);
		}
		return Optional.empty();
	}

}
