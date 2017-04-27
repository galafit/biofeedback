package main.chart;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph {

    public LineGraph(List<DataItem> dataItemList) {
        this.dataItemList = dataItemList;
    }


    public void draw(Graphics g, Rectangle area) {

        g.setColor(Color.BLUE);

        for (int i = 0; i < dataItemList.size(); i++) {

            if (i > 0) {
                int pointX = xAxis.valueToPoint(dataItemList.get(i).getX(), area);
                int pointY = yAxis.valueToPoint(dataItemList.get(i).getY(), area);

                int previousPointX = xAxis.valueToPoint(dataItemList.get(i - 1).getX(), area);
                int previousPointY = yAxis.valueToPoint(dataItemList.get(i - 1).getY(), area);

                System.out.println(dataItemList.get(i - 1).getX()+" x, y "+ dataItemList.get(i - 1).getY() );
                System.out.println(previousPointX+" point_x, point_y "+previousPointY );

                g.drawLine(previousPointX, previousPointY, pointX, pointY);
            }
        }
    }

}
