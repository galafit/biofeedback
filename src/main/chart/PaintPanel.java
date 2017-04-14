package main.chart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 24.03.17.
 */
public class PaintPanel extends JPanel {
    private LineChart lineChart;
    private XAxisPainter xAxisPainter;

    private LinearAxisX xAxis;
    private LinearAxisY yAxis;

    private int xIndent=50;
    private int yIndent=50;

    private int width=500;
    private int height=500;


    public PaintPanel(Point2d[] points) {
        setPreferredSize(new Dimension(width, height));
        xAxis = new LinearAxisX(0.01,950);
        yAxis = new LinearAxisY(0,1);
        xAxisPainter = new XAxisPainter(xAxis, new AxisViewSettings());
        lineChart = new LineChart(new ChartItems2DList(points),xAxis, yAxis);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Rectangle area = new Rectangle(xIndent,yIndent,width-2*xIndent,height-2*yIndent);
        g.setColor(Color.GRAY);
        g.drawRect(area.x, area.y, area.width, area.height);
        xAxisPainter.draw(g, area, yIndent - 5);
        lineChart.draw(g, area);
    }


}
