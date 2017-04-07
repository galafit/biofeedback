package main.chart;

import org.w3c.dom.css.Rect;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class XAxisPainter {
    private LinearAxisX xAxis;
    private LinearAxisY yAxis;
    private int MinTickInterval;

    public XAxisPainter(LinearAxisX xAxis, LinearAxisY yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void draw(Graphics g, Rectangle area){
        g.setColor(Color.YELLOW);
        g.drawLine(area.x,area.y,area.width+area.x,area.y);

        double step = (xAxis.getMax() - xAxis.getMin())/10;


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
       if (firstStepDigit == baseSteps[baseSteps.length-1]){
          // power++;
       }

       double newStep = (firstStepDigit * Math.pow(10,power));

       System.out.println("newStep: " + newStep);

        for (int i = 0; i < 10; i++) {
            double value = xAxis.getMin() + i * (xAxis.getMax() - xAxis.getMin())/10;

            g.drawString(String.valueOf(value), xAxis.valueToPoint(value,area),area.y);
        }
    }

}
