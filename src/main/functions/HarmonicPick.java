package main.functions;

/**
 * Created by gala on 20/03/17.
 */
public class HarmonicPick extends Harmonic {
    private double pct;

    public HarmonicPick(double freq, double pct) {
        super(freq / 2, 0, 2);
        this.pct = pct;
    }

    @Override
    protected double base(double x) {
        if(x < pct) {
            return Math.sin(Math.PI * x / pct);
        }
        if(1 < x && x < 1 + pct) {
            return  - Math.sin(Math.PI * (x-1) / pct);
        }
        return 0;
    }
}
