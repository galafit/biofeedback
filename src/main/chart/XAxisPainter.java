package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class XAxisPainter {
    private LinearAxisX xAxis;
    private LinearAxisY yAxis;

    public XAxisPainter(LinearAxisX xAxis, LinearAxisY yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawLine(xAxis.getPointMin(),yAxis.getPointMin(),xAxis.getPointMax(),yAxis.getPointMin());

        for (int i = 0; i < 10; i++) {
            double value = xAxis.getMin() + i * (xAxis.getMax() - xAxis.getMin())/10;
            g.drawString(String.valueOf(value), xAxis.valueToPoint(value),yAxis.getPointMin());
        }
    }

}
