package main.functions;

import main.data.DataSeries;
import main.data.Scaling;

/**
 * Created by recovery on 13.02.2017.
 */
public interface Function {
    double value (double x);

    default double[] toArray(double start, double sampleRate, double duration) {
        int n = (int) (sampleRate * duration);
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = value(i/sampleRate + start);

        }
        return a;
    }

    default double[] toNormalizedArray(double start, double sampleRate, double duration, double scaling) {
        int n = (int) (sampleRate * duration);
        double[] a = new double[n];
        double max = 0;
        for (int i = 0; i < n; i++) {
            a[i] = value(i/sampleRate + start);
            max = Math.max(max, Math.abs(a[i]));

        }
        if(max > 0) {
            for (int i = 0; i < n; i++) {
                a[i] = a[i] * scaling / max;

            }
        }
        return a;
    }
}


