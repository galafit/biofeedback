package main.filters;

import main.data.DataSeries;

/**
 *
 */
public class FilterAlfa extends Filter {
    private int period = 4;
    private int bufferHalf = period * 4;
    private DataSeries alfaData;

    public FilterAlfa(DataSeries inputData) {
        super(inputData);
        alfaData = new FilterHiPassSymmetric(new FilterBandPass_Alfa(inputData), 2);
    }

    @Override
    public int get(long index) {
        if(index < 1 ) {
            return 0;
        }
        else {
            return Math.max(Math.abs(alfaData.get(index)) , Math.abs(alfaData.get(index-1)));

        }
    }
}