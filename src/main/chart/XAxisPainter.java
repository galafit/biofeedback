package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class XAxisPainter {
    private LinearAxisX xAxis;
    private int tickSize = 5;



    public XAxisPainter(LinearAxisX xAxis) {
        this.xAxis = xAxis;

    }

    public void draw(Graphics g, Rectangle area, int anchorPoint){
        g.setColor(Color.YELLOW);
        g.drawLine(area.x,anchorPoint,area.width+area.x,anchorPoint);

        Tick[] ticks = xAxis.getTicks(area);

        for (Tick tick : ticks) {
            int tickPoint =  xAxis.valueToPoint(tick.getValue(),area);
            drawLable(g,anchorPoint,tickPoint,tick.getLabel());
            drawTick(g,anchorPoint,tickPoint);
        }


    }

    private void drawTick(Graphics g, int anchorPoint, int tickPoint){

        g.drawLine(tickPoint,anchorPoint,tickPoint,anchorPoint - tickSize);
    }

    private void drawLable(Graphics g, int anchorPoint, int tickPoint, String lable){
        int stringWidth = g.getFontMetrics().stringWidth(lable);
        g.drawString(lable,tickPoint - stringWidth / 2, anchorPoint - tickSize);
    }
}
