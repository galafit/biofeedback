package main.filters;

import main.data.DataSeries;
import main.data.Scaling;

/**
 *
 */
public abstract class Filter implements DataSeries {
    protected DataSeries inputData;

    protected Filter(DataSeries inputData) {
        this.inputData = inputData;
    }

    @Override
    public long size() {
        return inputData.size();
    }

    @Override
    public Scaling getScaling() {
        return inputData.getScaling();
    }


    @Override
    public double start() {
        return (long)inputData.getScaling().getStart();
    }

    @Override
    public double sampleRate() {
        return 1.0 / inputData.getScaling().getSamplingInterval();
    }
}

