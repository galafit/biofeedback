package main.chart;

import main.chart.axis.Axis;
import main.chart.axis.AxisData;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph {

    public LineGraph(List<ChartItem> chartItemList) {
        this.chartItemList = chartItemList;
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

}
