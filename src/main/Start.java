package main;


import main.data.DataSeries;
import main.filters.AccelerometerMovement;
import main.filters.FilterDerivativeRem;
import main.filters.FrequencyDivider;
import main.filters.HiPassCollectingFilter;
import main.functions.Function;
import main.functions.Harmonic;
import main.functions.Sin;

import java.io.File;


/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
        filePlayTest();
        //current();

    }

    static void current() {
        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        //File fileToRead = new File(recordsDir, "cardio.edf");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries edfSeries1 = edfData.getChannelSeries(0);
            DataSeries edfSeries2 = edfData.getChannelSeries(1);
            viewer.addGraph(edfSeries1);
            viewer.addGraph(edfSeries2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void filePlayTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )


        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries eog_full = edfData.getChannelSeries(0);
            DataSeries accX = edfData.getChannelSeries(2);
            DataSeries accY = edfData.getChannelSeries(3);
            DataSeries accZ = edfData.getChannelSeries(4);

            DataSeries eog = new HiPassCollectingFilter(
                    new FrequencyDivider(eog_full, 5), eogCutOffPeriod);

            DataSeries acc = new AccelerometerMovement(
                    new FrequencyDivider(accX, 5),
                    new FrequencyDivider(accY, 5),
                    new FrequencyDivider(accZ, 5));


            Function sin = new Sin(80);


            Function mix = (x) -> {
                return eog.value(x) + acc.value(x) + 15000 * sin.value(x);
            };

            /* Function mix1 = new Function() {
                @Override
                public double value(double x) {
                    return eog.value(x) * sin.value(x);
                }
            }; */

            viewer.addGraph(eog);
            viewer.addGraph(acc);
            viewer.addGraph(mix);
            viewer.addPreview(new FilterDerivativeRem(eog));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


