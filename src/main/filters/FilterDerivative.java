package main.filters;

import main.data.DataSeries;

/**
 *
 */

public class FilterDerivative extends Filter {

    public FilterDerivative(DataSeries inputData) {
        super(inputData);
    }

    @Override
    public int get(long index) {
        if (index == 0) {
            return 0;
        }
        return inputData.get(index) - inputData.get(index - 1);
    }
}
