package ua.analaser.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: winter
 * Date: Mar 1, 2009
 * Time: 10:22:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class LineNumberPanel extends JPanel {

    public LineNumberPanel(Component parent) {
        super();

//        this.setSize(15, parent.getHeight());
        this.setPreferredSize(new Dimension(30, parent.getHeight()));
//        this.setMinimumSize(new Dimension(50, parent.getHeight()));
//        this.setBounds(0, 0, 50, parent.getHeight());
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        for (int i = 1; i < 1000; i++) {
            graphics.drawString("" + i, 1, i * 15);
        }
    }
}
