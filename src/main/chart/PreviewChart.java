package main.chart;

import java.awt.*;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 25.06.17.
 */
public class PreviewChart implements Component{
    private List<Chart> charts = new ArrayList<Chart>();
    private List<Preview> previews = new ArrayList<Preview>();
    private List<Integer> chartWeights = new ArrayList<Integer>();
    private List<Integer> previewWeights = new ArrayList<Integer>();

    private boolean isChartsSynchronized = true;

    public boolean isChartsSynchronized() {
        return isChartsSynchronized;
    }

    public void setChartsSynchronized(boolean chartsSynchronized) {
        isChartsSynchronized = chartsSynchronized;
    }

    public void addChart(Chart chart){
        addChart(chart, 1);
    }

    public void addChart(Chart chart, int weight){
        if (weight <= 0){
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage,weight);
            throw new IllegalArgumentException(formattedError);
        }
        chartWeights.add(weight);
        charts.add(chart);
    }

    public void addPreview(Preview preview, int weight){
        if (weight <= 0){
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage,weight);
            throw new IllegalArgumentException(formattedError);
        }
        previewWeights.add(weight);
        previews.add(preview);
    }

    public void addPreview(Preview preview){
        addPreview(preview, 1);
    }



    private Range getMaxRange(List<Chart> charts){
        double min = Double.MAX_VALUE;
        double max = - Double.MAX_VALUE;

        for (Chart chart : charts) {
            max = Math.max(max, chart.getXAxis(0).getRawMax());
            min = Math.min(min, chart.getXAxis(0).getRawMin());
        }
        return new Range(min,max);
    }

    private void synchronizeRanges(List<Chart> charts){
        Range range = getMaxRange(charts);
        for (Chart chart : charts) {
            chart.getXAxis(0).setRange(range.getMin(), range.getMax());
        }
    }

    public void draw(Graphics2D g2d, Rectangle fullArea) {

        if (isChartsSynchronized()) {
            synchronizeRanges(charts);

            List<Chart> castedPreviews = new ArrayList<Chart>();
            for (Preview preview : previews) {
                castedPreviews.add(preview);
            }

            synchronizeRanges(castedPreviews);

        }



        List<Chart> chartsAndPreviews = new AbstractList<Chart>() {
            @Override
            public Chart get(int index) {

                if (index < charts.size()){
                    return charts.get(index);
                }
                return previews.get(index - charts.size());
            }

            @Override
            public int size() {
                return charts.size() + previews.size();
            }

        };

        List<Integer> allWeights = new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {

                if (index < chartWeights.size()){
                    return chartWeights.get(index);
                }
                return previewWeights.get(index - chartWeights.size());
            }

            @Override
            public int size() {
                return chartWeights.size() + previewWeights.size();
            }

        };

        int weightSum = 0;
        for (int j = 0; j < allWeights.size(); j++) {
            weightSum += allWeights.get(j);
        }


        int oneWeightHeight = (chartsAndPreviews.size() == 0) ? fullArea.height : fullArea.height / weightSum;

        int chartY = fullArea.y;
        List<Rectangle> chartGraphAreas = new ArrayList<Rectangle>(chartsAndPreviews.size());


        for (int i = 0; i < chartsAndPreviews.size(); i++) {
            int chartHeight = oneWeightHeight * allWeights.get(i);
            Rectangle chartRectangle = new Rectangle(fullArea.x,chartY,fullArea.width,chartHeight);
            chartY = chartY + chartHeight;
            chartGraphAreas.add(chartsAndPreviews.get(i).getGraphArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minEnd = Integer.MAX_VALUE;
        for (Rectangle area : chartGraphAreas) {
            maxX = Math.max (maxX, area.x);
            minEnd = Math.min (minEnd, area.x + area.width);
        }

        for (int i = 0; i < chartsAndPreviews.size(); i++){
            chartsAndPreviews.get(i).adjustGraphArea(g2d, maxX, minEnd - maxX);
            chartsAndPreviews.get(i).draw(g2d);
        }
    }


}