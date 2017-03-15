package main.filters;

import main.data.DataList;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

public class HiPassCollectingFilter implements DataSeries {
    private DataSeries inputData;
    private DataList outputData;
    int bufferSize;
    private long counter;
    private long sum;

    public HiPassCollectingFilter(DataSeries inputData, double cutOffInterval ) {
        this.inputData = inputData;
        outputData = new DataList();

        double samplingInterval = 1;
        if(inputData.getScaling() != null) {
            samplingInterval = inputData.getScaling().getSamplingInterval();
        }
        bufferSize = (int)(cutOffInterval / samplingInterval);
        collectData();
    }

    public int getNext() {
        if(bufferSize == 0) {
            return inputData.get(counter++);
        }
        if (counter <= bufferSize) {
            sum += inputData.get(counter);
            return inputData.get(counter++) - (int) (sum / (counter));
        }
        else {
            sum += inputData.get(counter) - inputData.get(counter - bufferSize - 1);
        }

        return inputData.get(counter++) - (int) (sum / (bufferSize+1));
    }

    private void collectData() {
        if (outputData.size()  < inputData.size()) {
            for (long i = outputData.size(); i < inputData.size(); i++) {
                outputData.add(getNext());
            }
        }
    }

    @Override
    public int get(long index) {
        collectData();
        return outputData.get(index);
    }


    @Override
    public long size() {
        collectData();
        return outputData.size();
    }

    @Override
    public Scaling getScaling() {
        ScalingImpl scaling = new ScalingImpl(inputData.getScaling());
        if(bufferSize > 0) {
            scaling.setDataOffset(0);
        }
        outputData.setScaling(scaling);
        return outputData.getScaling();
    }


    @Override
    public double start() {
        return inputData.getScaling().getStart();
    }

    @Override
    public double sampleRate() {
        return 1.0 / inputData.getScaling().getSamplingInterval();
    }
}
