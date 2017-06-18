package main.chart.axis;

import com.sun.istack.internal.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

/**
 * Created by galafit on 17/6/17.
 */
class LinearTickProvider {
    double tickInterval;
    double min;
    double max;
    double pointsPerUnit;
    NumberFormat numberFormat;
    int ticksAmount;
    String units;

    public LinearTickProvider(double min, double max, double pointsPerUnit, @Nullable  String units) {
        this.min = min;
        this.max = max;
        this.pointsPerUnit = pointsPerUnit;
        this.units = units;
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
        NormalizedNumber normalizedInterval = new NormalizedNumber(tickInterval);
        int power = normalizedInterval.getPower();
        numberFormat = getTickLabelFormat(power);
    }

    public void setTickPixelInterval(int tickPixelInterval) {
        tickInterval = tickPixelInterval / pointsPerUnit;
        // firstDigit is in {1,2,5,10};
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

    public void setMinTickPixelInterval(double minTickPixelInterval) {
        tickInterval = minTickPixelInterval / pointsPerUnit;
        NormalizedNumber normalizedInterval = new NormalizedNumber(tickInterval);
        int power = normalizedInterval.getPower();
        int first2Digits = (int) (normalizedInterval.getDigits() * 10) + 1;
        power--;
        int[] roundValues = {10, 12, 15, 20, 25, 30, 40, 50, 60, 80, 100};
        //int[] roundValues = {10, 12, 15, 20,  25, 30, 40, 50, 60, 80, 100};
        // find the closest roundValue that is > first2Digits
        for (int roundValue : roundValues) {
            if(roundValue >= first2Digits) {
                first2Digits = roundValue;
                break;
            }
        }
        int formatPower = power;
        if(first2Digits == 100) {
            formatPower += 2;
        }
        int rest = first2Digits % 10;
        if(rest == 0 ) {
            formatPower++;
        }
        numberFormat = getTickLabelFormat(formatPower);
        tickInterval = (first2Digits * pow(10, power));
    }




    public void setTicksAmount(int ticksAmount) {
        if(max == min) {
            setTickInterval(max);
        }
        else {
            this.ticksAmount = ticksAmount;
            double tickPixelInterval = (max - min) * pointsPerUnit / (ticksAmount - 1);
            setMinTickPixelInterval(tickPixelInterval);
        }
   }



    private double getRoundMin() {
        return (min == max) ? min : getClosestTickPrev(min, tickInterval);
    }

    private double getRoundMax() {
        return (min == max) ? max : getClosestTickNext(max, tickInterval);
    }

    private int calculateTicksAmount() {
        return  (min == max || tickInterval == 0) ? 1 : (int) Math.round((getRoundMax() - getRoundMin()) / tickInterval) + 1;

    }

    public List<Tick> getTicks() {

        int ticksAmount = calculateTicksAmount();
        if(this.ticksAmount > 0 && ticksAmount > this.ticksAmount) {
            setTicksAmount(this.ticksAmount - 1); // this method set this.thickAmount = this.ticksAmount - 1;
            this.ticksAmount++; // to compensate the previous subtraction we +1;
        }

        List<Tick> ticks = new ArrayList<Tick>();
        double value = getRoundMin();
        ticksAmount = calculateTicksAmount();
        ticksAmount = (ticksAmount == 1) ? 1 : Math.max(ticksAmount, this.ticksAmount);
        for (int i = 1; i <= ticksAmount; i++) {
            String label = numberFormat.format(value);
            if (units != null){
                label = label + " " + units;
            }
            ticks.add(new Tick(value, label));
            value = value + tickInterval;
        }
        return ticks;
    }

    public void setMinTickPixelInterval_(double minTickPixelInterval) {
        tickInterval = minTickPixelInterval / pointsPerUnit;
        NormalizedNumber normalizedInterval = new NormalizedNumber(tickInterval);
        int power = normalizedInterval.getPower();
        int first2Digits = (int) (normalizedInterval.getDigits() * 10) + 1;
        power--;
        int[] roundValues = {10, 12, 15, 20, 25, 30, 40, 50, 60, 80, 100};
        //int[] roundValues = {10, 12, 15, 20,  25, 30, 40, 50, 60, 80, 100};
        // find the closest roundValue that is > first2Digits
        for (int roundValue : roundValues) {
            if(roundValue >= first2Digits) {
                first2Digits = roundValue;
                break;
            }
        }
        int formatPower = power;
        if(first2Digits == 100) {
            formatPower += 2;
        }
        int rest = first2Digits % 10;
        if(rest == 0 ) {
            formatPower++;
        }
        numberFormat = getTickLabelFormat(formatPower);
        tickInterval = (first2Digits * pow(10, power));
    }

}
