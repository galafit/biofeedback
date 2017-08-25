package main.functions;

/**
 * Created by gala on 02/03/17.
 */
public class HarmonicRect extends Harmonic {
    private double pct;
    private double amplitude;

    public HarmonicRect(double freq,  double pct, double amplitude) {
        super(freq, -1, 1);
        this.pct = pct;
        this.amplitude = amplitude;
    }

    public HarmonicRect(double freq,  double pct) {
        this(freq, pct, 1);
    }


    @Override
    protected double base(double x) {
        double y = 0;
        if(pct == 0) {
            y = (x > 0) ? amplitude : - amplitude;
        }
        else {
            y = (x > 0) ? amplitude * (Math.min(Math.min((x / pct), 1), ((1 - x) / pct))) : - amplitude * (Math.min(Math.min(((-x) / pct), 1), ((1 + x) / pct)));
        }

        return y;
    }
}
