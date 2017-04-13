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


    public XAxisPainter(LinearAxisX xAxis, AxisViewSettings axisViewSettings) {
        this.xAxis = xAxis;
        this.axisViewSettings = axisViewSettings;
    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        g.setColor(axisViewSettings.getAxisColor());
        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), Font.PLAIN, axisViewSettings.getLabelFontSize()));
        g.drawLine(area.x,anchorPoint,area.width+area.x,anchorPoint);

        Tick[] ticks = xAxis.getTicks(area,axisViewSettings.getTickPixelInterval());
        int maxTickSize = getMaxTickSize(g,ticks);

        if (maxTickSize > axisViewSettings.getTickPixelInterval()){
            ticks = xAxis.getTicks1(area, maxTickSize);
        }

        for (Tick tick : ticks) {
            int tickPoint =  xAxis.valueToPoint(tick.getValue(),area);
            drawLabel(g,anchorPoint,tickPoint,tick.getLabel());
            drawTick(g,anchorPoint,tickPoint);
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
