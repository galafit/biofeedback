package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph {

    public AreaGraph(DataList dataItemList) {
        this.dataItemList = dataItemList;
    }

    @Override
    public void draw(Graphics g, Rectangle area) {
        g.setColor(color);

        for (int i = 0; i < dataItemList.size(); i++) {

            if (i > 0) {
                int pointX = xAxis.valueToPoint(dataItemList.get(i).getX(), area);
                int pointY = yAxis.valueToPoint(dataItemList.get(i).getY(), area);

                int previousPointX = xAxis.valueToPoint(dataItemList.get(i - 1).getX(), area);
                int previousPointY = yAxis.valueToPoint(dataItemList.get(i - 1).getY(), area);

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
