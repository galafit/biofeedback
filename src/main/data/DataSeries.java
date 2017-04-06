package main.data;

import main.functions.Function;

/**
 *  Scaling gives us actual dependency dataValue(indexValue)
 *  based on the given  dependency of integers: main.data(index)
 *
 *  indexValue = index * stepInterval + startOffset
 *  where:
 *  stepInterval = getScaling().getSamplingInterval();
 *  startOffset = getScaling().getStart();
 *
 *  dataValue(indexValue) = get(index) * gain + offset
 *  where:
 *  gain = getScaling().pointsPerUnit();
 *  offset = getScaling().getOffset();
 */


public interface DataSeries extends Function {
    long size();
    int get(long index);
    Scaling getScaling();
    double start();
    double sampleRate();

    default double value(double x) {
        x = ((x - start())  * sampleRate());
        if (x < 0) {
            return 0;
        }
        if (x + 1 > size()){
            return 0;
        }
        long x_int = (int) x;
        double x_double = x - x_int;
        double value = (get(x_int + 1) - get(x_int)) * x_double + get(x_int);
        return value;
    }

    default double value_old(double x) {
        x = ((x - start())  * sampleRate());
        if (x < 0) {
            return 0;
        }
        if (x + 1 > size()){
            return 0;
        }

        return get((int) x);
    }
}
