package main;

/**
 * Created by hdablin on 04.03.17.
 */
public interface TimeSeries {
    long size();
    int get(long index);
    long start();
    double sampleRate();
}
