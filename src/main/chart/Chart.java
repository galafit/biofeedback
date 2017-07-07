package main.chart;

import main.chart.axis.*;
import main.chart.functions.Function2D;


import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by hdablin on 24.03.17.
 */
public class Chart implements Drawable {
    private List<Graph> graphs = new ArrayList<>();
    private Map<Integer, Function2D> functionMap = new Hashtable<Integer, Function2D>();
    private List<Axis> xAxisList = new ArrayList<>();
    private List<Axis> yAxisList = new ArrayList<>();
    private int chartPadding = 10;
    private int axisPadding = 10;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN, Color.PINK};

    private boolean isTicksAlignmentEnable = false;
    private int[] xAxisPositions;
    private int[] yAxisPositions;
    private Rectangle graphArea; // area to draw graphs


    public Chart() {
        Axis x = new LinearAxis();
        x.setHorizontal(true);
        xAxisList.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxisList.add(y);
    }

    public int getMaxGraphSize() {
        int maxSize = 0;
        // Graphics with functions are not taken into account
        for (int i = 0; i < graphs.size(); i++) {
            if(functionMap.get(new Integer(i)) == null) {
                maxSize = Math.max(maxSize, graphs.get(i).getDataSize());
            }
        }
        return maxSize;
    }

    // define whether ticks on different(opposite) xAxis or yAxis should be aligned/synchronized
    public void enableTicksAlignment(boolean isEnabled) {
        isTicksAlignmentEnable = isEnabled;
    }

    public Axis getXAxis(int xAxisIndex) {
        return xAxisList.get(xAxisIndex);
    }

    public Axis getYAxis(int yAxisIndex) {
        return yAxisList.get(yAxisIndex);
    }

    public int getXAxisAmount() {
        return xAxisList.size();
    }

    public int getYAxisAmount() {
        return yAxisList.size();
    }

    public void addYAxis(AxisType axisType, boolean isOpposite) {
        Axis axis = new LinearAxis();
        if (yAxisList.size() > 0) {
            axis.getGridSettings().setGridLineWidth(0);
            axis.getGridSettings().setMinorGridLineWidth(0);
        }
        axis.setOpposite(isOpposite);
        axis.setHorizontal(false);
        axis.getViewSettings().setAxisColor(colors[yAxisList.size() % colors.length]);
        yAxisList.add(axis);
    }

    public void addXAxis(AxisType axisType, boolean isOpposite) {
        Axis axis = new LinearAxis();
        if (xAxisList.size() > 0) {
            axis.getGridSettings().setGridLineWidth(0);
            axis.getGridSettings().setMinorGridLineWidth(0);
        }
        axis.setOpposite(isOpposite);
        axis.setHorizontal(true);
        axis.getViewSettings().setAxisColor(colors[xAxisList.size() % colors.length]);
        xAxisList.add(axis);
    }

    public void update(){
        for (Graph graph : graphs) {
            graph.rangeXaxis();
        }
    }

    public Graph getGraph(int index){
        return graphs.get(index);
    }

    public int getGraphsAmoiunt(){
        return graphs.size();
    }


    public void addGraph(Graph graph, DataList data) {
        addGraph(graph, data, 0, 0);
    }

    public void addGraph(Graph graph, DataList data, int xAxisIndex, int yAxisIndex) {
        graph.setData(data);
        addGraph(graph, xAxisIndex, yAxisIndex);

    }
    public void addGraph(Graph graph, Function2D function, int xAxisIndex, int yAxisIndex) {
        addGraph(graph, xAxisIndex, yAxisIndex);
        functionMap.put(graphs.size() - 1, function);
    }

    public void addGraph(Graph graph, Function2D function) {
        addGraph(graph, function, 0, 0);
    }

    private void addGraph(Graph graph, int xAxisIndex, int yAxisIndex) {
        graph.setAxis(xAxisList.get(xAxisIndex), yAxisList.get(yAxisIndex));
        graph.rangeXaxis();
        boolean isGraphExist = false;
        for (Graph graph1 : graphs) {
            if (graph1.getYAxis() == yAxisList.get(yAxisIndex)) {
                isGraphExist = true;
                break;
            }
        }

        if (!isGraphExist) {
            graph.setColor(yAxisList.get(yAxisIndex).getViewSettings().getAxisColor());
        } else {
            graph.setColor(graphicColors[graphs.size() % graphicColors.length]);
        }
        graphs.add(graph);
    }

    private void alignAxisTicks(List<Axis> axisList, Graphics2D g, Rectangle area) {
        int maxSize = 0;
        if (axisList.size() > 1) {
            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(0);
            }

            for (Axis axis : axisList) {
                maxSize = Math.max(maxSize, axis.getTicks(g, area).size());
            }

            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(maxSize);
                axis.setEndOnTick(true);
            }
        }
    }


    private void tieAxisStartValuesToArea(Rectangle area) {
        for (Axis axis : xAxisList) {
           if(axis.getStartValue() != null) {
               double startPoint = axis.valueToPoint(axis.getStartValue(), area);
               double newMinPoint = area.getX() - (startPoint - axis.getMinPoint(area));
               axis.setMinPoint(newMinPoint);
           }
        }
    }


    /**
     * We create XYList for every function. For every area point is calculated corresponding
     * value and function(value) and added to the list. So:
     * resultant XYLists contains «area.width» elements. Then those XYLists are added to
     * the corresponding Graphs.
     * @param area
     */
    private void setFunctions(Rectangle area) {
        Set keys = functionMap.keySet();
        for (Object key : keys) {
            Function2D function = functionMap.get(key);
            Graph graph = graphs.get((Integer) key);
            Axis xAxis = graph.getXAxis();
            boolean isEndOnTick = xAxis.isEndOnTick();
            double lowerPadding = xAxis.getLowerPadding();
            double upperPadding = xAxis.getUpperPadding();
            xAxis.setUpperPadding(0);
            xAxis.setLowerPadding(0);
            xAxis.setEndOnTick(false);
            XYList data = new XYList();
            for (int i = area.x; i <= area.width + area.x; i++) {
                double value = xAxis.pointsToValue(i, area);
                data.addItem(value, function.apply(value));
            }
            graph.setData(data);
            // restore axis settings
            xAxis.setEndOnTick(isEndOnTick);
            xAxis.setLowerPadding(lowerPadding);
            xAxis.setUpperPadding(upperPadding);
        }
    }

    //Calculate and set axis positions and graphArea
    private void setGraphAreaAndAxisPositions(Graphics2D g, Rectangle fullArea) {
        xAxisPositions = new int[xAxisList.size()];
        yAxisPositions = new int[yAxisList.size()];

        int topIndent = chartPadding;
        int bottomIndent = chartPadding;
        int leftIndent = chartPadding;
        int rightIndent = chartPadding;

        //Calculate position and indents of xAxisList
        int xAxisAmount = xAxisList.size() - 1;
        for (int i = xAxisAmount; i >= 0; i--) {
            int size = xAxisList.get(i).getWidth(g, fullArea);
            if (i != xAxisAmount) {
                size += axisPadding;
            }
            if (!xAxisList.get(i).isOpposite()) {
                bottomIndent += size;
                xAxisPositions[i] = fullArea.y + fullArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisPositions[i] = fullArea.y + topIndent;
            }
        }
        //Calculate position and indents of yAxisList
        int yAxisAmount = yAxisList.size() - 1;
        for (int i = yAxisAmount; i >= 0; i--) {
            int size = yAxisList.get(i).getWidth(g, fullArea);
            if (i != yAxisAmount) {
                size += axisPadding;
            }
            if (!yAxisList.get(i).isOpposite()) {
                leftIndent += size;
                yAxisPositions[i] = fullArea.x + leftIndent;
            } else {
                rightIndent += size;
                yAxisPositions[i] = fullArea.x + fullArea.width - rightIndent;
            }
        }
        Rectangle graphArea = new Rectangle(fullArea.x + leftIndent, fullArea.y + topIndent, fullArea.width - leftIndent - rightIndent, fullArea.height - topIndent - bottomIndent);
        setGraphArea(graphArea);
    }

    Rectangle getGraphArea() {
        return graphArea;
    }

    private void setGraphArea(Rectangle newGraphArea) {
        graphArea = newGraphArea;
        tieAxisStartValuesToArea(newGraphArea);
        for (Axis axis : yAxisList) {
            axis.resetRange();
        }
        for (Graph graph : graphs) {
            graph.setDataRange(newGraphArea);
            graph.rangeYaxis();
        }

    }

    Rectangle calculateGraphArea(Graphics2D g2d, Rectangle fullArea) {
        tieAxisStartValuesToArea(fullArea);
        setFunctions(fullArea);
        for (Axis axis : yAxisList) {
            axis.resetRange();
        }
        for (Graph graph : graphs) {
            graph.setDataRange(fullArea);
            graph.rangeYaxis();
        }

        setGraphAreaAndAxisPositions(g2d, fullArea);

        if (isTicksAlignmentEnable) {
            alignAxisTicks(xAxisList, g2d, graphArea);
            alignAxisTicks(yAxisList, g2d, graphArea);
            setGraphAreaAndAxisPositions(g2d, fullArea);
        }
        return graphArea;
    }

    // used in multi-pane charts to align its graph areas
    void reduceGraphArea(Graphics2D g2d, int graphAreaX, int graphAreaWidth) {
        int shiftLeft, shiftRight;
        Rectangle graphArea = this.graphArea;
        shiftLeft = graphAreaX - graphArea.x;
        shiftRight = graphArea.x + graphArea.width - graphAreaX - graphAreaWidth;

        for (int i = 0; i < yAxisList.size(); i++) {
            if (yAxisList.get(i).isOpposite()) {
                yAxisPositions[i] -= shiftRight;
            } else {
                yAxisPositions[i] += shiftLeft;
            }
        }
        graphArea.x = graphAreaX;
        graphArea.width = graphAreaWidth;
        setGraphArea(graphArea);
    }


    void draw(Graphics2D g2d) {
        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).draw(g2d, graphArea, xAxisPositions[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).draw(g2d, graphArea, yAxisPositions[i]);
        }

        Rectangle clip = g2d.getClipBounds();
        g2d.setClip(graphArea);

        for (Graph graph : graphs) {
            graph.draw(g2d, graphArea);
        }

        g2d.setClip(clip);
    }


    public void draw(Graphics2D g2d, Rectangle fullArea) {
        calculateGraphArea(g2d, fullArea);
        draw(g2d);
    }
}
