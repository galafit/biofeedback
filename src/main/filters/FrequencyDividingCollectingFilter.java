package main.filters;

import main.data.DataList;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

/**
 * Created by galafit on 20/8/17.
 */
public class FrequencyDividingCollectingFilter implements DataSeries {
    private DataSeries inputData;
    private DataList outputData;
    private int divider;

    public FrequencyDividingCollectingFilter(DataSeries inputData, int divider) {
        this.inputData = inputData;
        this.divider = divider;
        outputData = new DataList();
        ScalingImpl scaling = new ScalingImpl(inputData.getScaling());
        scaling.setSamplingInterval(scaling.getSamplingInterval() * divider);
        outputData.setScaling(scaling);
    }

    private void collectData() {
        if (outputData.size() < size()) {
            int counter = 0;
            long sum = 0;
            long start = outputData.size() * divider;
            for (long i = start; i < inputData.size(); i++) {
                counter++;
                sum += inputData.get(i);
                if(counter == divider) {
                    outputData.add((int)(sum / divider));
                    counter = 0;
                    sum = 0;
                }
            }
        }
    }

    @Override
    public double start() {
        return inputData.start();
    }

    @Override
    public double sampleRate() {
        return inputData.sampleRate() / divider;
    }

    @Override
    public int get(long index) {
        collectData();
        return outputData.get(index);
    }


    @Override
    public long size() {
        return inputData.size() / divider;
    }

    @Override
    public Scaling getScaling() {
        return outputData.getScaling();
    }

}
