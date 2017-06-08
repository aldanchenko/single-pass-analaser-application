package ua.analaser.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ParserDocumentListener implements DocumentListener {

    private ParserThread lastParserThread = null;

    public void changedUpdate(DocumentEvent e) {
        //handler(e);
	}

	public void insertUpdate(final DocumentEvent documentEvent) {
        handler(documentEvent);
	}

	private void handler(final DocumentEvent e) {

        /*if (this.lastParserThread != null) {
            this.lastParserThread.interrupt();
            this.lastParserThread = null;
        }*/

//        ParserThread parserThread = new ParserThread(e.getDocument()); //getParserThread(e);
//        this.lastParserThread = parserThread;

//        parserThread.start();
        getParserThread(e).start();
	}

	public void removeUpdate(DocumentEvent e) {
		handler(e);
	}

    public ParserThread getParserThread(DocumentEvent e) {
        if (lastParserThread == null) {
            this.lastParserThread = new ParserThread(e.getDocument());
        } else {
            lastParserThread.interrupt();
            lastParserThread = null;
            
            this.lastParserThread = new ParserThread(e.getDocument());
        }

        return lastParserThread;
    }
	
	/*private SimplePascalParser handleChanges(DocumentEvent event) throws Exception {
		StreamUtility streamUtility = StreamUtility.getInstance();
		
		Document document = event.getDocument();
		SimplePascalParser pascalParser = null;

		streamUtility.setAsStream(document.getText(0, document.getLength()));						
		
		pascalParser = new SimplePascalParser(streamUtility);
		pascalParser.parse();
		
		DocumentHelper.highliteDocumentLexemes((DefaultStyledDocument) document, pascalParser);
				
		return pascalParser;
	}

	private void refreshLexemeTable(JPanel tablePanel, List<Lexeme> lexemes) {
		JList list = (JList) ((JScrollPane) tablePanel.getComponent(0)).getViewport().getComponent(0);
		DefaultListModel model = new DefaultListModel();
		
		for (Lexeme lexeme : lexemes) {
			model.addElement(lexeme);
		}
		
		list.setModel(model);
	}*/
}
