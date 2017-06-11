package main.chart.axis;

import java.util.List;

/**
 * Created by hdablin on 15.04.17.
 */
public interface TickProvider {
    public Double getTickInterval();
    public Double getTickPixelInterval();
    public void setTickInterval(double tickInterval);
    public void setTickPixelInterval(double tickPixelInterval);
    public void setMinTickPixelInterval(double minTickPixelInterval);
    public List<Tick> getTicks();
}
