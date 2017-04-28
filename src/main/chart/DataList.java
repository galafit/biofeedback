package main.chart;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public interface DataList extends List<DataItem> {
    public double getXmin();

    public double getXmax();

    public double getYmin();

    public double getYmax();
}
