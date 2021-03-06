package main.filters;

import main.data.DataSeries;

/**
 *
 */

public class FilterLowPass extends Filter {
    private int bufferSize;
    private long indexBefore = -10;
    private long sumBefore = 0;

    public FilterLowPass(DataSeries inputData, int bufferSize) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    public FilterLowPass(DataSeries inputData, double cutOffFrequency) {
        super(inputData);
        double frequency = 1;
        if(inputData.getScaling() != null) {
            frequency = inputData.sampleRate();
        }

        bufferSize = (int) (frequency / cutOffFrequency);
    }

    @Override
    public int get(long index) {
        if (index < bufferSize) {
            return 0;
        }
        if (index >= size()- bufferSize) {
            return 0;
        }
        long sum = 0;
        if(index == (indexBefore +1)) {
            sum = sumBefore + inputData.get(index + bufferSize) - inputData.get(index - bufferSize);
            sumBefore = sum;
            indexBefore = index;
        }
        else {
            for (long i = (index - bufferSize); i < (index + bufferSize); i++) {
                sum += inputData.get(i);
            }
        }
        return (int)(sum/(2*bufferSize));
    }
}
