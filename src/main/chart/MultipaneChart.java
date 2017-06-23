package main.chart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.06.17.
 */
public class MultipaneChart {
    private List<Chart> charts = new ArrayList<Chart>();

    public void addChart(Chart chart){
        charts.add(chart);
    }

    public Chart getChart(int index){
        return charts.get(index);
    }

    public int chartAmount(){
        return charts.size();
    }


    protected void draw(Graphics2D g2d, Rectangle fullArea) {

        int chartHeight = (chartAmount() == 0) ? fullArea.height : fullArea.height / chartAmount();

        int chartY = fullArea.y;
        List<Rectangle> chartPaintingAreas = new ArrayList<Rectangle>(chartAmount());

        for (Chart chart : charts) {
            Rectangle chartRectangle = new Rectangle(fullArea.x,chartY,fullArea.width,chartHeight);
            chartY = chartY + chartHeight;
            chartPaintingAreas.add(chart.getPaintingArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minWidth = 0;
        for (Rectangle chartPaintingArea : chartPaintingAreas) {
            maxX = Math.max (maxX, chartPaintingArea.x);
            minWidth = Math.min (minWidth, chartPaintingArea.width);
        }

    }


}
