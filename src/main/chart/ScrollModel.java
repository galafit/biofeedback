package main.chart;

/**
 * Created by hdablin on 30.06.17.
 */
public interface ScrollModel {
    public int getMin();
    public int getMax();
    public int getScrollPosition();
    public int getScrollWidth();

    public void setMin(int min);
    public void setMax(int max);
    public void setScrollPosition(int scrollPosition);
    public void setScrollWidth(int scrollWidth);
}
