package main.chart.axis;

import main.chart.TickProvider;

import java.awt.*;

/**
 * Created by hdablin on 16.04.17.
 */
public class LinearAxis extends Axis {
    public LinearAxis(boolean isHorizontal) {
        this.axisData = new LinearAxisData(isHorizontal);
        this.axisPainter = new AxisPainter(axisData, new AxisViewSettings());
    }
}

