package main.chart.axis;

import com.sun.istack.internal.Nullable;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by galafit on 17/6/17.
 */
class TickProviderTest {
    double ticksInterval;
    double min;
    double max;
    double pointsPerUnit;
    DecimalFormat numberFormat = new DecimalFormat();
    int ticksAmount;
    String units;


    public TickProviderTest(double min, double max, double pointsPerUnit, @Nullable  String units) {
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


    public void setTicksInterval(double ticksInterval) {
        ScientificNumber scientificNumber = new ScientificNumber(ticksInterval);
        int numberOfFirstDigits = 7;
        int firstDigits = (int) (scientificNumber.getDigits() * Math.pow(10, numberOfFirstDigits)) + 1;
        int power = scientificNumber.getPower() - numberOfFirstDigits;

        NormalizedDouble roundInterval = new NormalizedDouble(firstDigits, power);
        setTicksIntervalAndFormat(ticksInterval, roundInterval.getPower());
    }

    public void setTickPixelInterval(int tickPixelInterval) {
        if(max == min) {
            return;
        }
        ticksInterval = tickPixelInterval / pointsPerUnit;
        // firstDigit is in {1,2,5,10};
        ScientificNumber tick = new ScientificNumber(ticksInterval);

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
        setTicksIntervalAndFormat(firstDigit * Math.pow(10, power), power);

    }

    /**
     * Calculate and set ticksInterval such that
     * resultant ticks amount <= given ticks amount!
     * @param givenTicksAmount
     */
    public void setTicksAmount(int givenTicksAmount) {
        if(max == min) {
            return;
        }
        ticksAmount = givenTicksAmount;
        int[] roundValues = {10, 12, 15, 20, 25, 30, 40, 50, 60, 80, 100};
        setRoundTickInterval(givenTicksAmount, roundValues);
    }

    /**
     * Calculate and set ticksInterval such that
     * resultant ticksPixelInterval >= minTickPixelInterval
     * @param minTickPixelInterval given min tick interval in pixels
     */
    public void setMinTickPixelInterval(double minTickPixelInterval) {
        if(max == min) {
            return;
        }
        int ticksAmount = (int)((max - min) * pointsPerUnit / minTickPixelInterval) + 1;
        if(ticksAmount < 2) {
            ticksAmount = 2;
        }
        int[] roundValues = {10, 20,  30, 40, 50, 60, 80, 100};
        setRoundTickInterval(ticksAmount, roundValues);
    }


    private void setTicksIntervalAndFormat(double ticksInterval, int formatPower) {
        if(calculateTicksAmount(ticksInterval) == 2) { // special case handled apart
            this.ticksInterval = max - min;
            numberFormat = getTickLabelFormat(getPixelInterval().getPower());
        }
        else{
            this.ticksInterval = ticksInterval;
            numberFormat = getTickLabelFormat(formatPower);
        }
    }

    /**
     * Find closest roundInterval >= given interval
     * @param interval given interval
     * @param roundValues array of possible round values. Each 10 <= value <= 100
     * @return closest roundInterval >= given interval
     */
    private NormalizedDouble roundIntervalUp(double interval, int[] roundValues)  {
        ScientificNumber scientificNumber = new ScientificNumber(interval);
        int power = scientificNumber.getPower();
        int first2Digits = (int) (scientificNumber.getDigits() * 10) + 1;
        power--;
        // find the closest roundValue that is > first2Digits
        for (int roundValue : roundValues) {
            if(roundValue >= first2Digits) {
                first2Digits = roundValue;
                break;
            }
        }

        return new NormalizedDouble(first2Digits, power);
    }

    /**
     * Find closest roundInterval <= given interval
     * @param interval given interval
     * @param roundValues array of possible round values. Each 10 <= value <= 100
     * @return closest roundInterval <= given interval
     */
    private NormalizedDouble roundIntervalDown(double interval, int[] roundValues)  {
        ScientificNumber scientificNumber = new ScientificNumber(interval);
        int power = scientificNumber.getPower();
        int first2Digits = (int) (scientificNumber.getDigits() * 10) + 1;
        power--;
        // find the closest roundValue that is < first2Digits
        for (int i = roundValues.length - 1; i >= 0; i--) {
            if(roundValues[i] <= first2Digits) {
                first2Digits = roundValues[i];
                break;
            }
        }
        return new NormalizedDouble(first2Digits, power);
    }


    /**
     * Find and set round ticksInterval such that:
     * resultantTicksAmount <= givenTicksAmount
     *
     * @param givenTicksAmount - desirable amount of ticks
     */
    private void setRoundTickInterval(int givenTicksAmount, int[] roundValues)  {
        if(givenTicksAmount <= 1) {
            String errMsg = MessageFormat.format("Invalid ticks amount: {0}. Expected >= 2", givenTicksAmount);
            throw new IllegalArgumentException(errMsg);
        }
        Double interval = (max - min)  / (givenTicksAmount - 1);
        NormalizedDouble normalizedDouble = roundIntervalUp(interval, roundValues);
        int intervalPower = normalizedDouble.getPower();
        double intervalValue = normalizedDouble.getDouble();

        int resultantTicksAmount = calculateTicksAmount(intervalValue);
        /*
         * Due to rounding (roundMin < min < max < roundMax)
         * sometimes it is possible that the resultantTicksAmount may be
         * greater than the givenTicksAmount:
         * resultantTicksAmount = givenTicksAmount + 1.
         * In this case we repeat the same procedure with (givenTicksAmount -1)
         */
        if(resultantTicksAmount > givenTicksAmount && givenTicksAmount > 2) {
            givenTicksAmount--;
            interval = (max - min)  / (givenTicksAmount - 1);
            normalizedDouble = roundIntervalUp(interval, roundValues);
            intervalPower = normalizedDouble.getPower();
            intervalValue = normalizedDouble.getDouble();

        }
        setTicksIntervalAndFormat(intervalValue, intervalPower);
    }


    private NormalizedDouble getPixelInterval() {
        int[] roundValues = {10, 20,  30, 40, 50, 60, 80, 90};
        double pixelInterval = 1;
        double interval = pixelInterval / pointsPerUnit;
        return roundIntervalDown(interval, roundValues);
    }

    private double getRoundMin(double roundTicksInterval) {
        if(min == max) {
            return min;
        }
        double tickNext = getClosestTickNext(min, roundTicksInterval);
        double tickPrev  = getClosestTickPrev(min, roundTicksInterval);
        double pixelInterval = getPixelInterval().getDouble();

        if(min - tickPrev < pixelInterval) {
            return tickPrev;
        }
        if(tickNext - min < pixelInterval) {
            return tickNext;
        }
        return tickPrev;
    }

    private double getRoundMax(double roundTicksInterval) {
        if(min == max) {
            return max;
        }
        double tickNext = getClosestTickNext(max, roundTicksInterval);
        double tickPrev = getClosestTickPrev(max, roundTicksInterval);
        double pixelInterval = getPixelInterval().getDouble();
        if(tickNext - max < pixelInterval) {
            return tickNext;
        }
        if(max - tickPrev < pixelInterval) {
            return tickPrev;
        }
        return tickNext;
    }


    private int calculateTicksAmount(double ticksInterval) {
        if (min == max) {
            return 1;
        }
        if(Math.round((max - min) / ticksInterval) <= 1) {
            return 2;
        }
        return (int) Math.round((getRoundMax(ticksInterval) - getRoundMin(ticksInterval)) / ticksInterval) + 1;
    }

    public List<Tick> getTicks() {
        List<Tick> ticks = new ArrayList<Tick>();
        int ticksAmount = calculateTicksAmount(ticksInterval);
        ticksAmount = (ticksAmount == 1) ? 1 : Math.max(ticksAmount, this.ticksAmount);
        double roundMin = getRoundMin(ticksInterval);
        double roundMax = getRoundMax(ticksInterval);
        double pixelInterval = getPixelInterval().getDouble();
        if(ticksAmount == 2) {
            roundMin = getRoundMin(pixelInterval);
            roundMax = getRoundMax(pixelInterval);
        }
        double value = roundMin;
       /* if (units != null){
            numberFormat = new DecimalFormat(numberFormat.toPattern() + " "+units);
        }*/
        for (int i = 1; i <= ticksAmount; i++) {
            String label = numberFormat.format(value);
            try {
                if(numberFormat.parse(label).doubleValue() == 0) { // cut minus
                    label = numberFormat.format(0);
                }
            } catch (ParseException e) {
                // do nothing !
            }
            if (units != null){
                label = label + " "+units;
            }
            if(i == 1) {
                if(Math.abs(min - roundMin) < pixelInterval) {
                    ticks.add(new Tick(min, label));
                }
            } else if (i == ticksAmount) {
                if (Math.abs(roundMax - max) < pixelInterval) {
                    ticks.add(new Tick(max, label));
                }
            } else {
                ticks.add(new Tick(value, label));
            }
            value = value + ticksInterval;
        }
        return ticks;
    }

}


