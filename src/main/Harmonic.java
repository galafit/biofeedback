package main;

/**
 * Created by recovery on 13.02.2017.
 */
public class Harmonic implements Function {
    @Override
    public double value(double x) {

        double y;
        if (x>=0) {
            y = Math.max((Math.min(Math.min(((x) / 0.1), 1), ((2 - x) / 0.1))), 0);
        } else {
            y = Math.max((Math.min(Math.min((((x + 1) + 1) / 0.1), 1), ((1 - (x + 1)) / 0.1))), 0);
        }

        return y;
    }
}
