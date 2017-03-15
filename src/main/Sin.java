package main;

/**
 * Created by gala on 13/03/17.
 */
public class Sin implements Function {
    private double freq;

    public Sin(double freq) {
        this.freq = freq;
    }

    @Override
    public double value(double x) {
        return Math.sin(2 * Math.PI * x * freq);
    }
}
