package main.chart;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class PaintPanel extends JPanel {
    private LineChart lineChart;
    private XAxisPainter xAxisPainter;

    private LinearAxisX xAxis;
    private LinearAxisY yAxis;

    private int xIndent=20;
    private int yIndent=20;

    private int width=500;
    private int height=500;


    public PaintPanel(Point2d[] points) {
        setPreferredSize(new Dimension(width, height));
        xAxis = new LinearAxisX(0.01,1);
        yAxis = new LinearAxisY(0,1);
        xAxisPainter = new XAxisPainter(xAxis,yAxis);
        lineChart = new LineChart(new ChartItems2DList(points),xAxis, yAxis);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Rectangle area = new Rectangle(xIndent,yIndent,width-2*xIndent,height-2*yIndent);
        g.setColor(Color.GRAY);
        g.drawRect(area.x, area.y, area.width, area.height);
        xAxisPainter.draw(g, area);
        lineChart.draw(g, area);
    }


}
