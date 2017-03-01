package main.tmp;

import main.Function;

/**
 * Created by gala on 28/02/17.
 */
public class Harmonic1 implements Function {
    private double freq;
    private double pct;

    public Harmonic1(double freq, double pct) {
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
        if(Math.abs(x) < pct) {
            return x/pct;
        }
        if(x > (1-pct)) {
            return (1-x)/pct;
        }
        if(x < -(1-pct)) {
            return -(1+x)/pct;
        }

        return  Math.signum(x);
    }
}
