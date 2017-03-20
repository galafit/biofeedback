package main.functions;

/**
 * Created by gala on 20/03/17.
 */
public abstract class Harmonic implements Function {
    protected double freq;
    protected double start;
    protected double end;

    public Harmonic(double freq, double start, double end) {
        this.freq = freq;
        this.start = start;
        this.end = end;
    }


    public double value(double x) {
        x = x * 2 * freq;
        long x_int = (long) x;
        double x_double = x - x_int;
        double shift = start * 2 / (end - start);
        double t = x_int % 2 + shift + x_double ;
        t = t * (end - start) / 2;
        return base(t);
    }


    public double value_0ld(double x) {
        x = x * 2 * freq;
        long x_int = (long) x;
        double x_double = x - x_int;

        double t = x_int % 2 - 1 + x_double ;
        return base(t);
    }


    protected abstract double base(double x);

}