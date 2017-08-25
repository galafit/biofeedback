package main.filters;

import main.data.DataList;
import main.data.DataSeries;
import main.data.ScalingImpl;

/**
 * Created by galafit on 24/8/17.
 */
public class FilterIntegral extends Filter {
    private DataList outputData;
    private long counter = 0;
    private long result = 0;

    public FilterIntegral(DataSeries inputData) {
        super(inputData);
        outputData = new DataList();
        ScalingImpl scaling = new ScalingImpl(inputData.getScaling());
        outputData.setScaling(scaling);
        collectData();
    }

    public int getNext() {
        result += Math.abs(inputData.get(counter));
        counter++;
        result -= result / 4;
        if(result >= Integer.MAX_VALUE) {
            throw new RuntimeException("Filter integral outside of Integer: "+"Value = "+result+", Integer.MAX_VALUE = "+Integer.MAX_VALUE);
        }
        return (int) result;
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

}
