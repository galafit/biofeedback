package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 16.04.17.
 */
public abstract class Axis {
    protected AxisData axisData;
    protected AxisPainter axisPainter;
    protected AxisViewSettings axisViewSettings;

    public void resetRange() {
        axisData.resetRange();
    }

    public boolean isVisible() {
        return axisViewSettings.isVisible();
    }

    public void setVisible(boolean isVisible) {
        axisViewSettings.setVisible(isVisible);
    }



    public void setHorizontal(boolean isHorisontal){
        axisData.setHorizontal(isHorisontal);
    }

    public boolean isAutoScale() {
       return axisData.isAutoScale();
    }

    public void setAutoScale(boolean isAutoScale){
        axisData.setAutoScale(isAutoScale);
    }

    public int valueToPoint(double value, Rectangle area){
        return axisData.valueToPoint(value, area);
    }

    /**
     * If isAutoScale = FALSE this method simply sets: min = newMin, max = newMax.
     * But if isAutoScale = TRUE then it only extends the range and sets:
     * min = Math.min(min, newMin), max = Math.max(max, newMax).
     *
     * @param newMin new min value
     * @param newMax new max value
     */
    public void setRange(double newMin, double newMax) {
        axisData.setRange(newMin, newMax);
    }

    public double getMin() {return axisData.getMin();}

    public double getMax() {
        return axisData.getMax();
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }

    public int getWidth(Graphics g, Rectangle area){
       return axisPainter.getAxisWidth(g, area);
    }
    public AxisViewSettings getViewSettings() {
        return axisViewSettings;
    }

    public void setViewSettings(AxisViewSettings axisViewSettings) {
        this.axisViewSettings = axisViewSettings;
    }

    public boolean isHorizontal() {return axisData.isHorizontal();}
}
