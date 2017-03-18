package main;


import main.data.DataSeries;
import main.filters.*;
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



            DataSeries alfa = new FilterHiPass(new FilterBandPass_Alfa(eog), 2);

            DataSeries alfa_contur = new FilterAlfa(eog);


            Function sin = new Sin(80);


            Function mix = (x) -> {
                return   alfa_contur.value(x) * sin.value(x);
            };
            /* Function mix1 = new Function() {
                @Override
                public double value(double x) {
                    return eog.value(x) * sin.value(x);
                }
            }; */
            viewer.addGraph(eog);
            viewer.addGraph(alfa);
            viewer.addGraph(mix);
            viewer.addPreview(new FilterDerivativeRem(eog));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


