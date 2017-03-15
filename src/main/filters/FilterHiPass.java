package main.filters;

import main.data.DataSeries;

/**
 * Created by mac on 20/02/15.
 */
public class FilterHiPass extends Filter {
    int bufferSize;

    public FilterHiPass(DataSeries inputData, int bufferSize) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    public int get(long index) {
        long sum = 0;
        if( bufferSize == 0) {
            return inputData.get(index);
        }
        if (index <= bufferSize) {
            for (int i = 0; i <= index; i++) {
                sum += inputData.get(i);
            }
            return inputData.get(index) - (int) (sum / (index + 1));
        }

        for (long i = index - bufferSize; i <=index; i++) {
            sum += inputData.get(i);
        }
        return inputData.get(index) - (int) (sum / (bufferSize + 1));
    }
}
