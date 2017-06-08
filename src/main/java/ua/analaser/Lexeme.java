package ua.analaser;

public class Lexeme {
	private String lexemeClass = null;
	private String value;
	private int startIndex;
	private int length;
	
	public Lexeme(String value) {
		this.value = value;
	}
	
	public Lexeme(String value, int start, int len) {
		this(value);
		this.startIndex = start;
		this.length = len;
	}	
	
	public void setLength(int endIndex) {
		this.length = endIndex;
	}
	public int getLength() {
		return getValue().length();
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setLexemeClass(String lexemeClass) {
		this.lexemeClass = lexemeClass;
	}
	public String getLexemeClass() {
		return lexemeClass;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
//		sb.append(getLexemeClass()).append(" : ").append(getValue());
		sb.append(getValue());
		sb.append("  [ ").append(getStartIndex()).append(" / ").append(getLength()).append(" ]");
		
		return sb.toString();
	}
	
	
}
