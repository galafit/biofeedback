package main.chart;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class XYList extends AbstractList<DataItem> implements DataList {
    private List<Point2d> items = new ArrayList<>();
    private double xMin = Double.MAX_VALUE;
    private double xMax = Double.MIN_VALUE;
    private double yMin = Double.MAX_VALUE;
    private double yMax = Double.MIN_VALUE;

    public void addItem(double x, double y){
        xMin = Math.min(xMin,x);
        xMax = Math.max(xMax,x);
        yMin = Math.min(yMin,y);
        yMax = Math.max(yMax,y);
        items.add(new Point2d(x,y));
    }


    public double getXmin() {
        return xMin;
    }

    public double getXmax() {
        return xMax;
    }

    public double getYmin() {
        return yMin;
    }

    public double getYmax() {
        return yMax;
    }

    @Override
    public int size(){
        return items.size();
    }

    @Override
    public DataItem get(int index){
        return items.get(index);
    }

}
