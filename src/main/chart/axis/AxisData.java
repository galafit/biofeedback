package main.chart.axis;

import main.chart.TickProvider;

import java.awt.*;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    protected double min = 0;
    protected double max = 1;
    protected AxisPosition axisPosition;


    public AxisData(AxisPosition axisPosition) {
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
