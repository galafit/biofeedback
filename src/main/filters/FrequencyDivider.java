package main.filters;

import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

/**
 * Created by gala on 13/03/17.
 */
public class FrequencyDivider extends Filter {
    int divider;

    public FrequencyDivider(DataSeries inputData, int divider) {
        super(inputData);
        this.divider = divider;
    }

    @Override
    public int get(long index) {
        return inputData.get(index * divider);
    }

    @Override
    public long size() {
        return inputData.size()/divider;
    }

    @Override
    public Scaling getScaling() {
        ScalingImpl scaling = new ScalingImpl(inputData.getScaling());
         scaling.setSamplingInterval(scaling.getSamplingInterval() * divider);
         return scaling;
    }


    @Override
    public double sampleRate() {
        return inputData.sampleRate()/divider;
    }
}
