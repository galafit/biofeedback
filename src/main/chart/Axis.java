package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class Axis {
    protected double min;
    protected double max;
    protected AxisPosition axisPosition;


    public Axis(double min, double max, AxisPosition axisPosition) {
        this.min = min;
        this.max = max;
        this.axisPosition = axisPosition;

    }

    public AxisPosition getAxisPosition() {
        return axisPosition;
    }

    abstract public double pointsPerUnit(Rectangle area);

    abstract public int valueToPoint(double value, Rectangle area);

    public double getMin() {return min;}

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    abstract public TickProvider getTicksProvider(Rectangle area);


}
