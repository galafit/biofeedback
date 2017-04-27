package main.chart;

import java.util.AbstractList;

/**
 * Created by hdablin on 05.04.17.
 */
public class DataList extends AbstractList<DataItem>{
    double[] itemsArray;

    public DataList(double[] itemsArray) {
        this.itemsArray = itemsArray;
    }

    @Override
    public int size(){
        return itemsArray.length;
    }

    @Override
    public DataItem get(int index){
        return new DataItem() {
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
