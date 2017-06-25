package main.chart;

import java.awt.*;

/**
 * Created by hdablin on 25.06.17.
 */
public class Preview extends Chart {
    private int cursorPosition;
    private Color cursorColor = Color.RED;
    private int cursorWidth = 5;


    @Override
    void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setColor(cursorColor);
        g2d.drawRect(graphArea.x + cursorPosition, graphArea.y, cursorWidth,  graphArea.height);
    }

    public void setCursorPosition(int cursorPosition){
        this.cursorPosition = cursorPosition;
    }




}
