package main;

/**
 * Created by recovery on 13.02.2017.
 */
public interface Function {
    double value (double x);

    default double[] toArray(double start, double duration, double scaling, double sampleRate) {
        int n = (int) (sampleRate * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = scaling * value((double)i/sampleRate);

        }
        return a;
    }

}


