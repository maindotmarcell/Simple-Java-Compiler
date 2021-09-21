import java.util.Optional;

public class Token {

	public enum TokenType implements Symbol {
		PLUS, MINUS, TIMES, DIVIDE, MOD, ASSIGN, EQUAL, NEQUAL, LT, LE, GT, GE, LPAREN, RPAREN, LBRACE, RBRACE, AND, OR,
		SEMICOLON, PUBLIC, CLASS, STATIC, VOID, MAIN, STRINGARR, ARGS, TYPE, PRINT, WHILE, FOR, IF, ELSE, DQUOTE,
		SQUOTE, ID, NUM, CHARLIT, TRUE, FALSE, STRINGLIT;

		@Override
		public boolean isVariable() {
			return false;
		}

	};

	private TokenType type;
	private Optional<String> value;

	public Token(TokenType type) {
		this.type = type;
		this.value = Optional.empty();
	}

	public Token(TokenType type, String value) {
		this.type = type;
		this.value = Optional.of(value);
	}

	public Optional<String> getValue() {
		return this.value;
	}

	public TokenType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		switch (type) {
		case ID :
		case NUM :
		case CHARLIT :
		case TYPE :
		case STRINGLIT : return "[" + type + ": " + value + "]";
		default : return "[" + type + "]";
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (!Token.class.isAssignableFrom(other.getClass())) return false;
		
		Token t = (Token) other;
		
		if (t.type != this.type) return false;
		
		if (t.value == null || this.value == null) return t.value == this.value;
		
		return t.value.equals(this.value);
	}	

}
