package main.functions;

/**
 * Created by gala on 02/03/17.
 */
public class HarmonicRect extends Harmonic {
    private double pct;

    public HarmonicRect(double freq, double pct) {
        super(freq, -1, 1);
        this.pct = pct;
    }


    @Override
    protected double base(double x) {
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
