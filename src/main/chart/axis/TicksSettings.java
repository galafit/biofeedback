package main.chart.axis;

import java.awt.*;

/**
 * Created by galafit on 13/6/17.
 */
public class TicksSettings {
    private final double DEFAULT_LABEL_PADDING_FACTOR = 0.4;
    private String tickLabelFontName = Font.SANS_SERIF;
    private int tickLabelsFontSize = 11;
    private Integer tickLabelsPadding = null;
    private boolean isTickLabelsVisible = true;
    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
    private int[] tickLabelAutoRotation = {-45, 90}; // at the moment not used

    private int ticksWidth = 1;
    private int tickSize = 2;
    private Integer tickPixelInterval = 100;
    private Double tickInterval = null; // in axis unit (tickUnit)

    public boolean isTickMarkVisible() {
        return (ticksWidth > 0) ? true : false;
    }

    public String getTickLabelFontName() {
        return tickLabelFontName;
    }
    public void setTickLabelFontName(String tickLabelFontName) {
        this.tickLabelFontName = tickLabelFontName;
    }

    public int getTickLabelsFontSize() {
        return tickLabelsFontSize;
    }

    public void setTickLabelsFontSize(int tickLabelsFontSize) {
        this.tickLabelsFontSize = tickLabelsFontSize;
    }

    public int getTickLabelsPadding() {
        return (tickLabelsPadding == null) ? (int) (tickLabelsFontSize * DEFAULT_LABEL_PADDING_FACTOR) : tickLabelsPadding;
    }

    public void setTickLabelsPadding(int tickLabelsPadding) {
        this.tickLabelsPadding = tickLabelsPadding;
    }

    public boolean isTickLabelsVisible() {
        return isTickLabelsVisible;
    }

    public void setTickLabelsVisible(boolean tickLabelsVisible) {
        isTickLabelsVisible = tickLabelsVisible;
    }

    public int[] getTickLabelAutoRotation() {
        return tickLabelAutoRotation;
    }

    public void setTickLabelAutoRotation(int[] tickLabelAutoRotation) {
        this.tickLabelAutoRotation = tickLabelAutoRotation;
    }

    public int getTicksWidth() {
        return ticksWidth;
    }

    public void setTicksWidth(int ticksWidth) {
        this.ticksWidth = ticksWidth;
    }

    public int getTickSize() {
        return tickSize;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    public int getTickPixelInterval() {
        return tickPixelInterval;
    }

    public void setTickPixelInterval(Integer tickPixelInterval) {
        this.tickPixelInterval = tickPixelInterval;
    }

    public Double getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(Double tickInterval) {
        this.tickInterval = tickInterval;
    }
}
