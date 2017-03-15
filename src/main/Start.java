package main;



import main.data.DataList;
import main.data.DataSeries;
import main.filters.FilterDerivativeRem;
import main.filters.FrequencyDivider;
import main.filters.HiPassCollectingFilter;

import java.io.File;


/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        //File fileToRead = new File(recordsDir, "cardio.edf");
        File fileToRead = new File(recordsDir, "devochka_copy2.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries edfSeries1 = edfData.getChannelSeries(0);
            DataSeries edfSeries2 = edfData.getChannelSeries(1);
            viewer.addGraph(edfSeries1);
            viewer.addGraph(edfSeries2);

            play(edfSeries1, 4, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static void simplePlayTest() {
        Viewer viewer = new Viewer();

        Function harmonic = new Harmonic(10, 0.1);
        Function sin = new Sin(0.5);

        /*Function mix = new Function() {
            @Override
            public double value(double x) {
                return harmonic.value(x) * sin.value(x);
            }
        };*/

        Function mix = (x) -> {return harmonic.value(x) * sin.value(x);};

        viewer.addGraph(mix, 2);
        play(mix, 4, 4);
    }


    static void filePlayTest() {
        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka_copy1.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries edfSeries1 = edfData.getChannelSeries(0);
            DataSeries edfSeries = new FrequencyDivider(edfSeries1, 5);
            int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )
            DataSeries eog = new HiPassCollectingFilter(edfSeries, eogCutOffPeriod);

            viewer.addGraph(eog);
            viewer.addPreview(new FilterDerivativeRem(eog));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void play(Function f, double duration, double volume) {
        StdAudio.play(f, duration, volume);
    }
}


