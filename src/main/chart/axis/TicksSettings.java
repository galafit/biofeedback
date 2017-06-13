package main.chart.axis;

/**
 * Created by galafit on 13/6/17.
 */
public class TicksSettings {
    private int tickLabelsFontSize = 11;
    private int tickLabelsPadding = 5;
    private boolean isTickLabelsVisible = true;
    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
    private int[] tickLabelAutoRotation = {-45, 90}; // at the moment not used

    private int ticksWidth = 1;
    private int tickSize = 5;
    private Integer tickPixelInterval = 100;
    private Double tickInterval = null; // in axis unit (tickUnit)

    public boolean isTicksVisible() {
        return (ticksWidth > 0) ? true : false;
    }

    public int getTickLabelsFontSize() {
        return tickLabelsFontSize;
    }

    public void setTickLabelsFontSize(int tickLabelsFontSize) {
        this.tickLabelsFontSize = tickLabelsFontSize;
    }

    public int getTickLabelsPadding() {
        return tickLabelsPadding;
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
