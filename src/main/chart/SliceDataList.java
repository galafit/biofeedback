package main.chart;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 26.06.17.
 */
public class SliceDataList extends AbstractList<DataItem> implements DataList{
   private PeriodicData periodicData;
   private int window;
   private int startIndex;
   private Double yMin = null;
   private Double yMax = null;

    public SliceDataList(PeriodicData periodicData) {
        this.periodicData = periodicData;
        window = periodicData.size() - 1;
    }

    public void setRange(double startValue, double endValue){
       startIndex = getMinIndex(startValue);
       window = getMaxIndex(endValue) - startIndex;
       setMinMax();
   }

   private void setMinMax(){
       double yMin = Double.MAX_VALUE;
       double yMax = -Double.MAX_VALUE;
       for (int i = startIndex; i <= startIndex + window; i++) {
           yMin = Math.min(yMin, periodicData.get(i).getY());
           yMax = Math.max (yMax, periodicData.get(i).getY());
       }
       this.yMin = yMin;
       this.yMax = yMax;
   }

   public int getMinIndex(double startValue){
       int index = periodicData.getIndex(startValue) - 1;
       return Math.max(0,index);
   }

   public int getMaxIndex(double endValue){
       int index = periodicData.getIndex(endValue) + 1;
       return Math.min(index, periodicData.size() - 1);
   }

   public int getFullSize() {
       return periodicData.size();
   }

    @Override
    public DataItem get(int index) {
        return periodicData.get(startIndex + index);
    }

    @Override
    public int size() {
        return window + 1;
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
       if(yMin == null) {
           setMinMax();
       }
       return yMin;
    }

    @Override
    public Double getYmax() {
        if(yMax == null) {
            setMinMax();
        }
        return yMax;
    }
}
