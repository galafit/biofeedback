package main.chart;

/**
 * Created by hdablin on 30.06.17.
 */
public class DefaultScrollModel implements ScrollModel {
    private int scrollPosition = 0;
    private int scrollWidth = 10;
    private int min = 0;
    private int max = 100;

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getScrollPosition() {
        return scrollPosition;
    }

    @Override
    public int getScrollWidth() {
        return scrollWidth;
    }

    @Override
    public void setMin(int min) {
        this.min = min;
    }

    @Override
    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    @Override
    public void setScrollWidth(int scrollWidth) {
        this.scrollWidth = scrollWidth;
    }
}
