package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 16.04.17.
 */
public abstract class Axis {
    protected AxisData axisData;
    protected AxisPainter axisPainter;

    public boolean isOpposite() {
        return axisData.isOpposite();
    }

    public void setOpposite(boolean opposite) {
        axisData.setOpposite(opposite);
    }


    public void resetRange() {
        axisData.resetRange();
    }

    public boolean isVisible() {
        return axisData.getAxisViewSettings().isVisible();
    }

    public void setVisible(boolean isVisible) {
        axisData.getAxisViewSettings().setVisible(isVisible);
    }
    public AxisViewSettings getAxisViewSettings() {
        return axisData.getAxisViewSettings();
    }

    public TicksSettings getTicksSettings() {
        return axisData.getTicksSettings();
    }

    public GridSettings getGridSettings() {
        return axisData.getGridSettings();
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

    public void draw(Graphics2D g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }

    public int getWidth(Graphics2D g, Rectangle area){
       return axisPainter.getAxisWidth(g, area);
    }
    public AxisViewSettings getViewSettings() {
        return axisData.getAxisViewSettings();
    }

    public boolean isHorizontal() {return axisData.isHorizontal();}
}
