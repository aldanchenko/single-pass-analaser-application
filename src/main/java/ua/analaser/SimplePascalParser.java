package ua.analaser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class SimplePascalParser {

	public static final int BUFFER_SIZE = 128;
	public static final int EOS = '\0';
	public static final int EOF = -1;

	public static final int ID = 261;
	public static final int STATEMENT = 258;
	public static final int EXPRESSION = 259;
	
	public static final int IF = 256;
	public static final int THEN = 257;
	public static final int ELSE = 268;
		
	public static final int DUMMY_STATEMENT = 260;
		
	public static final int DONE = 262;
	public static final int TRUE = 263;
	public static final int SPACE = 264;
	public static final int OR = 265;
	public static final int AND = 266;
	public static final int NOT = 267;
	
	
	public static final int NUMBER = 269;
	public static final int INTEGER = 270;
	public static final int DOUBLE = 271;
	public static final int CHAR = 272;
	public static final int ARRAY = 273;
	public static final int STRING = 274;
	public static final int TYPE = 275;
	public static final int STRING_CONSTANT = 295;
	public static final int VAR_DEFINITIOIN = 276;
	
	public static final int WHILE_CICLE = 277;
	public static final int DO_CICLE = 278;
	
	public static final int BEGIN = 279;
	public static final int END = 280;
		
	//(sqrt, cos, sin, div, ceil, flour, pow), строковые (substr, pos, length)

	/*public static final int FUNCTION_CONSOLE_READ = 281;
	public static final int FUNCTION_CONSOLE_WRITE = 282;
	
	public static final int FUNCTION_MATH_SQRT = 283;
	public static final int FUNCTION_MATH_COS = 284;
	public static final int FUNCTION_MATH_SIN = 285;
	public static final int FUNCTION_MATH_DIV = 286;
	public static final int FUNCTION_MATH_CEIL = 287;
	public static final int FUNCTION_MATH_FLOUR = 288;
	public static final int FUNCTION_MATH_POW = 289;
	
	public static final int FUNCTION_STRING_SUBSTR = 290;
	public static final int FUNCTION_STRING_POS = 291;
	public static final int FUNCTION_STRING_LENGTH = 292;*/
	
	public static final int FUNCTION = 293;
	public static final int METHOD = 294;
	
	public static final int NONE = -1;

	private int lookahead;

	private int lineNumber = 1;

	private String tokenValue;

	private StringBuffer lexBuffer = null;
	
	private StreamUtility streamUtility = null;

	private Map<String, Integer> symbolTable = null;
	
	private List<Lexeme> separatorsLexemes = null;
	private List<Lexeme> variablesLexemes = null;
	private List<Lexeme> typesLexemes = null;
	private List<Lexeme> methodsLexemes = null;
	private List<Lexeme> statemenLexemes = null;
	private List<Lexeme> mathLexemes = null;

	private List<LexicalException> errorsList = null;

	public static void main(String[] args) throws Exception {
		StreamUtility utility = StreamUtility.getInstance();
		String content = utility.readFile("test.txt");
		utility.setAsStream(content);
		
		SimplePascalParser pascalParser = new SimplePascalParser(utility);
		pascalParser.parse();
	}

    public SimplePascalParser(StreamUtility utility) {
		
		this.streamUtility = utility;
		
		this.lexBuffer = new StringBuffer();
		
		this.symbolTable = new HashMap<String, Integer>();
		
		this.separatorsLexemes = new ArrayList<Lexeme>();;
		this.variablesLexemes = new ArrayList<Lexeme>();;
		this.typesLexemes = new ArrayList<Lexeme>();;
		this.methodsLexemes = new ArrayList<Lexeme>();;
		this.statemenLexemes = new ArrayList<Lexeme>();
		this.mathLexemes = new ArrayList<Lexeme>();
		this.errorsList = new ArrayList<LexicalException>();

		initialize();
	}

	public void initialize() {
		// init

		symbolTable.put("If", IF);
		symbolTable.put("then", THEN);
		symbolTable.put("else", ELSE);
		symbolTable.put("true", TRUE);
		symbolTable.put("or", OR);
		symbolTable.put("and", AND);
//		symbolTable.put("dummy", DUMMY_STATEMENT);
		symbolTable.put("Var", VAR_DEFINITIOIN);
		symbolTable.put("Integer", INTEGER);
		symbolTable.put("Double", DOUBLE);
		symbolTable.put("String", STRING);
		symbolTable.put("Char", CHAR);
		symbolTable.put("While", WHILE_CICLE);
		symbolTable.put("do", DO_CICLE);
		symbolTable.put("Begin", BEGIN);
		symbolTable.put("End", END);
		
		symbolTable.put("Read", METHOD);
		symbolTable.put("Write", METHOD);
		
		symbolTable.put("Sqrt", FUNCTION);		
		symbolTable.put("Cos", FUNCTION);
		symbolTable.put("Sin", FUNCTION);
		symbolTable.put("Div", FUNCTION);
		symbolTable.put("Ceil", FUNCTION);
		symbolTable.put("Flour", FUNCTION);
		symbolTable.put("Pow", FUNCTION);
		
		symbolTable.put("Substr", FUNCTION);
		symbolTable.put("Pos", FUNCTION);
		symbolTable.put("Length", FUNCTION);
		
		symbolTable.put("0", 0);
	}
	
	public void parse() throws Exception {
        try {
            lookahead = lexicalAnalizer();

            match(BEGIN);
            emit(BEGIN, NONE);
        } catch (LexicalException ex) {
		    StreamUtility utility = StreamUtility.getInstance();
            int startIndex = utility.getCount();
            int length = 0;



            while (lookahead != ';') {
                lookahead = utility.readNextChar();
                length++;
            }

            ex.setStartIndex(startIndex);
            ex.setLexemeLength(length);

            errorsList.add(ex);

            lookahead = lexicalAnalizer();
        }
		
		while (lookahead != END) {
            try {

                statement();

            } catch (LexicalException ex) {
                StreamUtility utility = StreamUtility.getInstance();
                int startIndex = utility.getCount();
                int length = 0;


                                
                while (lookahead != ';') {                    
                    lookahead = utility.readNextChar();
                    length++;
                }

                ex.setStartIndex(startIndex);
                ex.setLexemeLength(length);

                errorsList.add(ex);
                
                lookahead = lexicalAnalizer();
            }
//			match(';');
		}

		emit(END, NONE);
//        System.out.println("Errors");
        for (LexicalException ex : errorsList) {
            System.out.println(ex);
        }
	}

    public String getLanguageLexemeByLookAhead(int lookahead) {
        if (symbolTable.containsValue(lookahead)) {
            Iterator<String> iterator = symbolTable.keySet().iterator();

            for ( ;iterator.hasNext(); ) {
                String key = iterator.next();
                int value = symbolTable.get(key);

                if (value == lookahead) {
                    return key;
                }
            }
        }

        return "Dont known";
    }

	public int lexicalAnalizer() throws Exception {
		int ch;	
		
		while (true) {
			ch = getchar();
	
			if (ch == ' ' || ch == '\t') {
				// Space and tabs deletion.
			} else if (ch == '\n') {
				lineNumber++; // Increase line number.
			// Number lexeme expression.
			} else if (Character.isDigit((char) ch)) {
				int startIndex = streamUtility.getCount();
				
				boolean isDouble = false;
				
				lexBuffer = new StringBuffer();
				lexBuffer.append((char) ch);
				ch = getchar();
				
				while (Character.isDigit((char) ch)) {
					lexBuffer.append((char) ch);
					ch = getchar();
				}
				
				if (ch == '.') {
					lexBuffer.append('.');
					ch = getchar();
					isDouble = true;
				}
				
				while (Character.isDigit((char) ch)) {
					lexBuffer.append((char) ch);
					ch = getchar();
				}
				
				if (ch == 'E') {
					lexBuffer.append('E');
					ch = getchar();

					if (!Character.isDigit((char) ch)) {
						throw new LexicalException(tokenValue,
                                "E", lookahead, lineNumber);
					}

					while (Character.isDigit((char) ch)) {
						lexBuffer.append((char) ch);
						ch = getchar();
					}
				}
				
				ungetc((char) ch);
				
				if (!symbolTable.containsKey(lexBuffer.toString())) {
					int tokenType = -1;

                    if (isDouble) {
						tokenType = DOUBLE;
					} else {
						tokenType = INTEGER;
					}
                    
					symbolTable.put(lexBuffer.toString(), tokenType);
				}

				tokenValue = lexBuffer.toString();
				int lexeme = symbolTable.get(lexBuffer.toString());
				lexemeCategorization(lexeme, startIndex);
				return lexeme;
			// ID lexeme expression.
			}  else if (isChar(ch)) {
				int startIndex = streamUtility.getCount();

				lexBuffer = new StringBuffer(); // Clear buffer;
				lexBuffer.append((char) ch);
				
				ch = getchar();

				while (isCharOrDigit(ch)) {
					lexBuffer.append((char) ch);
					ch = getchar();
				}

				ungetc((char) ch); // FIXME: if ch != EOF

				if (!symbolTable.containsKey(lexBuffer.toString())) {
					symbolTable.put(lexBuffer.toString(), ID);
				}

				tokenValue = lexBuffer.toString();
				int lexeme = symbolTable.get(lexBuffer.toString());
				lexemeCategorization(lexeme, startIndex);
				return lexeme;
			// String constant lexeme expression.
			} else if (ch == '\'') {
				int startIndex = streamUtility.getCount();

				lexBuffer = new StringBuffer(); // Clear buffer;
				
				do {
					lexBuffer.append((char) ch);
					ch = getchar();
					
					if (ch == EOF) {
						throw new LexicalException(tokenValue,
                                "String char", lookahead, lineNumber);
					}
					
				} while (ch != '\'');

				lexBuffer.append((char) ch);
				
//				ungetc((char) ch); // FIXME: if ch != EOF

				if (!symbolTable.containsKey(lexBuffer.toString())) {
					symbolTable.put(lexBuffer.toString(), STRING_CONSTANT);
				}

				tokenValue = lexBuffer.toString();
				int lexeme = symbolTable.get(lexBuffer.toString());
				lexemeCategorization(lexeme, startIndex);
				return lexeme;	
			} else if (ch == EOF) {
				int startIndex = streamUtility.getCount();
				lookahead = DONE;				
				return DONE;
			} else {
				int startIndex = streamUtility.getCount();
				
				lexemeCategorization(ch, startIndex);
				
				return ch;
			}
		}
	}
	
	public void ifStatement() throws Exception {
		int ch;
		
		while (true) {
			switch (lookahead) {
			/*case DUMMY_STATEMENT:
				match(DUMMY_STATEMENT);
				emit(DUMMY_STATEMENT, NONE);
//				lookahead = DONE;
				return;*/
			case IF:
				ch = lookahead;
				match(IF);
				emit(IF, NONE);
				booleanExpression(); 				
				match(THEN);
				emit(THEN, NONE);
				statement();	
				
				if (lookahead == ELSE) {
					match(ELSE);
					emit(ELSE, NONE); 				
					statement();
				}
				
				break;
			default:
				return;
			}
		}
	}
	
	public void mathExpression() throws Exception {
		int ch = lookahead;

//		matchIsNumberOrVariable(ch);
		
		while (true) {
			switch (lookahead) {
			case '-':
			case '+':
			case '/':
			case '*':
				ch = lookahead;
				match(ch);
				emit(ch, NONE);
				ch = lookahead;
				matchIsNumberOrVariable(ch);
				break;
			default:

				match(';');
				emit(';', NONE);
				
				return;
			}
		}
	}

	private boolean matchIsNumberOrVariable(int ch) throws Exception,
			LexicalException {
		if (ch == ID || ch == INTEGER || ch == DOUBLE) {		
			emit(ID, NONE);
			match(ch);
			return true;
		} else {
//			errorHandling();
//            throw new LexicalException(tokenValue, lookahead, lineNumber);
		}
		
		return false;
	}
	
	private boolean isFunction(int ch) {
		return ch == FUNCTION;
	}	
	
	public void statement() throws Exception {
		while (true) {
			switch (lookahead) {
			case ID:
				match(ID);
				emit(ID, NONE);
				
				if (lookahead == '=') {
					match('=');
					emit('=', NONE);
					
					if (isFunction(lookahead)) {
						functionExpression();
					} else if (matchIsNumberOrVariable(lookahead)) {
						mathExpression();
					}
					
				}
				
				return;
			case VAR_DEFINITIOIN:
				match(VAR_DEFINITIOIN);
				emit(VAR_DEFINITIOIN, NONE);
				match(ID);
				emit(ID, NONE);
				match(':');
				emit(':', NONE);
				switch (lookahead) {
					case INTEGER:
					case DOUBLE:
					case CHAR:
					case STRING:
						emit(lookahead, NONE);
						match(lookahead);
						match(';');
						emit(';', NONE);
						break;
					default:
                        throw new LexicalException(tokenValue,
                                "Integer, Double, Char or String", lookahead, lineNumber);
//							errorHandling();

				}

				return;
			case IF:
				ifStatement();
				return;
			case WHILE_CICLE:
				match(WHILE_CICLE);
				emit(WHILE_CICLE, NONE);
				booleanExpression();
				match(DO_CICLE);
				emit(DO_CICLE, NONE);
				statement();
				return;
			case METHOD:
				methodExpression();
				return;
			case FUNCTION:
				functionExpression();
				return;
			default:				
				return;
			}
		}
	}

	private void functionExpression() throws Exception {

		String functionName = tokenValue;
		
		match(FUNCTION);
		emit(FUNCTION, NONE);
		
		if ("Sin".equals(functionName) || "Cos".equals(functionName) 
				|| "Sqrt".equals(functionName) || "Ceil".equals(functionName)) {
		
			match('(');
			emit('(', NONE);
			
			matchIsNumberOrVariable(lookahead);
			
			match(')');
			emit(')', NONE);
		} else if ("Pow".equals(functionName)) {
			match('(');
			emit('(', NONE);
			
			matchIsNumberOrVariable(lookahead);
			
			match(',');
			emit(',', NONE);
			
			matchIsNumberOrVariable(lookahead);
			
			match(')');
			emit(')', NONE);
		} else if ("Substr".equals(functionName)) {
			match('(');
			emit('(', NONE);
			
			matchStringConstantOrVariable();
			
			match(',');
			emit(',', NONE);
			
			match(INTEGER);
			emit(NUMBER, NONE);
			
			match(')');
			emit(')', NONE);
		} else if ("Pos".equals(functionName)) {
			match('(');
			emit('(', NONE);
			
			matchStringConstantOrVariable();
			
			match(',');
			emit(',', NONE);
			
			matchStringConstantOrVariable();
			
			match(')');
			emit(')', NONE);
		} else if ("Length".equals(functionName)) {
			match('(');
			emit('(', NONE);
			
			match(ID);
			emit(ID, NONE);
			
			match(')');
			emit(')', NONE);	
		}
		

		match(';');
		emit(';', NONE);
	}

	private void methodExpression() throws Exception {

		match(METHOD);
		emit(METHOD, NONE);
		
		match('(');
		emit('(', NONE);
		
		match(ID);
		emit(ID, NONE);
		
		match(')');
		emit(')', NONE);
		

		match(';');
		emit(';', NONE);
	}
	
	private void matchStringConstantOrVariable() throws Exception {
		if (lookahead == STRING_CONSTANT) {
			match(STRING_CONSTANT);
			emit(STRING_CONSTANT, NONE);	
		} else if (lookahead == ID) {
			match(ID);
			emit(ID, NONE);
		} else {
//			errorHandling();
            throw new LexicalException(tokenValue,
                    "String constant or variable", lookahead, lineNumber);
		}		
	}

	public void booleanExpression() throws Exception {
		int ch = lookahead;
		
		match(ID);
		emit(ch, NONE);		
		
		while (true) {
			switch (lookahead) {
				case '=':
				case '<':
				case '>':
				case  OR:
				case AND:	
					ch = lookahead;
					match(ch);
					emit(ch, NONE);
					ch = lookahead;
					matchIsNumberOrVariable(ch);
					break;
				default:
					return;
			}
		}
	}

	public void match(int ch) throws Exception {
		if (lookahead == ch) {
			lookahead = lexicalAnalizer();
		} else {
//			errorHandling();
            throw new LexicalException(tokenValue,
                    ch, lookahead, lineNumber);
		}
		/*
		 * if (lookahead == c) { lookahead = getchar(); } else {
		 * errorHandling(); }
		 */
	}

	public void emit(int ch, int tokenVal) {		
		// FIXME:
		/*int startIndex = 0;
				
		Lexeme lexeme = null; //new Lexeme("", startIndex, length);
		//lexeme.setValue(tokenValue);

		char s;
		
		switch (ch) {
			case '+':
			case '-':
			case '*':
			case '/':			
			case '=':
			case '>':
			case '<':
				s = (char) ch;
				System.out.print(s);				
				break;
			case ':':
			case ',':
			case '(':
			case ')':
			case ';':
				s = (char) ch;
				System.out.print(s);
				break;
			case BEGIN:
				System.out.print(" Begin ");
				break;
			case END:
				System.out.print(" End ");
				break;
			case IF:
				System.out.print(" If ");
				break;
			case THEN:
				System.out.print(" then ");
				break;
			case WHILE_CICLE:
				System.out.print("while ");
				break;
			case DO_CICLE:
				System.out.print(" do ");
				break;
			case ELSE:
				System.out.print(" else ");
				break;
			case TRUE:
				System.out.print(" true ");
				break;
			case OR:
				System.out.print(" or ");
				break;
			case AND:
				System.out.print(" and ");
				break;
			case VAR_DEFINITIOIN:
				System.out.print(" var ");
				break;
			case STRING:
				System.out.print(" string");
				break;
			case CHAR:
				System.out.print(" char");
				break;
			case INTEGER:
				System.out.print(" Integer");
				break;			
			case DOUBLE:
				System.out.print(" double");
				break;
			case DUMMY_STATEMENT:
				System.out.print(" dummy ");
				break;
			case ID:			
				System.out.print(tokenValue);
				break;
			case STRING_CONSTANT:			
				System.out.print(tokenValue);
				break;
			case NUMBER:
				System.out.print(tokenValue);
				break;
			case METHOD:
			case FUNCTION:
				System.out.print(tokenValue);
				break;
			default:
				System.out.print("token " + (char) ch + ", tokenval " + tokenVal);
				break;

		}*/
	}

	public void lexemeCategorization(int ch, int lexemeStartIndex) {		
		// FIXME:
		int startIndex = lexemeStartIndex; 
				
		Lexeme lexeme = null; //new Lexeme("", startIndex, length);
		//lexeme.setValue(tokenValue);

		char s;
		
		switch (ch) {
			case '+':
			case '-':
			case '*':
			case '/':			
			case '=':
			case '>':
			case '<':
				s = (char) ch;
				
				lexeme = new Lexeme(s + "", startIndex, 1);
				getMathStatementLexemes().add(lexeme);
				break;
			case ':':
			case ',':
			case '(':
			case ')':
			case ';':
				s = (char) ch;
				lexeme = new Lexeme(s + "", startIndex, 1);
				getSeparatorsLexemes().add(lexeme);
				break;
			case BEGIN:
				addToLanguageLexems("Begin", startIndex);
				break;
			case END:
				addToLanguageLexems("End", startIndex);
				break;
			case IF:
				addToLanguageLexems("If", startIndex);
				break;
			case THEN:
				addToLanguageLexems("Then", startIndex);
				break;
			case WHILE_CICLE:
				addToLanguageLexems("While", startIndex);
				break;
			case DO_CICLE:
				addToLanguageLexems("Do", startIndex);
				break;
			case ELSE:
				addToLanguageLexems("Else", startIndex);
				break;
			case TRUE:
				addToLanguageLexems("True", startIndex);
				break;
			case OR:
				addToLanguageLexems("Or", startIndex);
				break;
			case AND:
				addToLanguageLexems("And", startIndex);
				break;
			case VAR_DEFINITIOIN:
				addToLanguageLexems("Var", startIndex);
				break;
			case STRING:
				addToTypeLexems("String", startIndex);
				break;
			case CHAR:
				addToTypeLexems("Char", startIndex);
				break;
			case INTEGER:
				addToTypeLexems("Integer", startIndex);
				break;			
			case DOUBLE:
				addToTypeLexems("Double", startIndex);
				break;
			case DUMMY_STATEMENT:
				break;
			case ID:
				addToVariableLexems(tokenValue, startIndex);
				break;
			case STRING_CONSTANT:
				addToVariableLexems(tokenValue, startIndex);
				break;
			case NUMBER:
				addToVariableLexems(tokenValue, startIndex);
				break;
			case METHOD:
			case FUNCTION:
				addToMethodleLexems(tokenValue, startIndex);
				break;
			default:
				break;

		}
	}

	private void addToLanguageLexems(String value, int startIndex) {
		Lexeme lexeme = new Lexeme("", startIndex, value.length());
		lexeme.setValue(value);
		getLanguageStatementLexemes().add(lexeme);
	}
	
	private void addToMathLexems(String value, int startIndex) {
		Lexeme lexeme = new Lexeme("", startIndex, value.length());
		lexeme.setValue(value);
		getMathStatementLexemes().add(lexeme);
	}
	
	private void addToTypeLexems(String value, int startIndex) {
		Lexeme lexeme = new Lexeme("", startIndex, value.length());
		lexeme.setValue(value);
		getTypesLexemes().add(lexeme);
	}
	
	private void addToMethodleLexems(String value, int startIndex) {
		Lexeme lexeme = new Lexeme("", startIndex, value.length());
		lexeme.setValue(value);
		getMethodsLexemes().add(lexeme);
	}
	
	private void addToVariableLexems(String value, int startIndex) {
		Lexeme lexeme = new Lexeme("", startIndex, value.length());
		lexeme.setValue(value);
        if (!getVariablesLexemes().contains(lexeme)) {
		    getVariablesLexemes().add(lexeme);
        }
	}

	private void errorHandling(LexicalException ex) /*throws LexicalException*/ {
        errorsList.add(ex);
		/*throw new LexicalException("Character don't correspond. Lookahead is "
				+ (char) lookahead);*/
	}

	private int getchar() throws Exception {
		return streamUtility.readNextChar();
	}	

	private void ungetc(char ch) throws IOException {
		streamUtility.ungetc(ch);
	}

	private boolean isChar(int ch) {
		return "asdfghjklmnbvcxzqwertyuiopASDFGHJKLMNBVCXZQWERTYUIOP"
				.indexOf((char) ch) != -1;
	}

	private boolean isDigit(int ch) {
		return "0123456789".indexOf((char) ch) != -1;
	}

	private boolean isCharOrDigit(int ch) {
		return isChar(ch) || isDigit(ch);
	}
	
	private int getCurrentStreamIndex() {
		return streamUtility.getCount();
	}
	
	public List<Lexeme> getLanguageStatementLexemes() {
		return statemenLexemes;
	}
	
	public List<Lexeme> getMathStatementLexemes() {
		return mathLexemes;
	}

	/**
	 * @param separatorsLexemes the separatorsLexemes to set
	 */
	public void setSeparatorsLexemes(List<Lexeme> separatorsLexemes) {
		this.separatorsLexemes = separatorsLexemes;
	}

	/**
	 * @return the separatorsLexemes
	 */
	public List<Lexeme> getSeparatorsLexemes() {
		return separatorsLexemes;
	}

	/**
	 * @param variablesLexemes the variablesLexemes to set
	 */
	public void setVariablesLexemes(List<Lexeme> variablesLexemes) {
		this.variablesLexemes = variablesLexemes;
	}

	/**
	 * @return the variablesLexemes
	 */
	public List<Lexeme> getVariablesLexemes() {
		return variablesLexemes;
	}

	/**
	 * @param typesLexemes the typesLexemes to set
	 */
	public void setTypesLexemes(List<Lexeme> typesLexemes) {
		this.typesLexemes = typesLexemes;
	}

	/**
	 * @return the typesLexemes
	 */
	public List<Lexeme> getTypesLexemes() {
		return typesLexemes;
	}

	/**
	 * @param methodsLexemes the methodsLexemes to set
	 */
	public void setMethodsLexemes(List<Lexeme> methodsLexemes) {
		this.methodsLexemes = methodsLexemes;
	}

	/**
	 * @return the methodsLexemes
	 */
	public List<Lexeme> getMethodsLexemes() {
		return methodsLexemes;
	}

    public List<LexicalException> getErrorsList() {
        return errorsList;
    }

    public void setErrorsList(List<LexicalException> errorsList) {
        this.errorsList = errorsList;
    }
}

