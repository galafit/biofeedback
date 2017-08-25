package main.filters;

import main.data.DataSeries;

/**
 * Created by galafit on 20/8/17.
 */
public class FilterDerivativeAvg extends Filter {
    private static final int DEFAULT_DISTANCE_MSEC = 120;
    private int distance_point;

    public FilterDerivativeAvg(DataSeries inputData, int timeMs) {
        super(inputData);
        distance_point = (int) Math.round(timeMs * inputData.sampleRate() / 1000);
        System.out.println("number of avg point: "+distance_point);
    }

    public FilterDerivativeAvg(DataSeries inputData) {
        this(inputData, DEFAULT_DISTANCE_MSEC);
    }

    @Override
    public int get(long index) {
        if (index < distance_point * 2) {
            return 0;
        }

        long sum1 = 0;
        long sum2 = 0;
        for (long i = 0; i < distance_point; i++) {
            sum1 += inputData.get(index - i);
            sum2 += inputData.get(index - distance_point - i);
        }
        return (int) ((sum1 - sum2) / distance_point);
    }
}
