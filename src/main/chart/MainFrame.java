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
        for (int i = -1000; i <1500 ; i++) {
            xyList.addItem(i,rand.nextInt(100)/10.0);
        }

        XYList xyList2 = new XYList();
        for (int i = 0; i <6 ; i++) {
            xyList2.addItem(4057.0789,i);
        }

        chart.addGraph(new LineGraph(),xyList);
        //chart.addGraph(new LineGraph(), xyList2, 1,1);

        Function2D foo = new Foo();
        chart.addGraph(new LineGraph(), foo);


        PeriodicData periodicData = new PeriodicData(0,1);
        for (int i = 0; i <15000; i++) {
            periodicData.addData(i);
        }
        SliceDataList sliceDataList = new SliceDataList(periodicData);

        Chart chart1 = new Chart();
        chart1.addGraph(new AreaGraph(),sliceDataList);
        Function2D sin = new Sin();
        chart1.addGraph(new LineGraph(),sin);

        Function2D tg = new Tg();
        //chart1.addGraph(new LineGraph(), tg);


        ChartWithPreview chartWithPreview = new ChartWithPreview();

        chartWithPreview.addChart(chart1);
        chartWithPreview.addChart(chart);
        chartWithPreview.addPreviewPanel();




        PreviewChartPanel chartPanel = new PreviewChartPanel(chartWithPreview);
        //ChartPanel chartPanel = new ChartPanel(chart);

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
