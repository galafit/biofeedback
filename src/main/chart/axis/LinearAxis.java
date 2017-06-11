package main.chart.axis;

/**
 * Created by hdablin on 16.04.17.
 */
public class LinearAxis extends Axis {
    public LinearAxis() {
        this.axisData = new LinearAxisData();
        this.axisViewSettings = new AxisViewSettings();
        this.axisPainter = new AxisPainter(axisData, this.axisViewSettings);
    }
}

