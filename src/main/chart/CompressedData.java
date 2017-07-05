package main.chart;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 05.07.17.
 */
public class CompressedData {
    private PeriodicData data;
    private int compressRatio = 1;
    private List<Double> compressedData;

    public CompressedData(PeriodicData data) {
        this.data = data;
        compress();
    }

    private void compress(){
        compressedData = new ArrayList<Double>();
        List<Double> dataList = data.getDataList();
        double sum = 0;
        int count = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (count < compressRatio){
                sum += dataList.get(i);
                count++;
            }else{
                compressedData.add(sum / compressRatio);
                sum = 0;
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
