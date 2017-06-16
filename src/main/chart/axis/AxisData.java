package main.chart.axis;


import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    private String name = "Test name 125679000999";
    private String units = "kg";

    private Double min = null;
    private Double max = null;
    private final double DEFAULT_MIN = 0;
    private final double DEFAULT_MAX = 1;
    private boolean isHorizontal;
    private boolean isAutoScale = true;
    private boolean isInverted = false;
    private boolean isOpposite = false;

    private double lowerPadding = 0.02;
    private double upperPadding = 0.02;
    private boolean isEndOnTick = true;

    private AxisViewSettings axisViewSettings = new AxisViewSettings();
    private TicksSettings ticksSettings = new TicksSettings();
    private GridSettings gridSettings = new GridSettings();

    public abstract void setTicksPoints(List<Integer> points, Rectangle area);

    public AxisViewSettings getAxisViewSettings() {
        return axisViewSettings;
    }

    public TicksSettings getTicksSettings() {
        return ticksSettings;
    }

    public GridSettings getGridSettings() {
        return gridSettings;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public boolean isInverted() {
        return isInverted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isEndOnTick() {
        return isEndOnTick;
    }

    public void setEndOnTick(boolean endOnTick) {
        isEndOnTick = endOnTick;
    }

    public void resetRange() {
        min = null;
        max = null;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    abstract public Double pointsPerUnit(Rectangle area);

    abstract public int valueToPoint(double value, Rectangle area);

    abstract public double pointsToValue(int point, Rectangle area);

    public Double getMin() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMin = (!isAutoScale()) ? resultantMin : resultantMin - lowerPadding * (resultantMax - resultantMin);

        return resultantMin;

    }

    public Double getMax() {
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMax = (!isAutoScale()) ? resultantMax : resultantMax + upperPadding * (resultantMax - resultantMin);
        return resultantMax;

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
        if (newMin > newMax){
            String errorMessage = "Error during setRange(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage,newMin,newMax);
            throw new IllegalArgumentException(formattedError);
        }
        if (!isAutoScale) {
            min = newMin;
            max = newMax;
        } else {
            min = (min == null) ? newMin : Math.min(newMin, min);
            max = (max == null) ? newMax : Math.max(newMax, max);
        }
    }

    abstract public TickProvider getTicksProvider(Rectangle area);

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }
}
