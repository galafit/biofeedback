package main.chart;

import main.graph.*;

import java.awt.*;
import java.util.*;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph {

    public AreaGraph(java.util.List<ChartItem> chartItemList) {
        this.chartItemList = chartItemList;
    }

    @Override
    public void draw(Graphics g, Rectangle area) {
        g.setColor(Color.YELLOW);

        for (int i = 0; i < chartItemList.size(); i++) {

            if (i > 0) {
                int pointX = xAxis.valueToPoint(chartItemList.get(i).getX(), area);
                int pointY = yAxis.valueToPoint(chartItemList.get(i).getY(), area);

                int previousPointX = xAxis.valueToPoint(chartItemList.get(i - 1).getX(), area);
                int previousPointY = yAxis.valueToPoint(chartItemList.get(i - 1).getY(), area);

                g.drawLine(previousPointX, previousPointY, pointX, pointY);

                Polygon p = new Polygon();

                p.addPoint(previousPointX,previousPointY);
                p.addPoint(pointX,pointY);
                p.addPoint(pointX,yAxis.valueToPoint(0,area));
                p.addPoint(previousPointX,yAxis.valueToPoint(0,area));



                g.fillPolygon(p);
            }



        }
    }
}
