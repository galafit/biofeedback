package main.chart.axis;

import main.chart.TickProvider;

import java.awt.*;

/**
 * Created by hdablin on 16.04.17.
 */
public abstract class Axis {
    protected AxisData axisData;
    protected AxisPainter axisPainter;
    protected AxisViewSettings axisViewSettings;

    public int valueToPoint(double value, Rectangle area){
        return axisData.valueToPoint(value, area);
    }

    public void setMin(double min) {
        axisData.setMin(min);
    }

    public void setMax(double max) {
        axisData.setMax(max);
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }


    public AxisViewSettings getViewSettings() {
        return axisViewSettings;
    }

    public void setViewSettings(AxisViewSettings axisViewSettings) {
        this.axisViewSettings = axisViewSettings;
    }

    public boolean isHorizontal() {return axisData.isHorizontal();}
}
