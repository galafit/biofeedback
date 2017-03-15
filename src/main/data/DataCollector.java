package main.data;

/**
 * Class is designed to collect/store/cache a computed input main.data  and to give quick access to them.
 * Input main.data could be a filter, function and so on
 */

public class DataCollector implements DataSeries {
    protected DataSeries inputData;
    protected DataList outputData;

    public DataCollector(DataSeries inputData) {
        this.inputData = inputData;
        outputData = new DataList();
        collectData();
    }


    private void collectData() {
        if (outputData.size()  < inputData.size()) {
            for (long i = outputData.size(); i < inputData.size(); i++) {
                outputData.add(inputData.get(i));
            }
        }
    }

    @Override
    public int get(long index) {
        collectData();
        return outputData.get(index);
    }

    @Override
    public double start() {
        return (long) inputData.getScaling().getStart();
    }

    @Override
    public double sampleRate() {
        return 1.0/ inputData.getScaling().getSamplingInterval();
    }

    @Override
    public long size() {
        collectData();
        return outputData.size();
    }

    @Override
    public Scaling getScaling() {
        return inputData.getScaling();
    }
}
