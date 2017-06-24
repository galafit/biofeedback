package main.chart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.06.17.
 */
public class MultipaneChart implements Component {
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


    public void draw(Graphics2D g2d, Rectangle fullArea) {

        int chartHeight = (chartAmount() == 0) ? fullArea.height : fullArea.height / chartAmount();

        int chartY = fullArea.y;
        List<Rectangle> chartGraphAreas = new ArrayList<Rectangle>(chartAmount());

        for (Chart chart : charts) {
            Rectangle chartRectangle = new Rectangle(fullArea.x,chartY,fullArea.width,chartHeight);
            chartY = chartY + chartHeight;
            chartGraphAreas.add(chart.getGraphArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minEnd = Integer.MAX_VALUE;
        for (Rectangle area : chartGraphAreas) {
            maxX = Math.max (maxX, area.x);
            minEnd = Math.min (minEnd, area.x + area.width);
        }

        for (Chart chart : charts) {
            chart.adjustGraphArea(maxX, minEnd - maxX);
            chart.draw(g2d);
        }
    }


}
