package main.chart;

/**
 * Created by hdablin on 30.06.17.
 */
public interface ScrollModel {
    public long getMin();
    public long getMax();
    public long getViewportWidth();
    public long getViewportPosition();
    public void setViewportPosition(long newPosition);
}
