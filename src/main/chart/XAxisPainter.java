package main.chart;

import org.w3c.dom.css.Rect;

import java.awt.*;

/**
 * Created by hdablin on 06.04.17.
 */
public class XAxisPainter {
    private LinearAxisX xAxis;
    private LinearAxisY yAxis;


    public XAxisPainter(LinearAxisX xAxis, LinearAxisY yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void draw(Graphics g, Rectangle area){
        g.setColor(Color.YELLOW);
        g.drawLine(area.x,area.y,area.width+area.x,area.y);

        Tick[] ticks = xAxis.getTicks(area);

        for (Tick tick : ticks) {
            g.drawString(tick.getLabel(), tick.getPoint(),area.y);
        }


    }

}
