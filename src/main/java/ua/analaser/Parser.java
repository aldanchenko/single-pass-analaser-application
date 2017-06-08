package ua.analaser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
	public static final int BUFFER_SIZE = 128;
	public static final int EOS = '\0';
	public static final int EOF = -1;
	
	public static final int NUM = 256;	
	public static final int DIV = 257;	
	public static final int MOD = 258;	
	public static final int ID = 259;	
	public static final int DONE = 260;
	
	public static final int NONE = -1;	
	
	private static int lookahead;
	
	private static int lineNumber = 1;
	
	private static int tokenValue;
	
	private static String lexBuffer = "";
	
	private static Map<String, Integer> symbolTable = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws Exception {
		// init
		
		symbolTable.put("div", DIV);
		symbolTable.put("mod", MOD);
		symbolTable.put("0", 0);
		
		parse();
	}	
	
	public static void parse() throws Exception {
		lookahead = lexicalAnalizer();
		
		while (lookahead != DONE) {
			expression();
			match(';');
		}
	}

	public static int lexicalAnalizer() throws Exception {
		int ch;
		
		while (true) {
			ch = getchar();
			
			if (ch == ' ' || ch == '\t') {
				// Space and tabs deletion.
			} else if (ch == '\n') {
				lineNumber++; // Incrise line number.
			} else if (Character.isDigit(ch)) {
				tokenValue = ch - '0';
				ch = getchar();
				
				while (Character.isDigit(ch)) {
					tokenValue = tokenValue * 10 + ch - '0';
					ch = getchar();
				}
				
				ungetc((char) ch);
				return NUM;
			} else if (isChar(ch)) {
				lexBuffer = ch + "";
				ch = getchar();
				
				while (isCharOrDigit(ch)) {
					lexBuffer += ch;
					ch = getchar();
				}
				
				ungetc((char) ch); // FIXME: if ch != EOF
				
				if (!symbolTable.containsKey(lexBuffer)) {
					symbolTable.put(lexBuffer, ID);
				}
				
//				tokenValue = lexBuffer; FIXME
				tokenValue = symbolTable.get(lexBuffer);
				return tokenValue;
			} else if (ch == EOF) {
				lookahead = DONE;
				return DONE;
			} else {
				tokenValue = NONE;
				return ch;
			}
		}
	}

	public static void rest() throws Exception {
		if (lookahead == '+') {
			match('+');
			term();
			putchar('+');
			rest();
		} else if (lookahead == '-') {
			match('-');
			term();
			putchar('-');
			rest();
		}
				
	}
	
	public static void expression() throws Exception {
		int ch;
		term();
//		rest();
		while (true) {
			switch (lookahead) {
				case '+':
				case '-':
					ch = lookahead;
					match(lookahead);
					term();
					emit(ch, NONE);
					break;
				default:
					return;
			}
			/*if (lookahead == '+') {
				match('+');
				term();
				putchar('+');
			} else if (lookahead == '-') {
				match('-');
				term();
				putchar('-');
			} else {
				break;
			}*/
		}
	}
	
	public static void term() throws Exception {
		int ch;
		factor();
		
		while (true) {
			switch (lookahead) {
				case '*':
				case '/':
				case DIV:
				case MOD:
					ch = lookahead;
					match(lookahead);
					factor();
					emit(ch, NONE);
					break;
				default:
					return;
			}
		}
		
		/*if (Character.isDigit(lookahead)) {
			putchar(lookahead);
			match(lookahead);
		} else {
			throwException();
		}*/
	}	
	
	public static void match(int c) throws Exception {
		if (lookahead == c) {
			lookahead = lexicalAnalizer();
		} else {
			throwException();
		}
		/*if (lookahead == c) {
			lookahead = getchar();
		} else {
			throwException();
		}*/
	}
	
	public static void factor() throws Exception {
		
		switch (lookahead) {
		case '(':
			match('(');expression();match(')');
			break;
		case NUM:
			emit(NUM, tokenValue);
			match(NUM);
			break;
		case ID:
			emit(ID, tokenValue);
			match(ID);
			break;
		case -1:
			System.out.println("End");
			break;
		default:
			throwException();
		}
			
		
		/*if (lookahead == '(') {
			match('(');
			expression();
			match(')');
		} else if (lookahead == NUM) {
			System.out.println(tokenValue);
			match((char) tokenValue);
		} else {
			throwException();
		}*/
	}
	
	public static void emit(int ch, int tokenVal) {
		switch (ch) {
		case '+':
		case '-':
		case '*':
		case '/':
			System.out.print((char) ch);
			break;
		case DIV:
			System.out.print("div");
			break;
		case MOD:
			System.out.print((char) ch);
			break;
		case NUM:
			System.out.print(tokenVal);
			break;
		case ID:
			System.out.print((char) ch);
			break;
		default:
			System.out.print("token " + (char) ch + ", tokenval " + tokenVal);
			break;
			
				
		}
	}
	
	private static void throwException() throws LexicalException {
		throw new LexicalException("Character don't correspond. Lookahead is " 
				+ (char) lookahead);
	}
	
	private static int getchar() throws Exception {
		return StreamUtility.getInstance().readNextChar();
	}
	
	private static void putchar(int c) {
		System.out.println(c);
	}
	
	private static void ungetc(char ch) throws IOException {
		StreamUtility.getInstance().ungetc(ch);
	}
	
	private static boolean isChar(int ch) {
		return "asdfghjklmnbvcxzqwertyuiopASDFGHJKLMNBVCXZQWERTYUIOP".indexOf((char) ch) != -1;
	}
	
	private static boolean isDigit(int ch) {
		return "0123456789".indexOf((char) ch) != -1;
	}
	
	private static boolean isCharOrDigit(int ch) {
		return isChar(ch) || isDigit(ch);
	}
}
