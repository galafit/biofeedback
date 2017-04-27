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
    private List<Axis> horizontalAxis = new ArrayList<>();
    private List<Axis> verticalAxis = new ArrayList<>();

    private int xIndent=50;
    private int yIndent=50;

    public void addVerticalAxis(Axis axis){
        verticalAxis.add(axis);
    }

    public void addHorizontalAxis(Axis axis){
        horizontalAxis.add(axis);
    }

    public void addGraph(Graph graph){
        graphs.add(graph);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        int width = getSize().width;
        int height = getSize().height;
        Rectangle area = new Rectangle(xIndent,yIndent,width-2*xIndent,height-2*yIndent);
        g.setColor(Color.GRAY);
        g.drawRect(area.x, area.y, area.width, area.height);

        boolean xIsOpposite = true;
        int xAnchorPoint;
        if (xIsOpposite){
            xAnchorPoint = yIndent - 5;
        }else{
            xAnchorPoint = area.height + area.y + 5;
        }

        boolean yIsOpposite = false;
        int yAnchorPoint;
        if (yIsOpposite){
            yAnchorPoint = area.width + area.x + 5;
        }else{
            yAnchorPoint = xIndent - 5;
        }

        horizontalAxis.get(0).draw(g, area, xAnchorPoint);
        verticalAxis.get(0).draw(g, area, yAnchorPoint);

        for (Graph graph : graphs) {
            graph.draw(g,area);
        }


    }


}
