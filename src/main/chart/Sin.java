package main.chart;

/**
 * Created by hdablin on 19.06.17.
 */
public class Sin implements Function2D{
    @Override
    public double apply(double value) {
        return Math.sin(value);
    }
}
