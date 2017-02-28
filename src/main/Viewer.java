package main;

import main.data.DataList;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gala on 13/02/17.
 */
public class Viewer {

    public static void show(DataSeries data) {
        JFrame frame = new JFrame("Graphic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphViewer graphViewer = new GraphViewer(false, false);
        graphViewer.addGraphPanel(1, true);
        graphViewer.addGraph(data);


        frame.setPreferredSize(new Dimension(800, 600));
        frame.add(graphViewer, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

    }

    public static void show(int[] data) {
        show(new DataList(data));
    }

    public static void show(double[] data) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 100;
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public int get(int index) {
                return (int) (data[index]* intScaling);
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0/intScaling);
                scaling.setSamplingInterval(1.0/1000);
                return scaling;
            }
        };
        show(dataSeries);
    }
}
