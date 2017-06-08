package ua.analaser;

public class LexicalException extends Exception {

    private int currentLookAhead;

    private int expectedLookahead;

    private int lineNumber;

    private int startIndex;

    private int lexemeLength;

    private String currentLexeme;

    private String expectedLexeme;

	public LexicalException (String message) {
		super(message);
	}

    public LexicalException (String lexeme, String exLexeme, int lookahead, int lineNumber) {
		super();

        this.currentLookAhead = lookahead;
        this.lineNumber = lineNumber;
        this.currentLexeme = lexeme;
        this.expectedLexeme = exLexeme;
	}

    public LexicalException (String lexeme, int expLookahead, int lookahead, int lineNumber) {
		super();

        this.currentLookAhead = lookahead;
        this.lineNumber = lineNumber;
        this.currentLexeme = lexeme;
        this.expectedLookahead = expLookahead;
	}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Line Number: ").append(lineNumber).append(". ");
        if (this.expectedLexeme != null) {
            sb.append("Expected: ").append(expectedLexeme).append(". "); //TODO
        } else {
            sb.append("Expected: ").append('\"')
                    .append(getLexeme(expectedLookahead, currentLexeme)).append('\"').append(". "); //TODO
        }
        sb.append("But was: ").append('\"')
                .append(getLexeme(currentLookAhead, currentLexeme)).append('\"').append(". ");
        sb.append("Current lexeme: ").append(currentLexeme);

        return sb.toString();
    }

    public String getLexeme(int lookahead, String tokenValue) {
        String result = "";
		switch (lookahead) {
			case '+':
			case '-':
			case '*':
			case '/':
			case '=':
			case '>':
			case '<':
			case ':':
			case ',':
			case '(':
			case ')':
			case ';':
				result += (char) lookahead;
				break;
			case SimplePascalParser.BEGIN:
				result = "Begin";
				break;
			case SimplePascalParser.END:
				result = "End";
				break;
			case SimplePascalParser.IF:
				result = "If";
				break;
			case SimplePascalParser.THEN:
				result = "then";
				break;
			case SimplePascalParser.WHILE_CICLE:
				result = "while";
				break;
			case SimplePascalParser.DO_CICLE:
				result = "Do";
				break;
			case SimplePascalParser.ELSE:
				result = "else";
				break;
			case SimplePascalParser.TRUE:
				result = "true";
				break;
			case SimplePascalParser.OR:
				result = "or";
				break;
			case SimplePascalParser.AND:
				result = "and";
				break;
			case SimplePascalParser.VAR_DEFINITIOIN:
                result = "Var";
				break;
			case SimplePascalParser.STRING:
                result = "String";
				break;
			case SimplePascalParser.CHAR:
               result = "Char";
				break;
			case SimplePascalParser.INTEGER:
                result = "Integer";
				break;
			case SimplePascalParser.DOUBLE:
				result = "Double";
				break;
			case SimplePascalParser.ID:
			case SimplePascalParser.STRING_CONSTANT:
			case SimplePascalParser.NUMBER:
			case SimplePascalParser.METHOD:
			case SimplePascalParser.FUNCTION:
				result = tokenValue;
				break;
			default:
				break;

		}

        return result;
    }

    public int getCurrentLookAhead() {
        return currentLookAhead;
    }

    public void setCurrentLookAhead(int currentLookAhead) {
        this.currentLookAhead = currentLookAhead;
    }

    public String getCurrentLexeme() {
        return currentLexeme;
    }

    public void setCurrentLexeme(String currentLexeme) {
        this.currentLexeme = currentLexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLexemeLength() {
        return lexemeLength;
    }

    public void setLexemeLength(int lexemeLength) {
        this.lexemeLength = lexemeLength;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
