package main.filters;

import main.data.DataSeries;

/**
 * Created by galafit on 23/8/17.
 */
public class FilterNormalize extends Filter {
    int normalizedMax = 100;
    double scale;

    public FilterNormalize(DataSeries inputData, double max) {
        super(inputData);
        scale = normalizedMax / max;
    }

    @Override
    public int get(long index) {
        return (int)(inputData.get(index) * scale);
    }
}
