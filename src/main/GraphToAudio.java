package main;

import main.data.DataSeries;
import main.data.ScalingImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by galafit on 22/8/17.
 */
public class GraphToAudio {
    DataSeries data;
    long currentIndex;
    private AudioPositionListener listener;
    Future<?> future;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private double[] values = new double[11];
    private int[] frequencies = new int[11];


    public GraphToAudio(DataSeries data, AudioPositionListener listener) {
        this.data = data;
        this.listener = listener;
        // dependency: frequency(value) we define as a table.
        values[0] = 2;  frequencies[0] = 10;
        values[1] = 10; frequencies[1] = 20;
        values[2] = 20; frequencies[2] = 30;
        values[3] = 30; frequencies[3] = 55;
        values[4] = 40; frequencies[4] = 100;
        values[5] = 50; frequencies[5] = 190;
        values[6] = 60; frequencies[6] = 350;
        values[7] = 70; frequencies[7] = 650;
        values[8] = 80; frequencies[8] = 1200;
        values[9] = 90; frequencies[9] = 2400;
        values[10] = 100; frequencies[10] = 4000;
    }

    public void play(long startIndex) {
        currentIndex = startIndex;
        future = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int audioFrequency = valueToAudioFrequency(data.getPhysical(currentIndex));
                System.out.println(currentIndex + " Value: "+data.getPhysical(currentIndex)+" Frequency: "+audioFrequency);
                if (audioFrequency > 0) {
                    StdAudio.play(audioFrequency, 1);
                }
                listener.handleAudioPosition(currentIndex);
                currentIndex++;
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        future.cancel(true);
    }


    private int valueToAudioFrequency(double value) {
        value = Math.abs(value);
        for (int i = 0; i < values.length - 2; i++) {
            if(value > values[i] && value < values[i+1]) {
                double scale = (frequencies[i+1] - frequencies[i]) / (values[i+1] - values[i]);
                double audioFrequency = frequencies[i] + scale * (value - values[i]);
                return (int) audioFrequency;
            }
        }
        return 0;
    }

    private int valueToAudioVolume() {
        return 1;
    }
}
