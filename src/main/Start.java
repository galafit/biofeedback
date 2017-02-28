package main;


import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;
import main.tmp.Sin;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {

        //show(new Sin(1), 2);
        show(new Harmonic(1, 0.2), 2);
        //play(new Sin(200), 2);
        play(new Harmonic(10, 0.2), 2);
       // File file = new File(System.getProperty("user.dir"), "cat_purr.wav");
       // StdAudio.playFile(file, 1);
       // StdAudio.playFile(file, 3);
    }


    public static void play(Function f, double duration) {
        int sampleRate = StdAudio.SAMPLE_RATE;
        int n = (int) (sampleRate * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = f.value((double)i/sampleRate);

        }
        StdAudio.play(a);
    }

    static void show(Function f,  double duration) {
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

    static void show(double[] data, int sampleRate) {
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

    static void show(DataSeries dataSeries) {
        JFrame frame = new JFrame("Graphic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphViewer graphViewer = new GraphViewer(false, false);
        graphViewer.addGraphPanel(1, true);
        frame.setPreferredSize(new Dimension(1100, 600));
        frame.add(graphViewer, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        graphViewer.addGraph(dataSeries);
    }
}


