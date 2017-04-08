package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class LinearAxisY extends LinearAxis {

    public LinearAxisY(double min, double max) {
        super(min, max);
    }


    @Override
    public int valueToPoint(double value, Rectangle area) {

        return (int)(area.getY() + (value - min) * pointsPerUnit(area));

    }

    @Override
    public double pointsPerUnit(Rectangle area)   {
        return (area.getHeight())/(max-min);
    }
}
