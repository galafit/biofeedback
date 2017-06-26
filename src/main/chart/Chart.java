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
    private Map<Integer,Function2D> functionMap = new Hashtable<Integer, Function2D>();
    private List<Axis> xAxisList = new ArrayList<>();
    private List<Axis> yAxisList = new ArrayList<>();
    private int chartPadding = 10;
    private int axisPadding = 10;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0 );
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN,  Color.PINK};

    private boolean isTicksAligned = true;
    private int[] xAxisOriginPoints;
    private int[] yAxisOriginPoints;
    protected Rectangle graphArea;


    public Chart() {
        Axis x = new LinearAxis();
        x.setHorizontal(true);
        xAxisList.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxisList.add(y);
    }

    public boolean isTicksAligned() {
        return isTicksAligned;
    }

    public void setTicksAligned(boolean ticksAligned) {
        isTicksAligned = ticksAligned;
    }

    public Axis getXAxis(int xAxisIndex){
        if (xAxisIndex >= xAxisList.size()){
            return null;
        }
        return xAxisList.get(xAxisIndex);
    }

    public Axis getYAxis(int yAxisIndex){
        if (yAxisIndex >= yAxisList.size()){
            return null;
        }
        return yAxisList.get(yAxisIndex);
    }


    public void addYAxis(AxisType axisType, boolean isOpposite){

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

    public void addXAxis(AxisType axisType, boolean isOpposite){
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

    public void addGraph(Graph graph, DataList data){
       addGraph(graph, data,0,0);
    }

    public void addGraph(Graph graph, int xAxisIndex, int yAxisIndex){
        graph.setAxis(xAxisList.get(xAxisIndex), yAxisList.get(yAxisIndex));
        graph.rangeXaxis();
        graph.rangeYaxis();
        boolean isGraphExist = false;
        for (Graph graph1 : graphs) {
            if (graph1.getYAxis() == yAxisList.get(yAxisIndex)){
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

    public void addGraph(Graph graph, DataList data, int xAxisIndex, int yAxisIndex){
        graph.setData(data);
        addGraph(graph,xAxisIndex,yAxisIndex);

    }

    public void addGraph(Graph graph, Function2D function, int xAxisIndex, int yAxisIndex) {
        addGraph(graph,xAxisIndex,yAxisIndex);
        functionMap.put(graphs.size() - 1, function);
    }

    public void addGraph(Graph graph, Function2D function){
        addGraph(graph, function,0,0);
    }


    private void alignAxis(List<Axis> axisList, Graphics2D g, Rectangle area){
        int maxSize = 0;
        if(axisList.size() > 1) {
            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(0);
            }

            for (Axis axis : axisList) {
                maxSize = Math.max(maxSize, axis.getTicks(g,area).size());
            }

            for (Axis axis : axisList) {
                axis.getTicksSettings().setTicksAmount(maxSize);
                axis.setEndOnTick(true);
            }
        }
    }


    private void setFunctions(Rectangle area){
        Set keys = functionMap.keySet();
        for (Object key: keys) {
            Function2D function = functionMap.get(key);
            Graph graph = graphs.get((Integer)key);
            Axis xAxis = graph.getXAxis();
            boolean isEndOnTick = xAxis.isEndOnTick();
            double lowerPadding = xAxis.getLowerPadding();
            double upperPadding = xAxis.getUpperPadding();
            xAxis.setUpperPadding(0);
            xAxis.setLowerPadding(0);
            xAxis.setEndOnTick(false);
            XYList data = new XYList();
            for (int i = area.x; i <= area.width + area.x; i++ ){
                double value = xAxis.pointsToValue(i,area);
                data.addItem(value,function.apply(value));
            }
            graph.setData(data);
            // restore axis settings
            xAxis.setEndOnTick(isEndOnTick);
            xAxis.setLowerPadding(lowerPadding);
            xAxis.setUpperPadding(upperPadding);
        }
    }

    //Calculate axis position and indents
    private void defineGraphAreaAndAxisPositions(Graphics2D g, Rectangle fullArea) {
        xAxisOriginPoints = new int[xAxisList.size()];
        yAxisOriginPoints = new int[yAxisList.size()];

        int topIndent = chartPadding;
        int bottomIndent = chartPadding;
        int leftIndent = chartPadding;
        int rightIndent = chartPadding;

        int xAxisAmount =  xAxisList.size() - 1;
        for (int i = xAxisAmount; i >= 0 ; i--) {
            int size = xAxisList.get(i).getWidth(g, fullArea);
            if (i != xAxisAmount) {
                size += axisPadding;
            }
            if (!xAxisList.get(i).isOpposite()){
                bottomIndent += size;
                xAxisOriginPoints[i] = fullArea.y + fullArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisOriginPoints[i] = fullArea.y + topIndent;
            }
        }
        //Calculate axis position and indents of yAxisList
        int yAxisAmount = yAxisList.size() - 1;
        for (int i = yAxisAmount; i >= 0 ; i--) {
            int size = yAxisList.get(i).getWidth(g, fullArea);
            if (i != yAxisAmount) {
                size += axisPadding;
            }
            if (!yAxisList.get(i).isOpposite()){
                leftIndent += size;
                yAxisOriginPoints[i] = fullArea.x + leftIndent;
            } else {
                rightIndent += size;
                yAxisOriginPoints[i] = fullArea.x + fullArea.width - rightIndent;
            }
        }
        graphArea = new Rectangle(fullArea.x + leftIndent, fullArea.y +topIndent,fullArea.width - leftIndent - rightIndent,fullArea.height - topIndent - bottomIndent);
    }

    Rectangle getGraphArea() {
        return graphArea;
    }

     Rectangle calculateGraphArea(Graphics2D g2d, Rectangle fullArea){
        setFunctions(fullArea);

        for (Axis axis : yAxisList){
            axis.resetRange();
        }
        for (Graph graph : graphs) {
            graph.rangeYaxis();
        }

        defineGraphAreaAndAxisPositions(g2d, fullArea);
        if (isTicksAligned()) {
            alignAxis(xAxisList, g2d, graphArea);
            alignAxis(yAxisList, g2d, graphArea);
        }
        defineGraphAreaAndAxisPositions(g2d, fullArea);
        return graphArea;
    }

    void adjustGraphArea (Graphics2D g2d, int areaX, int areaWidth)  {
        int shiftLeft, shiftRight;
        shiftLeft = areaX - graphArea.x;
        shiftRight = graphArea.x + graphArea.width - areaX - areaWidth;

        for (int i = 0; i < yAxisList.size(); i++) {
            if (yAxisList.get(i).isOpposite()){
                yAxisOriginPoints[i] -= shiftRight;
            } else {
                yAxisOriginPoints[i] += shiftLeft;
            }
        }
        graphArea.x = areaX;
        graphArea.width = areaWidth;
        if (isTicksAligned()) {
            alignAxis(xAxisList, g2d, graphArea);
            alignAxis(yAxisList, g2d, graphArea);
        }
    }



    void draw(Graphics2D g2d) {
        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).draw(g2d, graphArea, xAxisOriginPoints[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).draw(g2d, graphArea, yAxisOriginPoints[i]);
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
