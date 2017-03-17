package main.filters;

import main.data.DataSeries;
import main.data.Scaling;

/**
 * Created by gala on 17/03/17.
 */
public class AccelerometerMovement implements DataSeries {
    private int step = 2;
    private DataSeries accX;
    private DataSeries accY;
    private DataSeries accZ;


    public AccelerometerMovement(DataSeries accX, DataSeries accY, DataSeries accZ) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
    }

    @Override
    public int get(long index) {
        return delta(accX, index) + delta(accY, index) + delta(accZ, index);
    }

    /**
     * Определяем разницу между макс и мин значениями на заданном колличестве точек
     */
    public int delta(DataSeries data,  long index) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i <= Math.min(step, index); i++) {
            max = Math.max(max, data.get(index - i));
            min = Math.min(min, data.get(index - i));
        }
        return Math.abs(max - min);
    }

    @Override
    public double start() {
        return accX.start();
    }

    @Override
    public double sampleRate() {
        return accX.sampleRate();
    }


    @Override
    public long size() {
       return accX.size();
    }

    @Override
    public Scaling getScaling() {
       return accX.getScaling();
    }
}
