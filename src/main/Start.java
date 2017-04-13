package main;


import main.data.DataSeries;
import main.filters.*;
import main.functions.Function;
import main.functions.HarmonicPick;
import main.functions.HarmonicRect;
import main.functions.Sin;
import uk.me.berndporr.iirj.Butterworth;

import java.io.File;


/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
       edfTest();
    }

    static void edfTest() {
        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "ekgcopy3.edf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            ChannelData eog = edfData.getChannelData(0);

            viewer.addGraph(eog);
            viewer.addPreview(new FilterDerivativeRem(eog));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void filterTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )


        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            ChannelData eog = edfData.getChannelData(0);
            Butterworth bw = new Butterworth();
            bw.highPass(1,eog.sampleRate(),1);
            eog.setFilter(bw);

            ChannelData eog1 = edfData.getChannelData(0);

            viewer.addGraph(eog);
            viewer.addGraph(eog1);
            viewer.addPreview(new FilterDerivativeRem(eog));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void funcitonTest() {
        HarmonicRect harmonicRect = new HarmonicRect(1, 0);
       // HarmonicPick harmonic = new HarmonicPick(10, 1);
        Function freqFunction = x -> {return 1 + x;};
        Function pctFunction = x -> {
            long x_int = (long) x + 1;
            double pct = x_int  * 0.05;
            if(pct > 0.5) {
                pct = 0.5;
            }
            return pct;
        };
        HarmonicPick harmonic = new HarmonicPick(2, pctFunction);

        Viewer viewer = new Viewer();
        viewer.addGraph(harmonic, 6 );

    }

    static void filePlayTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )


        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries eog_full = edfData.getChannelData(0);
            DataSeries accX = edfData.getChannelData(2);
            DataSeries accY = edfData.getChannelData(3);
            DataSeries accZ = edfData.getChannelData(4);

            DataSeries eog = new HiPassCollectingFilter(eog_full, eogCutOffPeriod);

            DataSeries acc = new AccelerometerMovement(accX, accY, accZ);

            DataSeries alfa = new FilterHiPass(new FilterBandPass_Alfa(eog), 2);
            DataSeries alfa_contur = new FilterAlfa(eog);

            Function alfa_time = (x) -> {
                double f = alfa_contur.value(x) / 600;
                if (f > 0.5) {
                    f = 0.5;
                }
                if (f < 0.01) {
                    f = 0.01;
                }
                return f;
            };

            Function harmoharmonicPickic = new HarmonicPick(10, alfa_time);
            Function sin = new Sin(200);

            Function mix = (x) -> {
                return   alfa.value(x) * sin.value(x);
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


