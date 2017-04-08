package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class LinearAxisX extends LinearAxis {


    public LinearAxisX(double min, double max) {
        super(min, max);
    }


    @Override
    public int valueToPoint(double value, Rectangle area) {

        return (int)(area.getX() + (value - min) * pointsPerUnit(area));

    }

    @Override
    public double pointsPerUnit(Rectangle area)   {
        return (area.getWidth())/(max-min);
    }
}
