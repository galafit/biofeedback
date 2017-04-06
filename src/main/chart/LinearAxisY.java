package main.chart;

/**
 * Created by hdablin on 06.04.17.
 */
public class LinearAxisY extends Axis {

    public LinearAxisY(double min, double max, int pointMin, int pointMax) {
        super(min, max, pointMin, pointMax);
    }


    @Override
    public int valueToPoint(double value) {

        return (int)(pointMin + (value - min) * pointsPerUnit());

    }
}
