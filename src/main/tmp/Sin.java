package main.tmp;

import main.Function;

/**
 * Created by gala on 28/02/17.
 */
public class Sin extends Function{
    private double freq;

    public Sin(double freq) {
        this.freq = freq;
    }

    @Override
    public double value(double x) {
        return Math.sin(2 * Math.PI * x * freq);
    }
}
