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

    public void setRange(int min, int max){
        axisData.setMin(min);
        axisData.setMax(max);
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }

    public int getSize(Graphics g, Rectangle area){
       return axisPainter.getSize(g, area);
    }
    public AxisViewSettings getViewSettings() {
        return axisViewSettings;
    }

    public void setViewSettings(AxisViewSettings axisViewSettings) {
        this.axisViewSettings = axisViewSettings;
    }

    public boolean isHorizontal() {return axisData.isHorizontal();}
}
