package main.chart;

import main.chart.axis.AxisType;
import main.chart.functions.Foo;
import main.chart.functions.Function2D;
import main.chart.functions.Sin;
import main.chart.functions.Tg;

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

        chart.addYAxis(AxisType.LINEAR, true);
       // chart.addXAxis(AxisType.LINEAR, true);

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
       // chart.addGraph(new LineGraph(), xyList2, 1,1);


        Function2D sin = new Sin();
        //chart.addGraph(new LineGraph(),sin);

        Function2D tg = new Tg();
        //chart.addGraph(new LineGraph(), tg);

        Function2D foo = new Foo();
        chart.addGraph(new LineGraph(), foo);

        Chart chart1 = new Chart();

        xyList.addItem(18,30);
        xyList.addItem(-3,17);
        chart1.addGraph(new LineGraph(),xyList);


        chart1.addYAxis(AxisType.LINEAR, false);
        chart1.addYAxis(AxisType.LINEAR, false);

      //  chart1.addXAxis(AxisType.LINEAR, true);
      //  chart1.addXAxis(AxisType.LINEAR, true);

        MultipaneChart multipaneChart = new MultipaneChart();
        multipaneChart.addChart(chart,1);
        multipaneChart.addChart(chart1,2);


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
