package main.chart;

import main.chart.axis.*;
import main.chart.functions.Function2D;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by hdablin on 24.03.17.
 */
public class Chart extends JPanel {
    private List<Graph> graphs = new ArrayList<>();
    private Map<Integer,Function2D> functionMap = new Hashtable<Integer, Function2D>();
    private List<Axis> xAxisList = new ArrayList<>();
    private List<Axis> yAxisList = new ArrayList<>();
    private int chartPadding = 10;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0 );
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN,  Color.PINK};

    private boolean isTicksAligned = true;


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

    public void addYAxis(Axis axis){
        axis.setHorizontal(false);
        axis.getViewSettings().setAxisColor(colors[xAxisList.size() % colors.length]);
        yAxisList.add(axis);
    }

    public void addXAxis(Axis axis){
        axis.setHorizontal(true);
        axis.getViewSettings().setAxisColor(colors[xAxisList.size() % colors.length]);
        xAxisList.add(axis);
    }

    public void addGraph(Graph graph, DataList data){
       addGraph(graph, data,0,0);
    }

    public void addGraph(Graph graph, int xAxisIndex, int yAxisIndex){
        if (xAxisIndex >= xAxisList.size()) {
            xAxisIndex = xAxisList.size() - 1;
        }
        if (yAxisIndex >= yAxisList.size()) {
            yAxisIndex = yAxisList.size() - 1;
        }

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

    private Rectangle calculateAxisPositions(Graphics2D g, Rectangle area, int[] xAxisOriginPoints, int[] yAxisOriginPoints) {
        //Calculate axis position and indents of xAxisList
        int width = getSize().width;
        int height = getSize().height;
        int topIndent = chartPadding;
        int bottomIndent = chartPadding;
        int leftIndent = chartPadding;
        int rightIndent = chartPadding;
        for (int i = xAxisList.size() - 1; i >= 0 ; i--) {

            int size = xAxisList.get(i).getWidth(g, area);

            if (!xAxisList.get(i).isOpposite()){
                bottomIndent += size;
                xAxisOriginPoints[i] = area.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisOriginPoints[i] = topIndent;
            }
        }
        //Calculate axis position and indents of yAxisList
        for (int i = yAxisList.size() - 1; i >= 0 ; i--) {

            int size = yAxisList.get(i).getWidth(g, area);

            if (!yAxisList.get(i).isOpposite()){
                leftIndent += size;
                yAxisOriginPoints[i] = leftIndent;
            } else {
                rightIndent += size;
                yAxisOriginPoints[i] = area.width - rightIndent;
            }
        }

        Rectangle newArea = new Rectangle(leftIndent,topIndent,width - leftIndent - rightIndent,height - topIndent - bottomIndent);
        return newArea;

    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        int width = getSize().width;
        int height = getSize().height;


        int[] xAxisOriginPoints = new int[xAxisList.size()];
        int[] yAxisOriginPoints = new int[yAxisList.size()];

        Rectangle fullArea = new Rectangle(0,0,width,height);

        setFunctions(fullArea);

        for (Axis axis : yAxisList){
            axis.resetRange();
        }

        for (Graph graph : graphs) {
            graph.rangeYaxis();
        }

        Rectangle area = calculateAxisPositions(g2d, fullArea, xAxisOriginPoints, yAxisOriginPoints);

        if (isTicksAligned()) {
            alignAxis(xAxisList, g2d, area);
            alignAxis(yAxisList, g2d, area);
        }

        area = calculateAxisPositions(g2d, fullArea, xAxisOriginPoints, yAxisOriginPoints);


        for (int i = 0; i < xAxisList.size(); i++) {
            xAxisList.get(i).draw(g2d, area, xAxisOriginPoints[i]);
        }

        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).draw(g2d, area, yAxisOriginPoints[i]);
        }

        g2d.setClip(area);

        for (Graph graph : graphs) {
            graph.draw(g2d,area);
        }
    }
}
