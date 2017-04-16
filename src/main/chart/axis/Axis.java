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

    public TickProvider getTicksProvider(Rectangle area){
        return axisData.getTicksProvider(area);
    }

    public AxisData getAxisData() {
        return axisData;
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }

    public void setAxisData(AxisData axisData) {
        this.axisData = axisData;
    }

    public AxisPainter getAxisPainter() {
        return axisPainter;
    }

    public void setAxisPainter(AxisPainter axisPainter) {
        this.axisPainter = axisPainter;
    }

    public AxisViewSettings getAxisViewSettings() {
        return axisViewSettings;
    }

    public void setAxisViewSettings(AxisViewSettings axisViewSettings) {
        this.axisViewSettings = axisViewSettings;
    }
}
