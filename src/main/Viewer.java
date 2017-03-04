package main;

import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gala on 04/03/17.
 */
public class Viewer extends JFrame{
    GraphViewer graphViewer;

    public Viewer() {
        setTitle("Graphic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphViewer = new GraphViewer(false, false);
        graphViewer.addGraphPanel(1, true);
        setPreferredSize(new Dimension(1100, 600));
        add(graphViewer, BorderLayout.CENTER);
        pack();
        setVisible(true);

    }

    public void show(DataSeries dataSeries) {
        graphViewer.addGraph(dataSeries);
    }

    public void show(TimeSeries timeSeries) {
        DataSeries dataSeries = new DataSeries() {
            @Override
            public int size() {
                return (int) timeSeries.size();
            }

            @Override
            public int get(int index) {
                return timeSeries.get(index);
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setSamplingInterval(1.0/timeSeries.sampleRate());
                scaling.setStart(timeSeries.start());
                scaling.setTimeSeries(true);
                return scaling;
            }
        };
        show(dataSeries);
    }

    public void show(Function f, double duration) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;
            int numberOfPoints = 1000;
            int sampleRate = (int)(numberOfPoints/duration);
            @Override
            public int size() {
                return  (int) (duration * sampleRate);
            }

            @Override
            public int get(int index) {
                double x = (double)(index) / sampleRate;

                return (int) (f.value(x) * intScaling);
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0/intScaling);
                scaling.setSamplingInterval(1.0/sampleRate);
                return scaling;
            }
        };
        show(dataSeries);
    }

    public void show(double[] data, int sampleRate) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;
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
                scaling.setSamplingInterval(1.0/sampleRate);
                return scaling;
            }
        };
        show(dataSeries);
    }
}
