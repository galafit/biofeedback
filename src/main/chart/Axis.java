package main.chart;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class Axis {
    protected double min;
    protected double max;
    protected int pointMin;
    protected int pointMax;

    public Axis(double min, double max, int pointMin, int pointMax) {
        this.min = min;
        this.max = max;
        this.pointMin = pointMin;
        this.pointMax = pointMax;
    }

    public double pointsPerUnit(){
        return (pointMax-pointMin)/(max-min);
    }

    abstract public int valueToPoint(double value);

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getPointMin() {
        return pointMin;
    }

    public void setPointMin(int pointMin) {
        this.pointMin = pointMin;
    }

    public int getPointMax() {
        return pointMax;
    }

    public void setPointMax(int pointMax) {
        this.pointMax = pointMax;
    }
}
