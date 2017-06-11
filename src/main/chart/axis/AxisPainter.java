package main.chart.axis;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by hdablin on 06.04.17.
 */
public class AxisPainter {
    private AxisData axis;
    private AxisViewSettings axisViewSettings;
    private int tickLabelPadding = 3;

    public AxisPainter(AxisData axis, AxisViewSettings axisViewSettings) {
        this.axis = axis;
        this.axisViewSettings = axisViewSettings;
    }

    public int getAxisWidth(Graphics g, Rectangle area) {
        if(!axisViewSettings.isVisible()) {
            return 0;
        }

        int size = 1;
        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), Font.PLAIN, axisViewSettings.getLabelFontSize()));

        TickProvider tickProvider = getTickProvider(g, area);
        List<Tick> ticks = tickProvider.getTicks();

        if (axisViewSettings.isTicksVisible()) {

            size = tickLabelPadding + axisViewSettings.getTickSize() + getMaxTickLabelSize(g, ticks, !axis.isHorizontal());
        }

        return size;
    }

    public void draw(Graphics g, Rectangle area, int axisOriginPoint) {
       if(axisViewSettings.isVisible()) {
           Font font = g.getFont();
           g.setFont(new Font(font.getFontName(), Font.PLAIN, axisViewSettings.getLabelFontSize()));

           TickProvider tickProvider = getTickProvider(g, area);
           List<Tick> ticks = tickProvider.getTicks();

           if (axisViewSettings.isMinorGridVisible()) {
               drawMinorGrid(g, area, ticks);
           }

           if (axisViewSettings.isGridVisible()) {
               drawGrid(g, area, ticks);
           }

           if (axisViewSettings.isTicksVisible()) {
               drawTicks(g, area, axisOriginPoint, ticks);
           }

           if (axisViewSettings.isAxisLineVisible()) {
               drawAxisLine(g, area, axisOriginPoint);
           }

       }
    }

    private void drawAxisLine(Graphics g, Rectangle area, int axisOriginPoint) {
        g.setColor(axisViewSettings.getAxisColor());
        if (axis.isHorizontal) {
            g.drawLine(area.x, axisOriginPoint, area.width + area.x, axisOriginPoint);
        } else {
            g.drawLine(axisOriginPoint, area.y, axisOriginPoint, area.y + area.height);
        }

    }

    private TickProvider getTickProvider(Graphics g, Rectangle area) {
        TickProvider tickProvider = axis.getTicksProvider(area);

        if (tickProvider.getTickInterval().isNaN()) {
            tickProvider.setTickPixelInterval(axisViewSettings.getTickPixelInterval());
        }

        List<Tick> ticks = tickProvider.getTicks();

        int maxTickSize = getMaxTickLabelSize(g, ticks, axis.isHorizontal());

        if (ticks.size() > 1) {

            if ((maxTickSize + axisViewSettings.getMinLabelSpace()) > tickProvider.getTickPixelInterval()) {
                tickProvider.setMinTickPixelInterval(maxTickSize + axisViewSettings.getMinLabelSpace());
            }
        }

        return tickProvider;

    }

    private void drawTicks(Graphics g, Rectangle area, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(axisViewSettings.getAxisColor());
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min <= tick.getValue() && tick.getValue() <= max) {
                int tickPoint = axis.valueToPoint(tick.getValue(), area);
                drawLabel(g, area, axisOriginPoint, tickPoint, tick.getLabel());
                drawTick(g, axisOriginPoint, tickPoint);
            }
        }
    }

    private void drawGrid(Graphics g, Rectangle area, List<Tick> ticks) {
        g.setColor(axisViewSettings.getGridColor());
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min <= tick.getValue() && tick.getValue() <= max) {
                if (axis.isHorizontal) {
                    g.drawLine(axis.valueToPoint(tick.getValue(), area), area.y, axis.valueToPoint(tick.getValue(), area), area.y + area.height);
                } else {
                    g.drawLine(area.x, axis.valueToPoint(tick.getValue(), area), area.x + area.width, axis.valueToPoint(tick.getValue(), area));
                }
            }
        }
    }

    private void drawMinorGrid(Graphics g, Rectangle area, List<Tick> ticks) {
        if (ticks.size() > 1) {
            g.setColor(axisViewSettings.getMinorGridColor());
            double max = axis.getMax();
            double min = axis.getMin();
            double tickInterval = ticks.get(1).getValue() - ticks.get(0).getValue();
            double minorTickInterval = tickInterval / axisViewSettings.getMinorGridDivider();

            double minorTickValue = ticks.get(0).getValue();
            while (minorTickValue <= max) {
                if (min <= minorTickValue) {
                    if (axis.isHorizontal) {
                        g.drawLine(axis.valueToPoint(minorTickValue, area), area.y, axis.valueToPoint(minorTickValue, area), area.y + area.height);
                    } else {
                        g.drawLine(area.x, axis.valueToPoint(minorTickValue, area), area.x + area.width, axis.valueToPoint(minorTickValue, area));
                    }
                }
                minorTickValue += minorTickInterval;
            }
        }

    }

    private void drawTick(Graphics g, int axisOriginPoint, int tickPoint) {
        if (axis.isHorizontal) {
            g.drawLine(tickPoint, axisOriginPoint + axisViewSettings.getTickSize() / 2, tickPoint, axisOriginPoint - axisViewSettings.getTickSize() / 2);
        } else {
            g.drawLine(axisOriginPoint - axisViewSettings.getTickSize() / 2, tickPoint, axisOriginPoint + axisViewSettings.getTickSize() / 2, tickPoint);
        }
    }

    private void drawLabel(Graphics g, Rectangle area, int axisOriginPoint, int tickPoint, String label) {
        //HORIZONTAL position
        if (axis.isHorizontal()) {
            // TOP axis position
            if (axisViewSettings.isOpposite()) {
                g.drawString(label, tickPoint - getLabelWidth(g, label) / 2, axisOriginPoint - axisViewSettings.getTickSize() / 2 - tickLabelPadding);
            } else { //BOTTOM axis position
                g.drawString(label, tickPoint - getLabelWidth(g, label) / 2, axisOriginPoint + axisViewSettings.getTickSize() / 2 + tickLabelPadding + getLabelHeight(g, label));
            }

        } else { //VERTICAL position
            //RIGTH axis position
            if (axisViewSettings.isOpposite()) {
                g.drawString(label, axisOriginPoint + axisViewSettings.getTickSize() / 2 + tickLabelPadding, tickPoint + getLabelHeight(g, label) / 2);

            } else { //LEFT axis position
                g.drawString(label, axisOriginPoint - axisViewSettings.getTickSize() / 2 - getLabelWidth(g, label) - tickLabelPadding, tickPoint + getLabelHeight(g, label) / 2);
            }
        }
    }

    private int getLabelWidth(Graphics g, String label) {
        return g.getFontMetrics().stringWidth(label);
    }

    private int getLabelHeight(Graphics g, String label) {
        // return (int)(g.getFontMetrics().getStringBounds(label,(Graphics2D)(g)).getHeight());
        TextLayout layout = new TextLayout(label, g.getFont(), ((Graphics2D) g).getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return (int) labelBounds.getHeight();
    }

    private int getMaxTickLabelSize(Graphics g, List<Tick> ticks, boolean isHorizontal) {
        if (isHorizontal) {
            int maxSize = 0;
            for (Tick tick : ticks) {
                maxSize = Math.max(maxSize, getLabelWidth(g, tick.getLabel()));
            }
            return maxSize;
        }
        return g.getFontMetrics().getHeight();
    }
}
