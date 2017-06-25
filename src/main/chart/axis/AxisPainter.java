package main.chart.axis;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
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

    public int getAxisWidth(Graphics2D g, Rectangle area) {
        if (!isAxisVisible()) {
            return 0;
        }
        int size = 0;
        if(isAxisLineVisible()) {
            size += getAxisLineWidth();
        }

        List<Tick> ticks = getTicks(g, area);

        if (isTicksVisible()) {
            size += getTicksSize();
        }
        if(isLabelsVisible()) {
            int labelsSize = 0;
            if(axis.isHorizontal()) {
                labelsSize = getMaxTickLabelsHeight(g, getLabelFont(), ticks);
            } else {
                labelsSize = getMaxTickLabelsWidth(g, getLabelFont(), ticks);
            }
            size += getLabelPadding() + labelsSize;
        }

        if (isAxisNameVisible()) {
            size = size + getNamePadding() + getStringHeight(g, getName(), getNameFont());
        }

        return size;
    }


    public void draw(Graphics2D g, Rectangle area, int axisOriginPoint) {
        if (isAxisVisible()) {

            List<Tick> ticks = getTicks(g, area);

            if (isMinorGridVisible()) {
                drawMinorGrid(g, area, ticks);
            }

            if (isGridVisible()) {
                drawGrid(g, area, ticks);
            }

            if (isTicksVisible()) {
                drawTicks(g, area, axisOriginPoint, ticks);
            }

            if (isLabelsVisible()) {
                drawLabels(g, area, axisOriginPoint, ticks);
            }

            if (isAxisLineVisible()) {
                drawAxisLine(g, area, axisOriginPoint);
            }

            if(isAxisNameVisible()) {
                drawName(g, area, axisOriginPoint, ticks);
            }
        }
    }

    public List<Tick> getTicks(Graphics2D g, Rectangle area) {
        List<Tick> ticks =  axis.getTicks(area);
        if(ticks.size() > 1) {
            int labelsSize = 0;
            if(axis.isHorizontal()) {
                labelsSize = getMaxTickLabelsWidth(g, getLabelFont(), ticks);
            } else {
                labelsSize = getMaxTickLabelsHeight(g, getLabelFont(), ticks);
            }
            double ticksInterval = ticks.get(1).getValue() - ticks.get(0).getValue();
            int tickPixelInterval = (int)(ticksInterval * axis.pointsPerUnit(area));
            // min space between labels = 2 symbols size (roughly)
            int labelSpace = 2 * axis.getTicksSettings().getTickLabelsFontSize();
            int requiredSpace = labelsSize + labelSpace;

            if (requiredSpace > tickPixelInterval) {
                ticks = axis.getTicks(area, requiredSpace);
            }
        }
        return ticks;

    }

    private void drawAxisLine(Graphics2D g, Rectangle area, int axisOriginPoint) {
        g.setColor(getAxisColor());
        Stroke defaultStroke = g.getStroke();
        g.setStroke(LineStyle.SOLID.getStroke(getAxisLineWidth()));
        if (axis.isHorizontal()) {
            if(axis.isOpposite()) {
               axisOriginPoint -= getAxisLineWidth()/2;
            }
            else {
                axisOriginPoint += getAxisLineWidth()/2;
            }
            g.drawLine(area.x, axisOriginPoint, area.width + area.x, axisOriginPoint);
        } else {
            if(axis.isOpposite()) {
                axisOriginPoint += getAxisLineWidth()/2;
            }
            else {
                axisOriginPoint -= getAxisLineWidth()/2;
            }
            g.drawLine(axisOriginPoint, area.y, axisOriginPoint, area.y + area.height);
        }
        g.setStroke(defaultStroke);
    }

    private void drawName(Graphics2D g, Rectangle area, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(getAxisColor());
        g.setFont(getNameFont());

        int namePosition = 0;
        if(isAxisLineVisible()) {
            namePosition += getAxisLineWidth();
        }
        if (isTicksVisible()) {
            namePosition += getTicksSize();
        }
        if(isLabelsVisible()) {
            int labelsSize = 0;
            if(axis.isHorizontal()) {
                labelsSize = getMaxTickLabelsHeight(g, getLabelFont(), ticks);
            } else {
                labelsSize = getMaxTickLabelsWidth(g, getLabelFont(), ticks);
            }
            namePosition += getLabelPadding() + labelsSize;
        }
        namePosition += getNamePadding();
        int nameWidth = getStringWidth(g, axis.getName(), getNameFont());
        if (axis.isHorizontal()) {
            if (axis.isOpposite()) {
                g.drawString(axis.getName(), area.x + area.width / 2 - nameWidth / 2, axisOriginPoint - namePosition);
            } else {
                namePosition += getStringHeight(g, axis.getName(), getNameFont());
                g.drawString(axis.getName(), area.x + area.width / 2 - nameWidth / 2, axisOriginPoint + namePosition);
            }
        } else {
            if (!axis.isOpposite()) {
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.toRadians(-90));
                AffineTransform defaultTransform = g.getTransform();
                g.setTransform(transform);
                int nameX = axisOriginPoint - namePosition;
                int nameY = area.y + area.height / 2 + nameWidth / 2;
                g.drawString(axis.getName(), -nameY, nameX);
                g.setTransform(defaultTransform);
            } else {
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.toRadians(+90));
                AffineTransform defaultTransform = g.getTransform();
                g.setTransform(transform);
                int nameX = axisOriginPoint + namePosition;
                int nameY = area.y + area.height / 2 - nameWidth / 2;
                g.drawString(axis.getName(), nameY,  - nameX);
                g.setTransform(defaultTransform);
            }
        }
    }

    private void drawTicks(Graphics2D g, Rectangle area, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(getAxisColor());
        Font labelFont = getLabelFont();
        g.setFont(labelFont);
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min <= tick.getValue() && tick.getValue() <= max) {
                int tickPoint = axis.valueToPoint(tick.getValue(), area);
                drawTick(g, axisOriginPoint, tickPoint);
            }
        }
    }

    private void drawLabels(Graphics2D g, Rectangle area, int axisOriginPoint, List<Tick> ticks) {
        g.setColor(getAxisColor());
        Font labelFont = getLabelFont();
        g.setFont(labelFont);
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min <= tick.getValue() && tick.getValue() <= max) {
                int tickPoint = axis.valueToPoint(tick.getValue(), area);
                drawLabel(g, labelFont, axisOriginPoint, tickPoint, tick.getLabel());
            }
        }
    }

    private void drawGrid(Graphics2D g, Rectangle area, List<Tick> ticks) {
        LineStyle gridLineStile = axis.getGridSettings().getGridLineStyle();
        int gridWidth = axis.getGridSettings().getGridLineWidth();
        g.setColor(axis.getGridSettings().getGridColor());
        Stroke defaultStroke = g.getStroke();
        g.setStroke(gridLineStile.getStroke(gridWidth));
        double max = axis.getMax();
        double min = axis.getMin();
        for (Tick tick : ticks) {
            if (min < tick.getValue() && tick.getValue() < max) {
                if (axis.isHorizontal()) {
                    g.drawLine(axis.valueToPoint(tick.getValue(), area), area.y + 1, axis.valueToPoint(tick.getValue(), area), area.y  + area.height - 1);
                } else {
                    g.drawLine(area.x + 1, axis.valueToPoint(tick.getValue(), area), area.x + area.width - 1, axis.valueToPoint(tick.getValue(), area));
                }
            }
        }
        g.setStroke(defaultStroke);

    }

    private void drawMinorGrid(Graphics2D g, Rectangle area, List<Tick> ticks) {
        if (ticks.size() > 1) {
            LineStyle gridLineStile = axis.getGridSettings().getMinorGridLineStyle();
            int gridWidth = axis.getGridSettings().getMinorGridLineWidth();
            int gridDivider = axis.getGridSettings().getMinorGridDivider();

            g.setColor(axis.getGridSettings().getMinorGridColor());
            Stroke defaultStroke = g.getStroke();
            g.setStroke(gridLineStile.getStroke(gridWidth));
            double max = axis.getMax();
            double min = axis.getMin();
            double tickInterval = ticks.get(1).getValue() - ticks.get(0).getValue();
            double minorTickInterval = tickInterval / gridDivider;

            double minorTickValue = ticks.get(0).getValue();
            while (minorTickValue < max) {
                if (min < minorTickValue) {
                    if (axis.isHorizontal()) {
                        g.drawLine(axis.valueToPoint(minorTickValue, area), area.y + 1, axis.valueToPoint(minorTickValue, area), area.y + area.height - 1);
                    } else {
                        g.drawLine(area.x + 1, axis.valueToPoint(minorTickValue, area), area.x + area.width - 1, axis.valueToPoint(minorTickValue, area));
                    }
                }
                minorTickValue += minorTickInterval;
            }
            g.setStroke(defaultStroke);
        }
    }

    private void drawTick(Graphics2D g, int axisOriginPoint, int tickPoint) {
        Stroke defaultStroke = g.getStroke();
        int tickWidth = axis.getTicksSettings().getTicksWidth();
        g.setStroke(LineStyle.SOLID.getStroke(tickWidth));

        int tickEnd;
        if (axis.isHorizontal()) {
            if(axis.isOpposite()) {
                axisOriginPoint -= getAxisLineWidth();
                 tickEnd = axisOriginPoint - getTicksSize();
            } else {
                axisOriginPoint += getAxisLineWidth();
                tickEnd = axisOriginPoint + getTicksSize();
            }
            g.drawLine(tickPoint, axisOriginPoint, tickPoint, tickEnd);
        } else {
            if(axis.isOpposite()) {
                axisOriginPoint += getAxisLineWidth();
                tickEnd = axisOriginPoint + getTicksSize();
            } else {
                axisOriginPoint -= getAxisLineWidth();
                tickEnd = axisOriginPoint - getTicksSize();
            }
            g.drawLine(axisOriginPoint, tickPoint, tickEnd, tickPoint);
        }
        g.setStroke(defaultStroke);
    }

    private void drawLabel(Graphics2D g, Font font, int axisOriginPoint, int tickPoint, String label) {
        //HORIZONTAL position
        int labelPosition = 0;
        if(isAxisLineVisible()) {
            labelPosition += getAxisLineWidth();
        }
        if(isTicksVisible()) {
            labelPosition += getTicksSize();
        }
        labelPosition += getLabelPadding();
        if (axis.isHorizontal()) {
            // TOP axis position
            if (axis.isOpposite()) {
                g.drawString(label, tickPoint - getStringWidth(g,  label, font) / 2, axisOriginPoint - labelPosition);
            } else { //BOTTOM axis position
                labelPosition += getStringHeight(g, label, font);
                g.drawString(label, tickPoint - getStringWidth(g, label, font) / 2, axisOriginPoint + labelPosition);
            }

        } else { //VERTICAL position
            //RIGTH axis position
            int labelY = tickPoint + getStringHeight(g, label, font) / 2 - 1;
            if (axis.isOpposite()) {
                g.drawString(label, axisOriginPoint + labelPosition, labelY);
            } else { //LEFT axis position
                labelPosition += getStringWidth(g, label, font) + 1;
                g.drawString(label, axisOriginPoint - labelPosition, labelY);
            }
        }
    }

    private Rectangle2D getStringBounds(Graphics2D g, String string, Font font) {
        TextLayout layout = new TextLayout(string, font, g.getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return labelBounds;
    }

    private int getStringWidth(Graphics2D g, String label, Font font) {
        return (int)Math.round(getStringBounds(g, label, font).getWidth());
    }

    private int getStringHeight(Graphics2D g, String label, Font font) {
         //return (int)(g.getFontMetrics().getStringBounds(label,(Graphics2D)(g)).getHeight());
        return (int)Math.round(getStringBounds(g, label, font).getHeight());
    }


    private int getLabelHeightApprox(Graphics g) {
        return g.getFontMetrics().getHeight();
    }

    private int getMaxTickLabelsWidth(Graphics2D g, Font font, List<Tick> ticks) {
        int maxSize = 0;
        for (Tick tick : ticks) {
            maxSize = Math.max(maxSize, getStringWidth(g, tick.getLabel(), font));
        }
        return maxSize;
    }

    private int getMaxTickLabelsHeight(Graphics2D g, Font font, List<Tick> ticks) {
        int maxSize = 0;
        for (Tick tick : ticks) {
            maxSize = Math.max(maxSize, getStringHeight(g, tick.getLabel(), font));
        }
        return maxSize;
    }

    private Font getNameFont() {
        String fontName = axis.getAxisViewSettings().getNameFontName();
        return new Font(fontName, Font.PLAIN, axis.getAxisViewSettings().getNameFontSize());
    }

    private Font getLabelFont() {
        String fontName = axis.getTicksSettings().getTickLabelFontName();
        return new Font(fontName, Font.PLAIN, axis.getTicksSettings().getTickLabelsFontSize());
    }

    private int getTicksSize() {
        return axis.getTicksSettings().getTickSize();
    }

    private int getAxisLineWidth() {
        return axis.getAxisViewSettings().getAxisLineWidth();
    }

    private int getNamePadding() {
        return axis.getAxisViewSettings().getNamePadding();
    }

    private int getLabelPadding() {
        return axis.getTicksSettings().getTickLabelsPadding();
    }

    private boolean isAxisVisible() {
        return axis.getAxisViewSettings().isVisible();
    }

    private boolean isAxisLineVisible() {
        return axis.getAxisViewSettings().isAxisLineVisible();
    }

    private boolean isAxisNameVisible() {
        return axis.getAxisViewSettings().isNameVisible();
    }

    private boolean isGridVisible() {
        return axis.getGridSettings().isGridVisible();
    }

    private boolean isMinorGridVisible() {
        return axis.getGridSettings().isMinorGridVisible();
    }

    private boolean isTicksVisible() {
        return axis.getTicksSettings().isTickMarkVisible();
    }

    private boolean isLabelsVisible() {
        return axis.getTicksSettings().isTickLabelsVisible();
    }

    private String getName() {
        return axis.getName();
    }

    private Color getAxisColor() {
        return axis.getAxisViewSettings().getAxisColor();
    }



}
