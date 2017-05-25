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
        yAxis.add(axis);
    }

    public void addXAxis(Axis axis){
        axis.setHorizontal(true);
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

        int[] xAnchorPoints = new int[xAxis.size()];
        int[] yAnchorPoints = new int[yAxis.size()];

        Rectangle fullArea = new Rectangle(0,0,width,height);

        //Calculate axis position and indents of xAxis
        for (int i = xAxis.size() - 1; i >= 0 ; i--) {

            int size = xAxis.get(i).getSize(g, fullArea);

            if (!xAxis.get(i).getViewSettings().isOpposite()){
                bottomIndent += size;
                xAnchorPoints[i] = fullArea.height - bottomIndent;
            } else {
                topIndent += size;
                xAnchorPoints[i] = topIndent;
            }
        }
        //Calculate axis position and indents of yAxis
        for (int i = yAxis.size() - 1; i >= 0 ; i--) {

            int size = yAxis.get(i).getSize(g, fullArea);

            if (!yAxis.get(i).getViewSettings().isOpposite()){
                leftIndent += size;
                yAnchorPoints[i] = leftIndent;
            } else {
                rightIndent += size;
                yAnchorPoints[i] = fullArea.width - rightIndent;
            }
        }

        Rectangle area = new Rectangle(leftIndent,topIndent,width - leftIndent - rightIndent,height - topIndent - bottomIndent);
        g.setColor(Color.GRAY);
        g.drawRect(area.x, area.y, area.width, area.height);

        for (int i = 0; i < xAxis.size(); i++) {
            xAxis.get(i).draw(g, area, xAnchorPoints[i]);
        }

        for (int i = 0; i < yAxis.size(); i++) {
            yAxis.get(i).draw(g, area, yAnchorPoints[i]);
        }

        for (Graph graph : graphs) {
            graph.draw(g,area);
        }



    }


}
