package ua.analaser.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: winter
 * Date: Mar 1, 2009
 * Time: 10:15:50 AM
 */
public class CustomPanel extends JPanel {

    private JTextPane textPane;
    private LineNumberPanel lineNumberPanel;

    public CustomPanel(JTextPane textPane) {
        this.setLayout(new BorderLayout(5, 5));

        this.textPane = textPane;
        this.lineNumberPanel = new LineNumberPanel(this);

        this.add(lineNumberPanel, BorderLayout.WEST);
        this.add(textPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(100, 200);

        CustomPanel panel = new CustomPanel(new JTextPane());
        frame.getContentPane().add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
