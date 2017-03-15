package main.data;

import main.Function;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 *  gain = getScaling().getGain();
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
        if (x > size()){
            return 0;
        }
        return get((int)x);
    }
}
