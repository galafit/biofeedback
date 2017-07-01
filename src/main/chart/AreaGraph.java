package main.chart;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph {

    public AreaGraph(DataList dataItemList) {
        this.dataItemList = dataItemList;
    }

    @Override
    public void draw(Graphics2D g, Rectangle area) {
        g.setColor(color);

        for (int i = 0; i < dataItemList.size(); i++) {

            if (i > 0) {
                double pointX = xAxis.valueToPoint(dataItemList.get(i).getX(), area);
                double pointY = yAxis.valueToPoint(dataItemList.get(i).getY(), area);

                double previousPointX = xAxis.valueToPoint(dataItemList.get(i - 1).getX(), area);
                double previousPointY = yAxis.valueToPoint(dataItemList.get(i - 1).getY(), area);

                GeneralPath path = new GeneralPath();


                Shape line = new Line2D.Double(previousPointX, previousPointY, pointX, pointY);

           /*     g.drawLine(previousPointX, previousPointY, pointX, pointY);

                Polygon p = new Polygon();
                p.addPoint(previousPointX,previousPointY);
                p.addPoint(pointX,pointY);
                p.addPoint(pointX,yAxis.valueToPoint(0,area));
                p.addPoint(previousPointX,yAxis.valueToPoint(0,area));

                g.fillPolygon(p);*/
            }



        }
    }
}
