package main.chart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by hdablin on 02.07.17.
 */
public class ScrollPainter {
    private ScrollModel scrollModel;
    private Rectangle paintingArea;
    private Color cursorColor = Color.RED;

    public ScrollPainter(ScrollModel scrollModel) {
        this.scrollModel = scrollModel;
    }

    public double getScrollPosition(){
        return scrollModel.getScrollPosition();
    }

    public boolean isMouseInsideCursor(int mouseX, int mouseY) {
        if (paintingArea != null) {
            double cursorAreaX = paintingArea.getX() + scrollModel.getScrollPosition();
            double cursorAreaY = paintingArea.getY();
            Rectangle2D cursorArea = new Rectangle2D.Double(cursorAreaX, cursorAreaY, scrollModel.getScrollWidth(), paintingArea.height);
            return cursorArea.contains(mouseX, mouseY);
        }
        return false;
    }



    public void moveCursorPosition(int offset){
        scrollModel.setScrollPosition(scrollModel.getScrollPosition() + offset);
    }

    public void setCursorPosition(int mousePosition) {
        if (paintingArea != null) {
            double scrollPosition = mousePosition;
            if (scrollPosition <= paintingArea.getX()) {
                scrollPosition = 0;
            } else if (scrollPosition >= paintingArea.getX() + paintingArea.getWidth()) {
                scrollPosition = paintingArea.getWidth();
            } else {
                scrollPosition -= paintingArea.getX();
            }
            scrollModel.setScrollPosition(scrollPosition);
        }
    }

    public void draw(Graphics2D g2d, Rectangle area){
        paintingArea = area;
        g2d.setColor(cursorColor);
        double cursorX = area.getX() + scrollModel.getScrollPosition();
        double cursorY = area.getY();
        double cursorHeight = area.height;
        g2d.draw(new Rectangle2D.Double(cursorX, cursorY, scrollModel.getScrollWidth(), cursorHeight));
    }
}
