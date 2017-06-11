package main.chart.axis;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

import static java.lang.Math.pow;

/**
 * Created by hdablin on 08.04.17.
 */
public class LinearAxisData extends AxisData {
    @Override
    public TickProvider getTicksProvider(Rectangle area) {
        return new LinearTickProvider(getMin(),getMax(),pointsPerUnit(area));
    }

    @Override
    public double pointsPerUnit(Rectangle area) {
        double min = getMin();
        double max = getMax();
        if (isHorizontal) {
            return (area.getWidth()) / (max - min);
        }
        return (area.getHeight())/ (max - min);
    }

    @Override
    public int valueToPoint(double value, Rectangle area) {
        double axisMin = getMin();
        double axisMax = getMax();


        if (axisMin == axisMax){
            if (isHorizontal) {
                return (int)(area.getX() + area.getWidth() / 2);
            }
            return (int)(area.getY() + area.getHeight() / 2);
        }

        if (isHorizontal) {
            return (int)(area.getX() + (value - axisMin) * pointsPerUnit(area));
        }
        return (int)(area.getY() + area.height - (value - axisMin) * pointsPerUnit(area));
    }
}

class LinearTickProvider implements TickProvider{
    Double tickInterval = Double.NaN;
    double min;
    double max;
    Double pointsPerUnit = Double.NaN;
    NumberFormat numberFormat;

    public LinearTickProvider(double min, double max, double pointsPerUnit) {
        this.min = min;
        this.max = max;
        this.pointsPerUnit = pointsPerUnit;
    }

    private double getClosestTickPrev(double value, double tickInterval){
      return Math.floor(value / tickInterval) * tickInterval;
    }

    private double getClosestTickNext(double value, double tickInterval){
        return Math.ceil(value / tickInterval) * tickInterval;
    }


    private NumberFormat getTickLabelFormat(int power) {
        DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
        DecimalFormat dfNeg3 = new DecimalFormat("0.000");
        DecimalFormat dfNeg2 = new DecimalFormat("0.00");
        DecimalFormat dfNeg1 = new DecimalFormat("0.0");
        DecimalFormat df0 = new DecimalFormat("#,##0");
        DecimalFormat df = new DecimalFormat("#.######E0");

        if (power == -4) {
            return dfNeg4;
        }
        if (power == -3) {
            return dfNeg3;
        }
        if (power == -2) {
            return dfNeg2;
        }
        if (power == -1) {
            return dfNeg1;
        }
        if (power >= 0 && power <= 6) {
            return df0;
        }
        return df;
    }

    @Override
    public Double getTickInterval() {
        return tickInterval;
    }

    @Override
    public Double getTickPixelInterval() {
        if (tickInterval.isNaN()){
            return Double.NaN;
        }
        return tickInterval * pointsPerUnit;
    }

    @Override
    public void setTickInterval(double tickInterval) {
        this.tickInterval = tickInterval;

    }

    @Override
    public void setTickPixelInterval(double tickPixelInterval) {

        tickInterval = tickPixelInterval / pointsPerUnit;
        // firstDigit is in {2,5,10};
        NormalizedNumber tick = new NormalizedNumber(tickInterval);

        int power = tick.getPower();
        int firstDigit = (int)(tick.getDigits());
        switch (firstDigit){
            case 3 : firstDigit = 2;
                break;
            case 4 : firstDigit = 5;
                break;
            case 6 : firstDigit = 5;
                break;
            case 7 : firstDigit = 5;
                break;
            case 8 : firstDigit = 1;
                power++;
                break;
            case 9 : firstDigit = 1;
                power++;
                break;
        }

        tickInterval = (firstDigit * pow(10,power));
        numberFormat = getTickLabelFormat(power);

    }

    @Override
    public void setMinTickPixelInterval(double minTickPixelInterval) {
        tickInterval = minTickPixelInterval / pointsPerUnit;
        // firstDigit is in {2,5,10};
        NormalizedNumber tick = new NormalizedNumber(tickInterval);

        int power = tick.getPower();
        int firstDigit = (int)(tick.getDigits() + 1);
        switch (firstDigit){
            case 3 : firstDigit = 5;
                break;
            case 4 : firstDigit = 5;
                break;
            case 6 : firstDigit = 1;
                power++;
                break;
            case 7 : firstDigit = 1;
                power++;
                break;
            case 8 : firstDigit = 1;
                power++;
                break;
            case 9 : firstDigit = 1;
                power++;
                break;
        }

        tickInterval = (firstDigit * pow(10,power));
        numberFormat = getTickLabelFormat(power);

    }

    public double getRoundMin(){
        return (min == max) ? min : getClosestTickPrev(min, tickInterval);
    }

    public double getRoundMax(){
        return (min == max) ? max : getClosestTickNext(max, tickInterval);
    }

    @Override
    public List<Tick> getTicks() {
        double roundMin = getRoundMin();
        double roundMax = getRoundMax();
        List<Tick> ticks = new ArrayList<Tick>();
        double value = roundMin;
        int tickAmount = (roundMax == roundMin || tickInterval == 0) ? 1 : (int) Math.round((roundMax - roundMin) / tickInterval) + 1 ;
        for(int i = 1; i <= tickAmount; i++) {
            String label =  numberFormat.format(value);
            ticks.add(new Tick(value, label));
            value = value + tickInterval;
        }
        return ticks;
    }
}