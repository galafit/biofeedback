package main.chart;

import main.chart.axis.Axis;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph {
    protected DataList dataItemList;
    protected Axis xAxis;
    protected Axis yAxis;
    protected Color color = Color.GRAY;

    protected void rangeAxis(){
        if (xAxis.isAutoScale()){
            xAxis.setRange(Math.min(xAxis.getMin(),dataItemList.getXmin()),Math.max(xAxis.getMax(),dataItemList.getXmax()));
        }
        if (yAxis.isAutoScale()){
            System.out.println("getMin = " + yAxis.getMin());
            System.out.println("getMax = " + yAxis.getMax());
            System.out.println("itemYmin = " + dataItemList.getYmin());
            System.out.println("itemYmax = " + dataItemList.getYmax());
            yAxis.setRange(Math.min(yAxis.getMin(),dataItemList.getYmin()),Math.max(yAxis.getMax(),dataItemList.getYmax()));
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getXmin(){
       return dataItemList.getXmin();
    }

    public double getXmax(){
        return dataItemList.getXmax();
    }

    public double getYmin(){
        return dataItemList.getXmin();
    }

    public double getYmax(){
        return dataItemList.getXmax();
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public void setAxis(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public abstract void draw(Graphics g, Rectangle area);
}
