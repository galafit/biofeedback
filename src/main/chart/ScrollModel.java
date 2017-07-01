package main.chart;

/**
 * Created by hdablin on 30.06.17.
 */
public interface ScrollModel {
    public double getMin();
    public double getMax();
    public double getScrollPosition();
    public double getScrollWidth();

    public void setScrollPosition(double scrollPosition);
}
