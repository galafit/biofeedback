package main.filters;

import main.data.DataSeries;

public class FilterFourierIntegral extends Filter {

    public FilterFourierIntegral(DataSeries inputData) {
        super(inputData);
    }

    private double getFrequency() {
        double frequency = 1;
        if(inputData.getScaling() != null){
            frequency = 1 / inputData.getScaling().getSamplingInterval();
        }
        return frequency;
    }

    @Override
    public int get(long index) {
        double frequencyStep = 1 / getFrequency();
        double frequency = index * frequencyStep;
        if(frequency < 0.1)  {
            return 0;
        }
        double delta = frequency * 0.05;
        int numberOfPoints = (int)(delta / frequencyStep);
        int result = 0;
        for( int i = Math.max(0, (int)index - numberOfPoints); i <= Math.min(inputData.size(), index + numberOfPoints); i++ ) {
            result = result + inputData.get(i);
        }
        return result;
    }

    @Override
    public long size() {
        int index40hz = (int)(40 * getFrequency());
        return Math.min(inputData.size(), index40hz);
    }
}
