package main.chart;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 26.06.17.
 */
public class PeriodicData extends AbstractList<DataItem>{
    private double startValue;
    private List<Double> data;
    private double samplesPerUnit;

    public PeriodicData(double startValue, double samplesPerUnit) {
           this(startValue, samplesPerUnit, new ArrayList<Double>());
    }

    public PeriodicData(double startValue, double samplesPerUnit, List<Double> data) {
        this.startValue = startValue;
        this.samplesPerUnit = samplesPerUnit;
        this.data = data;

    }

    public void addData(double dataValue){
        data.add(dataValue);
    }

    public int getIndex (double value){
        return (int)((value - startValue) * samplesPerUnit);
    }



    @Override
    public DataItem get(int index) {
        return new DataItem() {
            @Override
            public double getX() {
                return startValue + index / samplesPerUnit;
            }

            @Override
            public double getY() {
                return data.get(index);
            }
        };
    }

    @Override
    public int size() {
        return data.size();
    }
}
