package main.chart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test title");

        PaintPanel paintpanel = new PaintPanel();
        paintpanel.setBackground(Color.GREEN);
        paintpanel.setPreferredSize(new Dimension(500, 500));
        JScrollPane scrollPane = new JScrollPane(paintpanel);

        scrollPane.setBackground(Color.BLACK);

        add(scrollPane,BorderLayout.CENTER);
        setSize(new Dimension(300, 300));
        //pack();
        setBackground(Color.YELLOW);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }



    public static void main(String[] args) {
        new MainFrame();
    }

    }
