package main.chart.axis;

import com.sun.istack.internal.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
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
    DecimalFormat numberFormat = new DecimalFormat();
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


    private DecimalFormat getTickLabelFormat(int power) {
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
        if(max == min) {
            return;
        }
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
        setTickInterval(firstDigit * pow(10, power));

    }

    /**
     * Find and set round tickInterval >= (max - min) / (givenTicksAmount - 1).
     * <p>
     * So normally resultantTicksAmount <= givenTicksAmount.
     * But since to calculate real ticks amount we will use
     * roundMin < min < max < roundMax sometimes it is possible that:
     * resultant ticks amount = givenTicksAmount + 1
     *
     * @param givenTicksAmount - desirable amount of ticks
     */
    private void setRoundTickInterval(int givenTicksAmount, int[] roundValues)  {
        if(givenTicksAmount <= 1) {
            String errMsg = MessageFormat.format("Invalid ticks amount: {0}. Expected >= 2", givenTicksAmount);
            throw new IllegalArgumentException(errMsg);
        }
        tickInterval = (max - min)  / (givenTicksAmount - 1);
        NormalizedNumber normalizedInterval = new NormalizedNumber(tickInterval);
        int power = normalizedInterval.getPower();
        int first2Digits = (int) (normalizedInterval.getDigits() * 10) + 1;
        power--;
        // find the closest roundValue that is > first2Digits
        for (int roundValue : roundValues) {
            if(roundValue >= first2Digits) {
                first2Digits = roundValue;
                break;
            }
        }

        if(first2Digits == 100) {
            first2Digits = 10;
            power++;
        }
        int formatPower = power;
        int rest = first2Digits % 10;
        if(rest == 0 ) {
            formatPower++;
        }
        tickInterval = (first2Digits * pow(10, power));
        numberFormat = getTickLabelFormat(formatPower);
    }



    /**
     * resultant ticks amount <= given ticks amount!
     * @param givenTicksAmount
     */
    public void setTicksAmount(int givenTicksAmount) {
        if(max == min) {
            return;
        }
        ticksAmount = givenTicksAmount;
        int[] roundValues = {10, 12, 15, 20, 25, 30, 40, 50, 60, 80, 100};
        /*
         * Normally resultantTicksAmount <= givenTicksAmount.
         * But since to calculate real ticks amount we will use
         * roundMin < min < max < roundMax sometimes it is possible that:
         * resultantTicksAmount = (givenTicksAmount + 1)
         * And in this case we need to call the same method with
         * (givenTicksAmount - 1)
         */
        setRoundTickInterval(givenTicksAmount, roundValues);
        int resultantTickAmount = calculateTicksAmount();
        // if resultantTicksAmount = given ticks amount + 1 we do next iteration
        if(resultantTickAmount > givenTicksAmount && givenTicksAmount > 2) {
            setRoundTickInterval(givenTicksAmount - 1, roundValues);
        }
   }

    public void setMinTickPixelInterval(double minTickPixelInterval) {
        if(max == min) {
            return;
        }
        int ticksAmount = (int)((max - min) * pointsPerUnit / minTickPixelInterval) + 1;
        if(ticksAmount < 2) {
            ticksAmount = 2;
        }
        int[] roundValues = {10, 20,  30, 40, 50, 60, 80, 100};
         /*
         * Normally resultantTicksAmount <= givenTicksAmount.
         * But since to calculate real ticks amount we will use
         * roundMin < min < max < roundMax sometimes it is possible that:
         * resultantTicksAmount = (givenTicksAmount + 1).
         * In this case resultantTickPixelInterval can be < minTickPixelInterval.
         * And to compensate that we need to call the same method with
         * (ticksAmount - 1)
         */
        setRoundTickInterval(ticksAmount, roundValues);
        int resultantTickAmount = calculateTicksAmount();
        if(resultantTickAmount > ticksAmount && ticksAmount > 2) {
            setRoundTickInterval(ticksAmount - 1, roundValues);
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
        List<Tick> ticks = new ArrayList<Tick>();
        double value = getRoundMin();
        int ticksAmount = calculateTicksAmount();
        ticksAmount = (ticksAmount == 1) ? 1 : Math.max(ticksAmount, this.ticksAmount);
        if (units != null){
            numberFormat = new DecimalFormat(numberFormat.toPattern() + " "+units);
        }
        for (int i = 1; i <= ticksAmount; i++) {
            String label = numberFormat.format(value);
            ticks.add(new Tick(value, label));
            value = value + tickInterval;
        }
        return ticks;
    }
}
