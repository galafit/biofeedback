package main.chart;

import main.chart.axis.Axis;
import main.chart.axis.LinearAxis;
import main.chart.functions.Foo;
import main.chart.functions.Function2D;
import main.chart.functions.Sin;
import main.chart.functions.Tg;
import main.functions.Function;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test chart");


        Chart chart = new Chart();

        Axis yAxis2 = new LinearAxis();
        Axis xAxis2 = new LinearAxis();


        yAxis2.getGridSettings().setGridLineWidth(0);
        yAxis2.getGridSettings().setMinorGridLineWidth(0);
        yAxis2.setOpposite(true);
       // yAxis2.setVisible(false);

        xAxis2.getGridSettings().setGridLineWidth(0);
        xAxis2.getGridSettings().setMinorGridLineWidth(0);
        xAxis2.setOpposite(true);


        chart.addYAxis(yAxis2);
        chart.addXAxis(xAxis2);

        XYList xyList = new XYList();
        Random rand = new Random();
        // xyList.addItem(-2.5,-8);
        for (int i = 0; i <15 ; i++) {
            xyList.addItem(i,rand.nextInt(100)/100.0);
        }
       // xyList.addItem(15, -1);


        XYList xyList2 = new XYList();
        for (int i = 0; i <6 ; i++) {
            //xyList2.addItem(i,rand.nextInt(100));
            xyList2.addItem(4057.0789,i);
        }


        chart.addGraph(new LineGraph(),xyList);
        chart.addGraph(new LineGraph(), xyList2, 1,1);


        Function2D sin = new Sin();
        //chart.addGraph(new LineGraph(),sin);

        Function2D tg = new Tg();
        //chart.addGraph(new LineGraph(), tg);

        Function2D foo = new Foo();
        chart.addGraph(new LineGraph(), foo);

        Chart chart1 = new Chart();
        chart1.addGraph(new LineGraph(),xyList);


        Axis yAxis3 = new LinearAxis();
        yAxis3.getGridSettings().setGridLineWidth(0);
        yAxis3.getGridSettings().setMinorGridLineWidth(0);
        yAxis3.setOpposite(true);
        chart1.addYAxis(yAxis3);


        MultipaneChart multipaneChart = new MultipaneChart();
        multipaneChart.addChart(chart);
        multipaneChart.addChart(chart1);


        ChartPanel chartPanel = new ChartPanel(multipaneChart);

        chartPanel.setPreferredSize(new Dimension(500, 500));
        chartPanel.setBackground(Color.BLACK);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        add(chartPanel,BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        new MainFrame();
    }

    }
