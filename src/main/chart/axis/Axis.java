package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 16.04.17.
 */
public abstract class Axis {
    protected AxisData axisData;
    protected AxisPainter axisPainter;
    protected AxisViewSettings axisViewSettings;


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

    public void setRange(double min, double max){
        axisData.setRange(min, max);
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
