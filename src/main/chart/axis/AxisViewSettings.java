package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 13.04.17.
 */
public class AxisViewSettings {

    private Color axisColor = Color.GRAY;
    private int axisLineWidth = 1;
    private boolean isVisible = true;

    public boolean isAxisLineVisible() {
        return (axisLineWidth > 0) ? true : false;
    }


    public Color getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public int getAxisLineWidth() {
        return axisLineWidth;
    }

    public void setAxisLineWidth(int axisLineWidth) {
        this.axisLineWidth = axisLineWidth;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
