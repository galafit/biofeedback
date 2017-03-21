package main.functions;

import org.apache.commons.math3.stat.Frequency;

/**
 * Created by gala on 20/03/17.
 */
public abstract class Harmonic implements Function {
    protected Function frequency;
    protected double start;
    protected double end;

    public Harmonic(Function freq, double start, double end) {
        this.frequency = freq;
        this.start = start;
        this.end = end;
    }

    public Harmonic(double freq, double start, double end) {
        this.start = start;
        this.end = end;
        frequency = x -> {return freq;};
    }


    public double value(double x) {
        double freq = frequency.value(x);
        x = x * freq;
        long x_int = (long) x;
        double x_double = x - x_int;
        double t = x_double * (end - start) + start;
        return base(t);
    }


    protected abstract double base(double x);

}