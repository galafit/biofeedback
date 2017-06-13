package main.chart;

import main.chart.axis.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 24.03.17.
 */
public class Chart extends JPanel {
    private List<Graph> graphs = new ArrayList<>();
    private List<Axis> xAxis = new ArrayList<>();
    private List<Axis> yAxis = new ArrayList<>();
    private int chartPadding = 10;

    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0 );
    private final Color ORANGE = new Color(255, 153, 0);

    private Color[] colors = {GREY, BROWN, Color.GREEN, Color.YELLOW};
    private Color[] graphicColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN,  Color.PINK};


    public Chart() {
        Axis x = new LinearAxis();
        x.setHorizontal(true);
        xAxis.add(x);
        Axis y = new LinearAxis();
        y.setHorizontal(false);
        yAxis.add(y);
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

    public void addGraph(Graph graph){
       addGraph(graph,0,0);
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

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
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

            int size = xAxis.get(i).getWidth(g, fullArea);

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

            int size = yAxis.get(i).getWidth(g, fullArea);

            if (!yAxis.get(i).isOpposite()){
                leftIndent += size;
                yAxisOriginPoints[i] = leftIndent;
            } else {
                rightIndent += size;
                yAxisOriginPoints[i] = fullArea.width - rightIndent;
            }
        }

        Rectangle area = new Rectangle(leftIndent,topIndent,width - leftIndent - rightIndent,height - topIndent - bottomIndent);
        g.setColor(Color.GRAY);
        g.drawRect(area.x, area.y, area.width, area.height);

        for (int i = 0; i < xAxis.size(); i++) {
            xAxis.get(i).draw(g, area, xAxisOriginPoints[i]);
        }

        for (int i = 0; i < yAxis.size(); i++) {
            yAxis.get(i).draw(g, area, yAxisOriginPoints[i]);
        }

        for (Graph graph : graphs) {
            graph.draw(g,area);
        }



    }


}
