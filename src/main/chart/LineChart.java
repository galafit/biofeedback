package main.chart;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineChart {
    private List<ChartItem> chartItemList;
    private Axis xAxis;
    private Axis yAxis;

    public LineChart(List<ChartItem> chartItemList, Axis xAxis, Axis yAxis) {
        this.chartItemList = chartItemList;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void draw(Graphics g, Rectangle area) {

        g.setColor(Color.BLUE);

        for (int i = 0; i < chartItemList.size(); i++) {

            if (i > 0) {
                int pointX = xAxis.valueToPoint(chartItemList.get(i).getX(), area);
                int pointY = yAxis.valueToPoint(chartItemList.get(i).getY(), area);

                int previousPointX = xAxis.valueToPoint(chartItemList.get(i - 1).getX(), area);
                int previousPointY = yAxis.valueToPoint(chartItemList.get(i - 1).getY(), area);

                g.drawLine(previousPointX, previousPointY, pointX, pointY);
            }
        }
    }

    public Axis getxAxis() {
        return xAxis;
    }

    public Axis getyAxis() {
        return yAxis;
    }

    public void setxAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }
}
