package main.chart.axis;

import main.chart.TickProvider;

import java.awt.*;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    protected double min = 0;
    protected double max = 1;
    protected boolean isHorizontal;
    protected boolean isAutoScale = true;

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    abstract public double pointsPerUnit(Rectangle area);

    abstract public int valueToPoint(double value, Rectangle area);

    public double getMin() {return min;}

    public double getMax() {
        return max;
    }

    public void setRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    abstract public TickProvider getTicksProvider(Rectangle area);

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }
}
