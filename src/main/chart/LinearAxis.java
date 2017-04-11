package main.chart;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static java.lang.Math.pow;

/**
 * Created by hdablin on 08.04.17.
 */
public abstract class LinearAxis extends Axis {

    private DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
    private DecimalFormat dfNeg3 = new DecimalFormat("0.000");
    private DecimalFormat dfNeg2 = new DecimalFormat("0.00");
    private DecimalFormat dfNeg1 = new DecimalFormat("0.0");
    private DecimalFormat df0 = new DecimalFormat("#,##0");
    private DecimalFormat df = new DecimalFormat("#.######E0");

    public LinearAxis(double min, double max) {
        super(min, max);
    }



    private NumberFormat getTickLabelFormat(int power) {

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




    private double getClosestTickPrev(double value, double tickInterval){
        return ((int)(value / tickInterval))*tickInterval;
    }

    private double getClosestTickNext(double value, double tickInterval){
        return getClosestTickPrev(value,tickInterval) + tickInterval;
    }

    @Override
    public Tick[] getTicks(Rectangle area) {
        double tickInterval = (max - min)/10;
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
        NumberFormat numberFormat = getTickLabelFormat(power);


        double roundMin = getClosestTickNext(min, tickInterval);
        double roundMax = getClosestTickPrev(max, tickInterval);
        System.out.println("roundMax: " + roundMax);


        int ticksAmount = (int)Math.round((roundMax - roundMin) / tickInterval) + 1;
        System.out.println("ticksAmount: " + ticksAmount);
        Tick[] ticks = new Tick[ticksAmount];
        double value = 0;

        for (int i = 0; i < ticksAmount; i++) {

            value = roundMin + tickInterval * i;
            String lable =  numberFormat.format(value);
            ticks[i] = new Tick(value, lable);

        }
        return ticks;
    }
}
