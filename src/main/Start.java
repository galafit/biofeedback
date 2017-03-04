package main;


import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {;




        Function f0 = new Harmonic(1, 0.1);
        Function fa = new Harmonic(2,0.1);


        Function ft = new Function() {
            @Override
            public double value(double x) {
                return f0.value(x) + fa.value(x);
            }
        };

        Function fl = (x) -> {return f0.value(x) + fa.value(x);};


    /*    show(f1, 2);
        play(f1, 10,  4);

        File file = new File(System.getProperty("user.dir"), "cat_purr.wav");
        StdAudio.playFile(file, 1);

        show(plus(f1, f2), 2);
        play(plus(f1, f2), 10,  4); */

        show(fl, 5);



    }

    static Function plus(Function... functions) {
        return new Function() {
            @Override
            public double value(double x) {
                double result = 0;
                for (Function f: functions) {
                    result += f.value(x);
                }
                return  result;
            }
        };
    }

    static Function multi(Function f1, Function f2) {
        return new Function() {
            @Override
            public double value(double x) {
                return  f1.value(x) * f2.value(x);
            }
        };
    }

    static Function sin(double freq) {
        return  new Function() {
            @Override
            public double value(double x) {
                return Math.sin(2 * Math.PI * x * freq);
            }
        };
    }



    static Function harmonic(double freq, double pct) {
        return new Harmonic(freq,pct);

    }


    static void play(Function f, double volume, double duration) {
        int sampleRate = StdAudio.SAMPLE_RATE;
        int n = (int) (sampleRate * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = volume * f.value((double)i/sampleRate);

        }
        StdAudio.play(a);
    }

    static void show(Function f, double duration) {
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


