package main.chart;

import main.chart.axis.*;


import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by hdablin on 24.03.17.
 */
public class Chart extends JPanel {
    private List<Graph> graphs = new ArrayList<>();
    private Map<Integer,Function2D> functionMap = new Hashtable<Integer, Function2D>();
    private List<Axis> xAxis = new ArrayList<>();
    private List<Axis> yAxis = new ArrayList<>();
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
        xAxis.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxis.add(y);
    }

    public boolean isTicksAligned() {
        return isTicksAligned;
    }

    public void setTicksAligned(boolean ticksAligned) {
        isTicksAligned = ticksAligned;
    }

    public Axis getXAxis(int xAxisIndex){
        if (xAxisIndex >= xAxis.size()){
            return null;
        }
        return xAxis.get(xAxisIndex);
    }

    public Axis getYAxis(int yAxisIndex){
        if (yAxisIndex >= yAxis.size()){
            return null;
        }
        return yAxis.get(yAxisIndex);
    }

    public void addYAxis(Axis axis){
        axis.setHorizontal(false);
        axis.getViewSettings().setAxisColor(colors[xAxis.size() % colors.length]);
        yAxis.add(axis);
    }

    public void addXAxis(Axis axis){
        axis.setHorizontal(true);
        axis.getViewSettings().setAxisColor(colors[xAxis.size() % colors.length]);
        xAxis.add(axis);
    }

    public void addGraph(Graph graph, DataList data){
       addGraph(graph, data,0,0);
    }

    public void addGraph(Graph graph, int xAxisIndex, int yAxisIndex){
        if (xAxisIndex >= xAxis.size()) {
            xAxisIndex = xAxis.size() - 1;
        }
        if (yAxisIndex >= yAxis.size()) {
            yAxisIndex = yAxis.size() - 1;
        }

        graph.setAxis(xAxis.get(xAxisIndex), yAxis.get(yAxisIndex));
        graph.rangeAxis();

        boolean isGraphExist = false;
        for (Graph graph1 : graphs) {
            if (graph1.getYAxis() == yAxis.get(yAxisIndex)){
                isGraphExist = true;
                break;
            }
        }

        if (!isGraphExist) {
            graph.setColor(yAxis.get(yAxisIndex).getViewSettings().getAxisColor());
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
            XYList data = new XYList();
            for (int i = area.x; i <= area.width + area.x; i++ ){
                double value = graph.getXAxis().pointsToValue(i,area);
                data.addItem(value,function.apply(value));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        int width = getSize().width;
        int height = getSize().height;

        int topIndent = chartPadding;
        int bottomIndent = chartPadding;
        int leftIndent = chartPadding;
        int rightIndent = chartPadding;

        int[] xAxisOriginPoints = new int[xAxis.size()];
        int[] yAxisOriginPoints = new int[yAxis.size()];

        Rectangle fullArea = new Rectangle(0,0,width,height);

        //Calculate axis position and indents of xAxis
        for (int i = xAxis.size() - 1; i >= 0 ; i--) {

            int size = xAxis.get(i).getWidth(g2d, fullArea);

            if (!xAxis.get(i).isOpposite()){
                bottomIndent += size;
                xAxisOriginPoints[i] = fullArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAxisOriginPoints[i] = topIndent;
            }
        }
        //Calculate axis position and indents of yAxis
        for (int i = yAxis.size() - 1; i >= 0 ; i--) {

            int size = yAxis.get(i).getWidth(g2d, fullArea);

            if (!yAxis.get(i).isOpposite()){
                leftIndent += size;
                yAxisOriginPoints[i] = leftIndent;
            } else {
                rightIndent += size;
                yAxisOriginPoints[i] = fullArea.width - rightIndent;
            }
        }

        Rectangle area = new Rectangle(leftIndent,topIndent,width - leftIndent - rightIndent,height - topIndent - bottomIndent);
        //g2d.setColor(Color.GRAY);
        //g2d.drawRect(area.x, area.y, area.width, area.height);

        setFunctions(area);

        if (isTicksAligned()) {
            alignAxis(xAxis, g2d, area);
            alignAxis(yAxis, g2d, area);
        }


        for (int i = 0; i < xAxis.size(); i++) {
            xAxis.get(i).draw(g2d, area, xAxisOriginPoints[i]);
        }

        for (int i = 0; i < yAxis.size(); i++) {
            yAxis.get(i).draw(g2d, area, yAxisOriginPoints[i]);
        }

        for (Graph graph : graphs) {
            graph.draw(g2d,area);
        }




    }


}
