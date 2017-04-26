package main.chart;

import main.chart.axis.Axis;
import main.chart.axis.LinearAxis;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test title");

        Point2d[] points =  {new Point2d(0.7, 0.3),new Point2d(0.8, 0.5),new Point2d(0.9,0.5), new Point2d(1,2.5)};

        Point2d[] points2 =  {new Point2d(0.0, 0.0),new Point2d(0.3, 0.5),new Point2d(0.7,0.5), new Point2d(0.9,2.5)};

        Point2d[] points3 =  {new Point2d(0.1, 0.3),new Point2d(0.3, 0.5)};


        Chart chart = new Chart();
        Axis xAxis = new LinearAxis(true);
        Axis yAxis = new LinearAxis(false);
        xAxis.setRange(0,3);
        yAxis.setRange(0,3);
        Graph lineGraph = new LineGraph(new ChartItems2DList(points));
        lineGraph.setxAxis(xAxis);
        lineGraph.setyAxis(yAxis);
        chart.addHorizontalAxis(xAxis);
        chart.addVerticalAxis(yAxis);
        chart.addGraph(lineGraph);

        Graph areaGraph = new AreaGraph(new ChartItems2DList(points2));
        areaGraph.setxAxis(yAxis);
        areaGraph.setyAxis(xAxis);
        chart.addGraph(areaGraph);


        chart.setPreferredSize(new Dimension(500, 500));
        chart.setBackground(Color.BLACK);
        chart.setPreferredSize(new Dimension(500, 500));
        add(chart,BorderLayout.CENTER);

      //  TestPane testPane = new TestPane();
      //  add(testPane,BorderLayout.NORTH);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);



    }


    public static void main(String[] args) {
        new MainFrame();
    }

    }
