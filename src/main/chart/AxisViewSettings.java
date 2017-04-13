package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 13.04.17.
 */
public class AxisViewSettings {
    private Color axisColor = Color.GRAY;
    private int labelFontSize = 9;
    private Color gridColor = Color.GRAY;
    private Boolean isGridVisible = true;
    private int tickSize = 5;
    protected int tickPixelInterval =10;

    public int getTickPixelInterval() {
        return tickPixelInterval;
    }

    public void setTickPixelInterval(int tickPixelInterval) {
        this.tickPixelInterval = tickPixelInterval;
    }

    public int getTickSize() {
        return tickSize;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    public Color getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public int getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(int labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public Boolean getGridVisible() {
        return isGridVisible;
    }

    public void setGridVisible(Boolean gridVisible) {
        isGridVisible = gridVisible;
    }
}
