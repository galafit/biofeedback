package main.chart.axis;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

/**
 * Created by galafit on 17/6/17.
 */
class LinearTickProvider {
    Double tickInterval;
    double min;
    double max;
    Double pointsPerUnit;
    NumberFormat numberFormat;
    int tickAmount;

    public LinearTickProvider(double min, double max, double pointsPerUnit) {
        this.min = min;
        this.max = max;
        this.pointsPerUnit = pointsPerUnit;
    }

    private double getClosestTickPrev(double value, double tickInterval) {
        return Math.floor(value / tickInterval) * tickInterval;
    }

    private double getClosestTickNext(double value, double tickInterval) {
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

    public void setTickInterval(double tickInterval) {
        this.tickInterval = tickInterval;

    }

    public void setTickPixelInterval(double tickPixelInterval) {
        tickInterval = tickPixelInterval / pointsPerUnit;
        // firstDigit is in {2,5,10};
        NormalizedNumber tick = new NormalizedNumber(tickInterval);

        int power = tick.getPower();
        int firstDigit = (int) (tick.getDigits());
        switch (firstDigit) {
            case 3:
                firstDigit = 2;
                break;
            case 4:
                firstDigit = 5;
                break;
            case 6:
                firstDigit = 5;
                break;
            case 7:
                firstDigit = 5;
                break;
            case 8:
                firstDigit = 1;
                power++;
                break;
            case 9:
                firstDigit = 1;
                power++;
                break;
        }
        tickInterval = (firstDigit * pow(10, power));
        numberFormat = getTickLabelFormat(power);

    }

    public void setMinTickPixelInterval_(double minTickPixelInterval) {
        tickInterval = minTickPixelInterval / pointsPerUnit + 1;
        // firstDigit is in {2,5,10};
        NormalizedNumber tick = new NormalizedNumber(tickInterval);

        int power = tick.getPower();
        int firstDigit = (int) (tick.getDigits() + 1);
        switch (firstDigit) {
            case 3:
                firstDigit = 5;
                break;
            case 4:
                firstDigit = 5;
                break;
            case 6:
                firstDigit = 1;
                power++;
                break;
            case 7:
                firstDigit = 1;
                power++;
                break;
            case 8:
                firstDigit = 1;
                power++;
                break;
            case 9:
                firstDigit = 1;
                power++;
                break;
        }

        tickInterval = (firstDigit * pow(10, power));
        numberFormat = getTickLabelFormat(power);
    }

    public void setMinTickPixelInterval(double minTickPixelInterval) {
        tickInterval = minTickPixelInterval / pointsPerUnit + 1;
        // firstDigit is in {2,5,10};
        NormalizedNumber tick = new NormalizedNumber(tickInterval);

        int power = tick.getPower();
        int firstDigit = (int) Math.round(tick.getDigits());
    /*    switch (firstDigit) {
            case 3:
                firstDigit = 4;
                break;
            case 7:
                firstDigit = 8;
                break;
            case 9:
                firstDigit = 1;
                power++;
                break;
        }*/

        tickInterval = (firstDigit * pow(10, power));
        numberFormat = getTickLabelFormat(power);
    }


    public void setTicksAmount(int tickAmount) {
        this.tickAmount = tickAmount;
        double tickPixelInterval = (max - min) * pointsPerUnit / (tickAmount - 1);
        setMinTickPixelInterval(tickPixelInterval);
    }


    public double getRoundMin() {
        return (min == max) ? min : getClosestTickPrev(min, tickInterval);
    }

    public double getRoundMax() {
        return (min == max) ? max : getClosestTickNext(max, tickInterval);
    }

    public List<Tick> getTicks() {
        double roundMin = getRoundMin();
        double roundMax = getRoundMax();
        List<Tick> ticks = new ArrayList<Tick>();
        double value = roundMin;
        int ticksAmount = (roundMax == roundMin || tickInterval == 0) ? 1 : (int) Math.round((roundMax - roundMin) / tickInterval) + 1;
        ticksAmount = (ticksAmount == 1) ? 1 : Math.max(ticksAmount, this.tickAmount);
        for (int i = 1; i <= ticksAmount; i++) {
            String label = numberFormat.format(value);
            ticks.add(new Tick(value, label));
            value = value + tickInterval;
        }
        return ticks;
    }
}
