package main;


import main.data.DataSeries;
import main.filters.*;
import main.functions.Function;
import main.functions.HarmonicPick;
import main.functions.HarmonicRect;
import main.functions.Sin;
import uk.me.berndporr.iirj.Butterworth;

import javax.xml.crypto.Data;
import java.io.File;


/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
       filePlayTest();
        // funcitonTest();
        //playTest();
    }

    static void playTest() {
        StdAudio.play(10, 1);
        StdAudio.play(11, 1);
        StdAudio.play(24, 1);
        StdAudio.play(33, 1);

       /* StdAudio.play(40, 1);
        StdAudio.play(120, 1);
        StdAudio.play(130, 1);
        StdAudio.play(140, 1);
        StdAudio.play(150, 1);
        StdAudio.play(2120, 1);
        StdAudio.play(3120, 1);
        StdAudio.play(4000, 1);*/
    }

    static void edfTest() {
        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "13_06_2017sasha_night_velik.bdf");
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
        HarmonicPick harmonic = new HarmonicPick(10, pctFunction);

        Function rect =  new HarmonicRect(10, 0);
        Viewer viewer = new Viewer();
        viewer.addGraph(harmonic, 6 );
        viewer.addGraph(rect, 5 );

    }

    static void filePlayTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )

        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "Gena-2017-08-25-manana.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries eog_full = edfData.getChannelData(0);
            DataSeries accX = edfData.getChannelData(1);
            DataSeries accY = edfData.getChannelData(2);
            DataSeries accZ = edfData.getChannelData(3);
            if(edfData.getNumberOfChannels() == 5) {
                accX = edfData.getChannelData(2);
                accY = edfData.getChannelData(3);
                accZ = edfData.getChannelData(4);
            }


            int resultantFrequency =  50; // hz
            int eog_divider = (int) eog_full.sampleRate() / resultantFrequency;

            DataSeries eog1 = new FrequencyDividingCollectingFilter(eog_full, eog_divider);

            DataSeries eog = new HiPassCollectingFilter(eog1, eogCutOffPeriod);

            DataSeries eog_clean = new FilterLowPass(eog, 2.0);


            int acc_divider = (int) accX.sampleRate() / resultantFrequency;
            DataSeries acc = new FrequencyDividingCollectingFilter(new AccelerometerMovement(accX, accY, accZ), acc_divider);


            DataSeries alfa = new FilterHiPass(new FilterBandPass_Alfa(eog1), 2);

            viewer.addGraph(eog);
            viewer.addGraph(eog_clean);
            viewer.addGraph(new FilterAbs(new FilterDerivativeAvg(eog)), false);
         //   viewer.addGraph(new FilterIntegral(new FilterDerivativeAvg(eog)), false);

           // viewer.addGraph(new FilterDerivativeRem(eog));
          //  viewer.addGraph(new FilterNormalize(eogDerivative, 50));
            viewer.addGraph(alfa);
           // viewer.addGraph(mix);
            viewer.addGraph(acc, false);
            viewer.addPreview(new FilterDerivativeRem(eog1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


