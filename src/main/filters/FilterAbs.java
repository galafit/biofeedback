package main.filters;

import main.data.DataSeries;

/**
 * Created by galafit on 24/8/17.
 */
public class FilterAbs extends Filter{
    public FilterAbs(DataSeries inputData) {
        super(inputData);
    }

    @Override
    public int get(long index) {
        return Math.abs(inputData.get(index));
    }
}
