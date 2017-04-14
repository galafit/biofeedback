package main.chart;

import main.graph.Graph;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class XAxisPainter {
    private LinearAxisX xAxis;
    private AxisViewSettings axisViewSettings;
    private int tickLabelPadding = 2;
    private int minorGridDivider = 5;


    public XAxisPainter(LinearAxisX xAxis, AxisViewSettings axisViewSettings) {
        this.xAxis = xAxis;
        this.axisViewSettings = axisViewSettings;
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){

        Tick[] ticks = calculateTicks(g, area);

        if (axisViewSettings.isAxisLineVisible()) {
            drawAxisLine(g, area, anchorPoint);
        }

        if (axisViewSettings.isTicksVisible()) {
            drawTicks(g, area, anchorPoint,ticks);
        }

        if (axisViewSettings.isGridVisible()) {
            drawGrid(g, area, ticks);
        }

        drawMinorGrid(g, area, ticks);

    }

    private void drawAxisLine (Graphics g, Rectangle area, int anchorPoint){
        g.setColor(axisViewSettings.getAxisColor());
        g.drawLine(area.x,anchorPoint,area.width+area.x,anchorPoint);
    }

    private void drawGrid (Graphics g, Rectangle area, Tick[] ticks){
        g.setColor(axisViewSettings.getGridColor());
        for (Tick tick : ticks) {
            g.drawLine(xAxis.valueToPoint(tick.getValue(),area),area.y,xAxis.valueToPoint(tick.getValue(),area),area.y + area.height);
        }

    }

    private Tick[] calculateTicks(Graphics g, Rectangle area){
        Tick[] ticks = xAxis.getTicks(area,axisViewSettings.getTickPixelInterval());
        int maxTickSize = getMaxTickSize(g,ticks);

        if (ticks.length > 1) {
            int tickInterval = xAxis.valueToPoint(ticks[1].getValue(),area) - xAxis.valueToPoint(ticks[0].getValue(),area);
            if ((maxTickSize + axisViewSettings.getMinLabelSpace()) > tickInterval) {
                ticks = xAxis.getTicks1(area, maxTickSize + axisViewSettings.getMinLabelSpace());
            }
        }

        return ticks;

    }

    private void drawTicks(Graphics g, Rectangle area, int anchorPoint, Tick[] ticks){
        g.setColor(axisViewSettings.getAxisColor());
        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), Font.PLAIN, axisViewSettings.getLabelFontSize()));


        for (Tick tick : ticks) {
            int tickPoint =  xAxis.valueToPoint(tick.getValue(),area);
            drawLabel(g,anchorPoint,tickPoint,tick.getLabel());
            drawTick(g,anchorPoint,tickPoint);
        }


    }

    private void drawMinorGrid(Graphics g, Rectangle area, Tick[] ticks){
        g.setColor(axisViewSettings.getMinorGridColor());
        if (ticks.length > 1) {
            double tickInterval = ticks[1].getValue() - ticks[0].getValue();
            double minorGridInterval = tickInterval / minorGridDivider;
            for (Tick tick : ticks) {
                for (int i = 1; i < minorGridDivider; i++) {
                    int x = xAxis.valueToPoint(tick.getValue() + minorGridInterval * i, area);
                    g.drawLine(x, area.y, x, area.y + area.height);
                }

            }
        }
    }

    private void drawTick(Graphics g, int anchorPoint, int tickPoint){

        g.drawLine(tickPoint,anchorPoint + axisViewSettings.getTickSize() / 2,tickPoint,anchorPoint - axisViewSettings.getTickSize() / 2);
    }

    private void drawLabel(Graphics g, int anchorPoint, int tickPoint, String label) {
        g.drawString(label,tickPoint - getLabelSize(g, label) / 2, anchorPoint - axisViewSettings.getTickSize() /2 - tickLabelPadding);
    }

    private int getLabelSize(Graphics g, String label){
        return g.getFontMetrics().stringWidth(label);
    }

    private int getMaxTickSize(Graphics g, Tick[] ticks){
        int maxSize = 0;
        for (Tick tick : ticks) {
           maxSize = Math.max(maxSize,getLabelSize(g,tick.getLabel()));
        }
        return maxSize;
    }
}
