package main.chart;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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


    @Override
    public Tick[] getTicks(Rectangle area) {

        double step = (max - min)/10;

        int[] baseSteps = {2,5,10};

        int power = (int) Math.log10(step);
        if(Math.log10(step) < 0) {
            power = power - 1;
        }

        int firstStepDigit = (int) (step / Math.pow(10,power));

        System.out.println("step: " + step);
        System.out.println("power: " + power);
        System.out.println("firstStepDigit: " + firstStepDigit);

        int difference = baseSteps[baseSteps.length-1];
        for (int i = 0; i < baseSteps.length ; i++) {
            int newDifference = firstStepDigit - baseSteps[i];
            if (Math.abs(newDifference) < Math.abs(difference)){
                difference = newDifference;
            }
            //System.out.println("difference: " + "i=" + i  + difference);
        }

        firstStepDigit = firstStepDigit - difference;
        //     if (firstStepDigit == baseSteps[baseSteps.length-1]){
        // power++;
        //   }


        step = (firstStepDigit * Math.pow(10,power));


        Tick[] ticks = new Tick[10];

        double roundMin = ((int)(min / step))*step;
        System.out.println("roundMin: " + roundMin);

        for (int i = 0; i < 10; i++) {

            //TODO: change 10
            String value =  getTickLabelFormat(power).format(i*firstStepDigit);
            System.out.println("value: " + value);
           // ticks[i] = new Tick(valueToPoint(value, area), String.valueOf(value));

        }
        return ticks;
    }
}
