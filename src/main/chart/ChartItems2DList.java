package main.chart;

import java.util.AbstractList;

/**
 * Created by hdablin on 05.04.17.
 */
public class ChartItems2DList extends AbstractList<DataItem> {
    Point2d[] chartItems2dList;
    boolean reversed;

    public ChartItems2DList(Point2d[] chartItems2dList, boolean reversed) {
        this.chartItems2dList = chartItems2dList;
        this.reversed = reversed;
    }

    @Override
    public int size(){
        return chartItems2dList.length;
    }

    @Override
    public DataItem get(int index){
        if(reversed) {
            DataItem item = chartItems2dList[index];
            return  new DataItem() {
                @Override
                public double getX() {
                    return item.getY();
                }

                @Override
                public double getY() {
                    return item.getX();
                }
            };
        }
        return chartItems2dList[index];
    }

}
