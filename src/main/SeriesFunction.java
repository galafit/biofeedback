package main;

import main.data.DataSeries;

/**
 * Created by gala on 05/03/17.
 */
public class SeriesFunction implements Function {
    DataSeries dataSeries;
    double sampleRate;
    double max = 1;
    long startIndex;

    public SeriesFunction(DataSeries edfSeries, long startIndex, double max) {
        this.dataSeries = edfSeries;
        this.max = max;
        this.startIndex = startIndex;
        sampleRate = edfSeries.sampleRate();
    }

    @Override
    public double value(double x) {
        x = x  * sampleRate + startIndex;
        if (x < 0) {
            return 0;
        }
        if (x > dataSeries.size()){
            return 0;
        }
        return dataSeries.get((int)x)/max;
    }

}
