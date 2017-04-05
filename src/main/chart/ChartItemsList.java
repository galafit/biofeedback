package main.chart;

import java.util.AbstractList;

/**
 * Created by hdablin on 05.04.17.
 */
public class ChartItemsList extends AbstractList<ChartItem>{
    double[] itemsArray;

    public ChartItemsList(double[] itemsArray) {
        this.itemsArray = itemsArray;
    }

    @Override
    public int size(){
        return itemsArray.length;
    }

    @Override
    public ChartItem get(int index){
        return new ChartItem() {
            @Override
            public double getX() {
                return index;
            }

            @Override
            public double getY() {
                return itemsArray[index];
            }
        };
    }
}
