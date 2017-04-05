package main.chart;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineChart {
    private List<ChartItem> chartItemList;
    private Axis xAxis;
    private Axis yAxis;

    public LineChart(List<ChartItem> chartItemList, Axis xAxis, Axis yAxis) {
        this.chartItemList = chartItemList;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void draw(Graphics g){


    }

    public Axis getxAxis() {
        return xAxis;
    }

    public Axis getyAxis() {
        return yAxis;
    }

    public void setxAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }
}
