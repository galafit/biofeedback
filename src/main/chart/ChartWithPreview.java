package main.chart;

import main.chart.axis.Axis;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 25.06.17.
 */
public class ChartWithPreview implements Drawable {
    private List<Chart> charts = new ArrayList<Chart>();
    private List<Chart> previews = new ArrayList<Chart>();
    private List<Integer> chartWeights = new ArrayList<Integer>();
    private List<Integer> previewWeights = new ArrayList<Integer>();
    private PreviewScrollModel scrollModel;
    private Color cursorColor = Color.RED;
    private int fullChartWidth;

    private double xAxisPointsPerUnit;

    public ChartWithPreview(int fullChartWidth) {
        this.fullChartWidth = fullChartWidth;
        scrollModel = new PreviewScrollModel();
    }

    private boolean isChartsSynchronized = true;

    public void addChart(Chart chart) {
        charts.add(chart);
        chartWeights.add(2);
        for (int i = 0; i < chart.getXAxisAmount(); i++) {
            Axis xAxis = chart.getXAxis(i);
            xAxis.setLength(fullChartWidth);
            xAxis.setOrigin(0);
        }
    }

    public void addChartPanel() {
        addChartPanel(1);
    }

    public void addChartPanel(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        chartWeights.add(weight);
        Chart chart = new Chart();
        chart.getXAxis(0).setAutoScale(false);
        chart.setTicksAligned(false);
        charts.add(chart);
    }



    public void addPreviewPanel(int weight) {
        if (weight <= 0) {
            String errorMessage = "Wrong weight: {0}. Expected > 0.";
            String formattedError = MessageFormat.format(errorMessage, weight);
            throw new IllegalArgumentException(formattedError);
        }
        previewWeights.add(weight);
        Chart preview = new Chart();
        preview.getXAxis(0).setAutoScale(false);
        // preview.getXAxis(0).setRange(0, 150);
        preview.setTicksAligned(false);
        previews.add(preview);
    }

    public void addPreviewPanel() {
        addPreviewPanel(1);
    }

    public void addGraph(int chartPanelIndex, Graph graph, DataList dataList) {
        charts.get(chartPanelIndex).addGraph(graph, dataList);

    }

    private Range getMaxRange() {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (Chart chart : charts) {
            max = Math.max(max, chart.getXAxis(0).getRawMax());
            min = Math.min(min, chart.getXAxis(0).getRawMin());
        }
        return new Range(min, max);
    }

    private void synchronizeRanges() {
        Range range = getMaxRange();
        for (Chart chart : charts) {
            chart.getXAxis(0).setRange(range.getMin(), range.getMax());
        }
        for (Chart chart : previews) {
            chart.getXAxis(0).setRange(range.getMin(), range.getMax());
        }
    }

    public boolean isMouseInsideCursor(int mousePosition){
        if (previews.size() > 0) {

            Rectangle firstArea = previews.get(0).getGraphArea();
            mousePosition = mousePosition - firstArea.x;

            if (mousePosition >= scrollModel.getScrollPosition() && mousePosition <= (scrollModel.getScrollPosition() + scrollModel.getScrollWidth())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void moveCursorPosition(int offset){
        scrollModel.setScrollPosition(scrollModel.getScrollPosition() + offset);
    }

    public void setCursorPosition(int mousePosition) {
        if (previews.size() > 0) {
            double scrollPosition = mousePosition;
            Rectangle firstArea = previews.get(0).getGraphArea();
            if (scrollPosition <= firstArea.getX()) {
                scrollPosition = 0;
            } else if (scrollPosition >= firstArea.getX() + firstArea.getWidth()) {
                scrollPosition = firstArea.getWidth();
            } else {
                scrollPosition -= firstArea.getX();
            }

            scrollModel.setScrollPosition(scrollPosition);
            setChartsAxisOrigins();
        }
    }


    private void setChartsAxisOrigins() {
        // axis position/point corresponding scroll position
        double axisPosition = scrollModel.getScrollPosition() * fullChartWidth / getPaintingAreaWidth();
        double axisOrigin = getPaintingAreaX() - axisPosition;
        for (Chart chart : charts) {
            for (int i = 0; i < chart.getXAxisAmount(); i++) {
                Axis xAxis = chart.getXAxis(i);
                xAxis.setOrigin(axisOrigin);
            }
        }
    }

    public void drawCursor(Graphics2D g2d) {
        if (previews.size() > 0) {
            Rectangle firstArea = previews.get(0).getGraphArea();
            Rectangle lastArea = previews.get(previews.size() - 1).getGraphArea();
            g2d.setColor(cursorColor);
            double cursorX = firstArea.getX() + scrollModel.getScrollPosition();
            double cursorY = firstArea.getY();
            double cursorHeight = lastArea.getY() + lastArea.getHeight() - firstArea.getY();
            g2d.draw(new Rectangle2D.Double(cursorX, cursorY, scrollModel.getScrollWidth(), cursorHeight));

        }
    }

    public void draw(Graphics2D g2d, Rectangle fullArea) {
        if (isChartsSynchronized) {
            synchronizeRanges();
        }
        List<Chart> chartsAndPreviews = new AbstractList<Chart>() {
            @Override
            public Chart get(int index) {

                if (index < charts.size()) {
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

                if (index < chartWeights.size()) {
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
            Rectangle chartRectangle = new Rectangle(fullArea.x, chartY, fullArea.width, chartHeight);
            chartY = chartY + chartHeight;
            chartGraphAreas.add(chartsAndPreviews.get(i).calculateGraphArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minEnd = Integer.MAX_VALUE;
        for (Rectangle area : chartGraphAreas) {
            maxX = Math.max(maxX, area.x);
            minEnd = Math.min(minEnd, area.x + area.width);
        }

        setChartsAxisOrigins();

        for (int i = 0; i < chartsAndPreviews.size(); i++) {
            chartsAndPreviews.get(i).adjustGraphArea(g2d, maxX, minEnd - maxX);
            chartsAndPreviews.get(i).draw(g2d);
        }

        drawCursor(g2d);
    }

    private double getPaintingAreaWidth() {
        return charts.get(0).getGraphArea().getWidth();
    }

    private double getPaintingAreaX() {
        return charts.get(0).getGraphArea().getX();
    }

    class PreviewScrollModel implements ScrollModel {
        double scrollPosition;

        @Override
        public double getMin() {
            return 0;
        }

        @Override
        public double getMax() {
            return getPaintingAreaWidth();
        }

        @Override
        public double getScrollPosition() {
            return scrollPosition;
        }

        @Override
        public double getScrollWidth() {
            return getPaintingAreaWidth() * getPaintingAreaWidth() / fullChartWidth;
        }

        @Override
        public void setScrollPosition(double scrollPosition) {
            if (scrollPosition > getMax() - getScrollWidth()) {
                scrollPosition = getMax() - getScrollWidth();
            }
            this.scrollPosition = scrollPosition;
        }
    }

}