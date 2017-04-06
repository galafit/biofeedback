package main.chart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test title");

        Point2d[] points =  {new Point2d(0.7, 0.3),new Point2d(0.7, 0.5)};

        PaintPanel paintpanel = new PaintPanel(points);
        paintpanel.setBackground(Color.BLACK);
        paintpanel.setPreferredSize(new Dimension(500, 500));
        add(paintpanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }



    public static void main(String[] args) {
        new MainFrame();
    }

    }
