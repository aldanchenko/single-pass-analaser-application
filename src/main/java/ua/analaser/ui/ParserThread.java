package ua.analaser.ui;

import ua.analaser.SimplePascalParser;
import ua.analaser.StreamUtility;
import ua.analaser.DocumentHelper;
import ua.analaser.Lexeme;
import ua.analaser.LexicalException;

import javax.swing.text.Document;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: winter
 * Date: Mar 7, 2009
 * Time: 11:23:59 PM
 */
public class ParserThread extends Thread {

    /*private String documentText = null;*/

    private Document document = null;

    /*private SimplePascalParser simpleParser = null;*/

    public ParserThread(Document document) {
        this.document = document;
    }

    @Override
    public void run() {
        try {
            StreamUtility streamUtility = StreamUtility.getInstance();

//            final SimplePascalParser simpleParser = null;

            try {
                streamUtility.setAsStream(document.getText(0, document.getLength()));

                final SimplePascalParser simpleParser = new SimplePascalParser(streamUtility);
                simpleParser.parse();

                refreshLexemeTable(ParserFrame.getLanguagePanel(), simpleParser.getLanguageStatementLexemes());
                refreshLexemeTable(ParserFrame.getMathPanel(), simpleParser.getMathStatementLexemes());
                refreshLexemeTable(ParserFrame.getMethodPanel(), simpleParser.getMethodsLexemes());
                refreshLexemeTable(ParserFrame.getSeparatorPanel(), simpleParser.getSeparatorsLexemes());
                refreshLexemeTable(ParserFrame.getTypePanel(), simpleParser.getTypesLexemes());
                refreshLexemeTable(ParserFrame.getVariablePanel(), simpleParser.getVariablesLexemes());

//                refreshLexemeTable(ParserFrame.getErrorsList(), simpleParser.getErrorsList());

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        DocumentHelper.highliteDocumentLexemes((DefaultStyledDocument) document, simpleParser);
                        refreshLexemeTable(ParserFrame.getErrorsList(), simpleParser.getErrorsList());
                    }
                });

            } catch (Exception ex) {
                 //JOptionPane.showMessageDialog(null, "Exception message :" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("ex " + ex.getMessage());
        }
    }

    private void refreshLexemeTable(JPanel tablePanel, List<Lexeme> lexemes) {
		JList list = (JList) ((JScrollPane) tablePanel.getComponent(0)).getViewport().getComponent(0);
		DefaultListModel model = new DefaultListModel();

		for (Lexeme lexeme : lexemes) {
			model.addElement(lexeme);
		}

		list.setModel(model);
	}

    private void refreshLexemeTable(JList list, List<LexicalException> errorsList) {
		DefaultListModel model = new DefaultListModel();

		for (LexicalException exception : errorsList) {
			model.addElement(exception);
		}

		list.setModel(model);
	}

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    /*public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }*/
}
