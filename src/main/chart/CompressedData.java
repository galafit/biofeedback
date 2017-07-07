package main.chart;

import main.chart.compressors.CompressFunction;
import main.chart.compressors.SumCompression;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 05.07.17.
 */
public class CompressedData {
    private PeriodicData data;
    private int compressRatio = 1;
    private List<Double> compressedData;
    private CompressFunction compressFunction;
    private double[] buffer;

    public CompressedData(PeriodicData data, int compressRatio, CompressFunction compressFunction) {
        this.data = data;
        this.compressRatio = compressRatio;
        this.compressFunction = compressFunction;
        buffer = new double[compressRatio];
        compress();
    }

    public CompressedData(PeriodicData data, int compressRatio){
        this(data, compressRatio, new SumCompression());
    }

    public void update(){
        compress();
    }

    private void compress(){
        compressedData = new ArrayList<Double>();
        List<Double> dataList = data.getDataList();

        int count = 0;
        for (int i = 0; i < dataList.size(); i++) {
            buffer[count] = dataList.get(i);
            count++;
            if (count == compressRatio) {
                compressedData.add(compressFunction.compress(buffer));
                count = 0;
            }
        }
    }

    public DataList getCompressedData() {
         PeriodicData periodicData = new PeriodicData(compressedData, data.getStartValue(),data.getSamplesPerUnit() / compressRatio);
         SliceDataList sliceDataList = new SliceDataList(periodicData);
         sliceDataList.setRange(0,periodicData.size() - 1);
         return sliceDataList;
    }
}
