package main.data;


import gnu.trove.list.array.TIntArrayList;


/**

 */
public class DataList  implements DataSeries {
    private TIntArrayList intArrayList;
    private Scaling scaling = new ScalingImpl();


    public DataList() {
        intArrayList = new TIntArrayList();
    }
    public DataList(int[] array) {
        intArrayList = TIntArrayList.wrap(array);
    }

    public void add(int value) {
        intArrayList.add(value);
    }

    public void set(int index, int value) {
        intArrayList.set(index, value);
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
    }

    @Override
    public long size() {
        return intArrayList.size();
    }

    @Override
    public int get(long index) {
        return intArrayList.get((int)index);
    }

    @Override
    public Scaling getScaling() {
        return scaling;
    }

    @Override
    public double start() {
        if(scaling != null) {
            return scaling.getStart() / 1000.0;
        }
        return 0;
    }

    @Override
    public double sampleRate() {
        if(scaling != null) {
            return 1.0 / scaling.getSamplingInterval();
        }
        return 1;
    }
}
