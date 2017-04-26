package main.chart;

import main.chart.axis.Axis;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph {
    protected List<ChartItem> chartItemList;
    protected Axis xAxis;
    protected Axis yAxis;

    public void setxAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }


    public void setyAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }

    public abstract void draw(Graphics g, Rectangle area);
}
