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


    public AxisPainter(AxisData axis) {
        this.axis = axis;
    }

    public int getAxisWidth(Graphics g, Rectangle area) {
        if(!axis.getAxisViewSettings().isVisible()) {
            return 0;
        }

        int size = 1;
        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), Font.PLAIN, axis.getTicksSettings().getTickLabelsFontSize()));

        TickProvider tickProvider = getTickProvider(g, area);
        List<Tick> ticks = tickProvider.getTicks();

        if (axis.getTicksSettings().isTicksVisible()) {
            size = axis.getTicksSettings().getTickLabelsPadding() + axis.getTicksSettings().getTickSize() + getMaxTickLabelSize(g, ticks, !axis.isHorizontal());
        }

        return size;
    }

    public void draw(Graphics g, Rectangle area, int axisOriginPoint) {
       if(axis.getAxisViewSettings().isVisible()) {
           Font font = g.getFont();
           g.setFont(new Font(font.getFontName(), Font.PLAIN, axis.getTicksSettings().getTickLabelsFontSize()));

           TickProvider tickProvider = getTickProvider(g, area);
           List<Tick> ticks = tickProvider.getTicks();

           if (axis.getGridSettings().isMinorGridVisible()) {
               drawMinorGrid(g, area, ticks);
           }

           if (axis.getGridSettings().isGridVisible()) {
               drawGrid(g, area, ticks);
           }

           if (axis.getTicksSettings().isTicksVisible()) {
               drawTicks(g, area, axisOriginPoint, ticks);
           }

           if (axis.getAxisViewSettings().isAxisLineVisible()) {
               drawAxisLine(g, area, axisOriginPoint);
           }

           drawName(g,area,axisOriginPoint, ticks);

       }
    }

    private void drawAxisLine(Graphics g, Rectangle area, int axisOriginPoint) {
        g.setColor(axis.getAxisViewSettings().getAxisColor());
        Graphics2D g2d = (Graphics2D) g;
        Stroke defaultStroke = g2d.getStroke();
        g2d.setStroke(DashStyle.SOLID.getStroke(axis.getAxisViewSettings().getAxisLineWidth()));
        if (axis.isHorizontal()) {
            g.drawLine(area.x, axisOriginPoint, area.width + area.x, axisOriginPoint);
        } else {
            g.drawLine(axisOriginPoint, area.y, axisOriginPoint, area.y + area.height);
        }
        g2d.setStroke(defaultStroke);
    }

    private TickProvider getTickProvider(Graphics g, Rectangle area) {
        TickProvider tickProvider = axis.getTicksProvider(area);

        if (tickProvider.getTickInterval().isNaN()) {
            tickProvider.setTickPixelInterval(axis.getTicksSettings().getTickPixelInterval());
        }

        List<Tick> ticks = tickProvider.getTicks();

        int maxTickSize = getMaxTickLabelSize(g, ticks, axis.isHorizontal());

        if (ticks.size() > 1) {

            if ((maxTickSize + axis.getTicksSettings().getTickLabelsPadding()) > tickProvider.getTickPixelInterval()) {
                tickProvider.setMinTickPixelInterval(maxTickSize + axis.getTicksSettings().getTickLabelsPadding());
            }
        }

        return tickProvider;

    }

    private void drawName (Graphics g, Rectangle area, int axisOriginPoint, List<Tick> ticks){
        g.setColor(axis.getAxisViewSettings().getAxisColor());
        int maxSize = getMaxTickLabelSize(g, ticks, axis.isHorizontal());
        int namePosition = maxSize + axis.getTicksSettings().getTickSize() / 2 + axis.getTicksSettings().getTickLabelsPadding() * 2;
        if (axis.isHorizontal()){
            int nameWidth = getLabelWidth(g,axis.getName());
            g.drawString(axis.getName(),area.x + area.width / 2 - nameWidth / 2, axisOriginPoint + namePosition);

        }
    }

    private void drawTicks(Graphics g, Rectangle area, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(axis.getAxisViewSettings().getAxisColor());
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
        g.setColor(axis.getGridSettings().getGridColor());
        Graphics2D g2d = (Graphics2D) g;
        Stroke defaultStroke = g2d.getStroke();
        g2d.setStroke(axis.getGridSettings().getGridLineStyle().getStroke(axis.getGridSettings().getGridLineWidth()));
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min < tick.getValue() && tick.getValue() < max) {
                if (axis.isHorizontal()) {
                    g.drawLine(axis.valueToPoint(tick.getValue(), area), area.y, axis.valueToPoint(tick.getValue(), area), area.y + area.height);
                } else {
                    g.drawLine(area.x, axis.valueToPoint(tick.getValue(), area), area.x + area.width, axis.valueToPoint(tick.getValue(), area));
                }
            }
        }
        g2d.setStroke(defaultStroke);

    }

    private void drawMinorGrid(Graphics g, Rectangle area, List<Tick> ticks) {
        if (ticks.size() > 1) {
            g.setColor(axis.getGridSettings().getMinorGridColor());
            Graphics2D g2d = (Graphics2D) g;
            Stroke defaultStroke = g2d.getStroke();
            g2d.setStroke(axis.getGridSettings().getMinorGridLineStyle().getStroke(axis.getGridSettings().getMinorGridLineWidth()));
            double max = axis.getMax();
            double min = axis.getMin();
            double tickInterval = ticks.get(1).getValue() - ticks.get(0).getValue();
            double minorTickInterval = tickInterval / axis.getGridSettings().getMinorGridDivider();

            double minorTickValue = ticks.get(0).getValue();
            while (minorTickValue < max) {
                if (min < minorTickValue) {
                    if (axis.isHorizontal()) {
                        g.drawLine(axis.valueToPoint(minorTickValue, area), area.y, axis.valueToPoint(minorTickValue, area), area.y + area.height);
                    } else {
                        g.drawLine(area.x, axis.valueToPoint(minorTickValue, area), area.x + area.width, axis.valueToPoint(minorTickValue, area));
                    }
                }
                minorTickValue += minorTickInterval;
            }
            g2d.setStroke(defaultStroke);
        }

    }

    private void drawTick(Graphics g, int axisOriginPoint, int tickPoint) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke defaultStroke = g2d.getStroke();
        g2d.setStroke(DashStyle.SOLID.getStroke(axis.getTicksSettings().getTicksWidth()));
        if (axis.isHorizontal()) {
            g.drawLine(tickPoint, axisOriginPoint + axis.getTicksSettings().getTickSize() / 2, tickPoint, axisOriginPoint - axis.getTicksSettings().getTickSize() / 2);
        } else {
            g.drawLine(axisOriginPoint - axis.getTicksSettings().getTickSize() / 2, tickPoint, axisOriginPoint + axis.getTicksSettings().getTickSize() / 2, tickPoint);
        }
        g2d.setStroke(defaultStroke);
    }

    private void drawLabel(Graphics g, Rectangle area, int axisOriginPoint, int tickPoint, String label) {
        int labelPadding = axis.getTicksSettings().getTickLabelsPadding();
        //HORIZONTAL position
        if (axis.isHorizontal()) {
            // TOP axis position
            if (axis.isOpposite()) {
                g.drawString(label, tickPoint - getLabelWidth(g, label) / 2, axisOriginPoint - axis.getTicksSettings().getTickSize() / 2 - labelPadding);
            } else { //BOTTOM axis position
                g.drawString(label, tickPoint - getLabelWidth(g, label) / 2, axisOriginPoint + axis.getTicksSettings().getTickSize() / 2 + labelPadding + getLabelHeight(g, label));
            }

        } else { //VERTICAL position
            //RIGTH axis position
            if (axis.isOpposite()) {
                g.drawString(label, axisOriginPoint + axis.getTicksSettings().getTickSize() / 2 + labelPadding, tickPoint + getLabelHeight(g, label) / 2);

            } else { //LEFT axis position
                g.drawString(label, axisOriginPoint - axis.getTicksSettings().getTickSize() / 2 - getLabelWidth(g, label) - labelPadding, tickPoint + getLabelHeight(g, label) / 2);
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
