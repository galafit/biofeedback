package main.chart;

import main.chart.axis.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class PaintPanel extends JPanel {
    private LineChart lineChart;

    private Axis xAxis;
    private Axis yAxis;

    private int xIndent=50;
    private int yIndent=50;



    public PaintPanel(Point2d[] points) {

        xAxis = new LinearAxis(true);
        yAxis = new LinearAxis(false);


        lineChart = new LineChart(new ChartItems2DList(points),xAxis, yAxis);
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

        xAxis.draw(g, area, xAnchorPoint);
        yAxis.draw(g, area, yAnchorPoint);
        lineChart.draw(g, area);
    }


}
