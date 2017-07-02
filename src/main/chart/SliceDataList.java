package main.chart;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 26.06.17.
 */
public class SliceDataList extends AbstractList<DataItem> implements DataList{
   private PeriodicData periodicData;
   private double window = 0;
   private double start;
   private double yMin;
   private double yMax;

    public SliceDataList(PeriodicData periodicData) {
        this.periodicData = periodicData;
        start = periodicData.get(0).getX();
    }

    public void setStartAndWindow(double start, double window){
       this.start = start;
       this.window = window;

       int minIndex = getMinIndex();
       int maxIndex = getMaxIndex();
       setMinMax(minIndex, maxIndex);

   }

   private void setMinMax(int minIndex, int maxIndex){
       double yMin = Double.MAX_VALUE;
       double yMax = -Double.MAX_VALUE;
       for (int i = minIndex; i <= maxIndex; i++) {
           yMin = Math.min(yMin, periodicData.get(i).getY());
           yMax = Math.max (yMax, periodicData.get(i).getY());
       }
       this.yMin = yMin;
       this.yMax = yMax;
   }

   public int getMinIndex(){
       int index = periodicData.getIndex(start) - 1;
       return Math.max(0,index);
   }

   public int getMaxIndex(){
       int index = periodicData.getIndex(start + window) + 1;
       return Math.min(index, periodicData.size() - 1);
   }

    @Override
    public DataItem get(int index) {
        return periodicData.get(getMinIndex() + index);
    }

    @Override
    public int size() {
        return getMaxIndex() - getMinIndex();
    }

    @Override
    public Double getXmin() {
        return periodicData.get(0).getX();
    }

    @Override
    public Double getXmax() {
        return periodicData.get(periodicData.size() - 1).getX();
    }

    @Override
    public Double getYmin() {
        return yMin;
    }

    @Override
    public Double getYmax() {
        return yMax;
    }
}
