package main.chart;

import main.chart.axis.Axis;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph {
    protected List<DataItem> dataItemList;
    protected Axis xAxis;
    protected Axis yAxis;



    public void setAxis(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public abstract void draw(Graphics g, Rectangle area);
}
