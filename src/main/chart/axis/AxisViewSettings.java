package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 13.04.17.
 */
public class AxisViewSettings {
    private Color axisColor = Color.GRAY;
    private int labelFontSize = 11;
    private Color gridColor = Color.DARK_GRAY;
    private Color minorGridColor = Color.BLUE;
    private boolean isGridVisible = true;
    private int tickSize = 5;
    protected int tickPixelInterval = 100;
    private int minLabelSpace = 5;
    private boolean isAxisLineVisible = true;
    private boolean isTicksVisible = true;
    private int minorGridDivider = 5;
    private boolean isMinorGridVisible = false;
    private boolean isOpposite = false;

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public boolean isMinorGridVisible() {
        return isMinorGridVisible;
    }

    public void setMinorGridVisible(boolean minorGridVisible) {
        isMinorGridVisible = minorGridVisible;
    }

    public int getMinorGridDivider() {
        return minorGridDivider;
    }

    public void setMinorGridDivider(int minorGridDivider) {
        this.minorGridDivider = minorGridDivider;
    }

    public Color getMinorGridColor() {
        return minorGridColor;
    }

    public void setMinorGridColor(Color minorGridColor) {
        this.minorGridColor = minorGridColor;
    }

    public boolean isAxisLineVisible() {
        return isAxisLineVisible;
    }

    public void setAxisLineVisible(boolean axisLineVisible) {
        isAxisLineVisible = axisLineVisible;
    }

    public boolean isTicksVisible() {
        return isTicksVisible;
    }

    public void setTicksVisible(boolean ticksVisible) {
        isTicksVisible = ticksVisible;
    }

    public int getMinLabelSpace() {
        return minLabelSpace;
    }

    public void setMinLabelSpace(int minLabelSpace) {
        this.minLabelSpace = minLabelSpace;
    }

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

    public boolean isGridVisible() {
        return isGridVisible;
    }

    public void setGridVisible(Boolean gridVisible) {
        isGridVisible = gridVisible;
    }
}
