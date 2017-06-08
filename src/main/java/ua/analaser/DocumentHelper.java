package ua.analaser;

import java.awt.Color;
import java.util.List;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class DocumentHelper {
	
	private static StyleContext styleContext = new StyleContext();
	private static Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
	
	private static Style mainStyle;
	private static Style statementStyle;
	private static Style methodStyle;
	private static Style separatorStyle;
	private static Style mathStyle; 
	private static Style variableStyle;
	private static Style typeStyle;
	
	public static void highliteDocumentLexemes(DefaultStyledDocument document, 
											List<Lexeme> lexemes, Style style) {
		for (Lexeme lexeme : lexemes) {
			document.setCharacterAttributes(lexeme.getStartIndex(), 
					lexeme.getLength(), style, false);	
		}		
	}

    public static void highliteDocumentErrors(DefaultStyledDocument document,
											List<LexicalException> lexemes, Style style) {
		for (LexicalException exception : lexemes) {
			document.setCharacterAttributes(exception.getStartIndex(),
					exception.getLexemeLength(), style, false);
		}
	}
	
	public static void highliteDocumentLexemes(DefaultStyledDocument document, 
									SimplePascalParser parser) {
		
		highliteDocumentLexemes(document, parser.getLanguageStatementLexemes(), getStatementStyle());
		highliteDocumentLexemes(document, parser.getMathStatementLexemes(), getMathStyle());
		highliteDocumentLexemes(document, parser.getMethodsLexemes(), getMethodStyle());
		highliteDocumentLexemes(document, parser.getSeparatorsLexemes(), getSeparatorStyle());
		highliteDocumentLexemes(document, parser.getTypesLexemes(), getTypeStyle());
		highliteDocumentLexemes(document, parser.getVariablesLexemes(), getVariableStyle());

		highliteDocumentErrors(document, parser.getErrorsList(), getErrorStyle());
	}
	
	public static StyleContext getStyleContext() {
		return styleContext;
	}
	
	public static Style getMainStyle() {
		
		if (mainStyle == null) {
			mainStyle = styleContext.addStyle("MainStyle", defaultStyle);
			StyleConstants.setLeftIndent(mainStyle, 1);
			StyleConstants.setRightIndent(mainStyle, 1);
			StyleConstants.setFirstLineIndent(mainStyle, 1);
			StyleConstants.setFontFamily(mainStyle, "serif");
			StyleConstants.setFontSize(mainStyle, 12);
		}
		
		return mainStyle;
	}
	
	public static Style getStatementStyle() {
		if (statementStyle == null) {
			statementStyle = styleContext.addStyle("statementStyle", null);
			StyleConstants.setFontFamily(statementStyle, "monospaced");
			StyleConstants.setForeground(statementStyle, Color.black);
			StyleConstants.setBold(statementStyle, true);
		}
		
		return statementStyle;
	}

	public static Style getMethodStyle() {
		if (methodStyle == null) {
			methodStyle = styleContext.addStyle("methodStyle", null);
			StyleConstants.setFontFamily(methodStyle, "monospaced");
			StyleConstants.setForeground(methodStyle, Color.black);
		}
		
		return methodStyle;
	}
	
	public static Style getSeparatorStyle() {
		if (separatorStyle == null) {
			separatorStyle = styleContext.addStyle("separatorStyle", null);
			StyleConstants.setFontFamily(separatorStyle, "monospaced");
			StyleConstants.setForeground(separatorStyle, Color.DARK_GRAY);
		}
		
		return separatorStyle;
	}
	
	public static Style getTypeStyle() {
		if (typeStyle == null) {
			typeStyle = styleContext.addStyle("typeStyle", null);
			StyleConstants.setFontFamily(typeStyle, "monospaced");
			StyleConstants.setForeground(typeStyle, Color.gray);
		}
		
		return typeStyle;
	}
	
	public static Style getVariableStyle() {
		if (variableStyle == null) {
			variableStyle = styleContext.addStyle("variableStyle", null);
			StyleConstants.setFontFamily(variableStyle, "monospaced");
			StyleConstants.setForeground(variableStyle, Color.blue);
		}
		
		return variableStyle;
	}
	
	public static Style getMathStyle() {
		if (mathStyle == null) {
			mathStyle = styleContext.addStyle("variableStyle", null);
			StyleConstants.setFontFamily(mathStyle, "monospaced");
			StyleConstants.setForeground(mathStyle, Color.LIGHT_GRAY);
		}
		
		return mathStyle;
	}

    public static Style getErrorStyle() {
		if (mathStyle == null) {
			mathStyle = styleContext.addStyle("variableStyle", null);
			StyleConstants.setFontFamily(mathStyle, "monospaced");
			StyleConstants.setForeground(mathStyle, Color.RED);
		}

		return mathStyle;
	}
}
