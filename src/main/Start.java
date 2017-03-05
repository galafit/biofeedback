package main;


import com.biorecorder.edflib.HeaderParsingException;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
        Viewer viewer = new Viewer();

        Function f0 = new Harmonic(1, 0.1);
        Function fa = new Harmonic(2,0.1);

        Function ft = new Function() {
            @Override
            public double value(double x) {
                return f0.value(x) + fa.value(x);
            }
        };

        Function fl = (x) -> {return f0.value(x) + fa.value(x);};
        // show(fl, 5);

        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "cardio.edf");
        try {
            EdfList edfList = new EdfList(fileToRead);
            viewer.show(edfList.getChannelSeries(0));
            viewer.show(edfList.getChannelSeries(1));

        } catch (Exception e) {
            e.printStackTrace();
        }

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

}


