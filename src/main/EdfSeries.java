package main;

/**
 * Created by hdablin on 04.03.17.
 */
public interface EdfSeries {
    long size();
    int get(long index);
    long start();
    double sampleRate();
}
