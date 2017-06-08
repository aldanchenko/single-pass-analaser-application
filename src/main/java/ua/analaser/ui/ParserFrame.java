package ua.analaser.ui;

import ua.analaser.DocumentHelper;
import ua.analaser.StreamUtility;
import ua.analaser.SimplePascalParser;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserFrame {

    public static final String fileName = "test.txt";
    
	static JPanel language = null;
	static JPanel methods = null;
	static JPanel separators = null;
	static JPanel types = null;
	static JPanel variables = null;
	static JPanel math = null;

    static JList errorsList = null;

    public static JPanel getLanguagePanel() {
		return language;
	}
	
	public static JPanel getMethodPanel() {
		return methods;
	}
	
	public static JPanel getSeparatorPanel() {
		return separators;
	}
	
	public static JPanel getTypePanel() {
		return types;
	}
	
	public static JPanel getVariablePanel() {
		return variables;
	}
	
	public static JPanel getMathPanel() {
		return math;
	}

    public static JList getErrorsList() {
        return errorsList;
    }
	
	public ParserFrame() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception evt) {
            // do nothing.
		}

		JFrame f = new JFrame("Simple Pascal Parser.");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the StyleContext, the document and the editTextPane
		//StyleContext sc = new StyleContext();		
		final DefaultStyledDocument doc = 
					new DefaultStyledDocument(DocumentHelper.getStyleContext());

		final JTextPane editTextPane = new JTextPane(doc);

        CustomPanel customPanel = new CustomPanel(editTextPane);

		final List<JPanel> tabs = new ArrayList<JPanel>();
		final List<String> tabTitles = new ArrayList<String>();
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						
						StreamUtility streamUtility = StreamUtility.getInstance();
						final String content = streamUtility.readFile(fileName);
						
						streamUtility.setAsStream(content);												
						
						SimplePascalParser pascalParser =
										new SimplePascalParser(streamUtility);
						pascalParser.parse();
																														
						// Set the logical style
						doc.setLogicalStyle(0, DocumentHelper.getMainStyle());

						// Add the text to the document
						doc.insertString(0, content, null);

						// Apply the character attributes
						
						DocumentHelper.highliteDocumentLexemes(doc, pascalParser);
						
						language = createSymbolTableFrame("Language", pascalParser.getLanguageStatementLexemes());
						methods = createSymbolTableFrame("Methods", pascalParser.getMethodsLexemes());
						separators = createSymbolTableFrame("Separators", pascalParser.getSeparatorsLexemes());
						types = createSymbolTableFrame("Types", pascalParser.getTypesLexemes());
						variables = createSymbolTableFrame("Variables", pascalParser.getVariablesLexemes());
						math = createSymbolTableFrame("Math", pascalParser.getMathStatementLexemes());

                        errorsList = new JList(pascalParser.getErrorsList().toArray());

						tabs.add(language);
						tabs.add(methods);
						tabs.add(separators);
						tabs.add(types);
						tabs.add(variables);
						tabs.add(math);
						// Finally, apply the style to the heading
						// doc.setParagraphAttributes(0, 1, heading2Style, false);

						// Set the foreground color and change the font
						editTextPane.setForeground(Color.pink);
						editTextPane.setFont(new Font("Monospaced", Font.ITALIC, 24));
					} catch (BadLocationException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Exception when constructing document: " + e);
			System.exit(1);
		}
		
		doc.addDocumentListener(new ParserDocumentListener());
		tabTitles.add("Language");
		tabTitles.add("Methods");
		tabTitles.add("Separators");
		tabTitles.add("Types");
		tabTitles.add("Variables");
		tabTitles.add("Math");

        JTabbedPane tabbedPane = createTabbedPane(tabs, tabTitles);

        JSplitPane splitPane = createSplitPane(customPanel, errorsList, JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400);

        JSplitPane horizontalSplit = createSplitPane(splitPane, tabbedPane, JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplit.setDividerLocation(600);

        f.getContentPane().add(horizontalSplit);        
		f.setSize(900, 500);
		f.setVisible(true);
	}
	
	public JSplitPane createSplitPane(JComponent leftContent,
                                     JComponent rightContent, int orientation) {
        
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(orientation);

		splitPane.setLeftComponent(new JScrollPane(leftContent));
		splitPane.setRightComponent(new JScrollPane(rightContent));
		
		splitPane.setDividerSize(7);
		splitPane.setDividerLocation(300);
		
		return splitPane;
	}

    private JTabbedPane createTabbedPane(List<JPanel> tabs, List<String> tabTitles) {
        JTabbedPane tabbedPane = new JTabbedPane();

        for (int i = 0; i < tabs.size(); i++) {
            tabbedPane.addTab(tabTitles.get(i), tabs.get(i));
        }
        
        return tabbedPane;
    }

    public JPanel createSymbolTableFrame(String title, java.util.List list) {
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));
		
		/*Object[] data = new Object[list.size()];
		
		for (int i = 0; i < data.length; i++) {
			data[i] = list.get(i).toString();
		}*/
		
		JList jList = new JList(list.toArray());

		panel.add(new JScrollPane(jList), BorderLayout.CENTER);
		
		return panel;
	}
	
	public static void main(String[] args) {
		new ParserFrame();
	}
}