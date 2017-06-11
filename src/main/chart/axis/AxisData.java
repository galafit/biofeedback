package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    private Double min = null;
    private Double max = null;
    private final double DEFAULT_MIN = 0;
    private final double DEFAULT_MAX = 1;
    protected boolean isHorizontal;
    protected boolean isAutoScale = true;
    private double lowerPadding = 0.02;
    private double upperPadding = 0.02;

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    abstract public double pointsPerUnit(Rectangle area);

    abstract public int valueToPoint(double value, Rectangle area);

    public Double getMin() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMin = (!isAutoScale()) ? resultantMin : resultantMin - lowerPadding * (resultantMax - resultantMin);

        return resultantMin;

    }

    public Double getMax() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMax = (!isAutoScale()) ? resultantMax : resultantMax + upperPadding * (resultantMax - resultantMin);
        return resultantMax;

    }

    public void setRange(double min, double max) {
        if (!isAutoScale) {
            this.min = min;
            this.max = max;
        } else {
            if (this.min == null) {
                this.min = min;
            }
            if (this.max == null){
                this.max = max;
            }
            this.min = Math.min(min, this.min);
            this.max = Math.max(max, this.max);
        }
    }

    abstract public TickProvider getTicksProvider(Rectangle area);

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }
}
