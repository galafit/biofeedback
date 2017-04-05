package main.chart;

import java.awt.geom.Point2D;
import java.util.AbstractList;

/**
 * Created by hdablin on 05.04.17.
 */
public class ChartItems2DList extends AbstractList<ChartItem> {
    Point2d[] chartItems2dList;

    public ChartItems2DList(Point2d[] chartItems2dList) {
        this.chartItems2dList = chartItems2dList;
    }

    @Override
    public int size(){
        return chartItems2dList.length;
    }

    @Override
    public ChartItem get(int index){
        return chartItems2dList[index];
    }

}
