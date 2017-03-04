package main;

/**
 * Created by hdablin on 04.03.17.
 */
public interface DataProvider {
    long size();
    double get(long index);

}
