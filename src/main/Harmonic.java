package main;

/**
 * Created by gala on 02/03/17.
 */
public class Harmonic implements Function {
    private double freq;
    private double pct;

    public Harmonic(double freq, double pct) {
        this.freq = freq;
        this.pct = pct;
    }

    @Override
    public double value(double x) {
        x = x * 2 * freq;
        int x_int = (int) x;
        double x_double = x - x_int;

        double t = x_int % 2 - 1 + x_double ;
        return base(t);
    }

    private double base(double x) {
        double y = 0;
        if (x > 0) {
            y = (Math.min(Math.min((x / pct), 1), ((1 - x) / pct)));
        }
        if (x < 0) {
            y = -(Math.min(Math.min(((-x) / pct), 1), ((1 + x) / pct)));
        }

        return y;
    }

}
