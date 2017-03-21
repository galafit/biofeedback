package main.functions;

/**
 * Created by gala on 20/03/17.
 */
public class HarmonicPick extends Harmonic {
    private Function pctFunction;
    double pct;


    public HarmonicPick(double freq, double pct) {
        super(freq / 2, 0, 2);
        this.pctFunction = (x) -> {
            return pct;
        };
    }

    public HarmonicPick(double freq, Function pctFunction) {
        super(freq / 2, 0, 2);
        this.pctFunction = pctFunction;
    }

    public HarmonicPick(Function freq, double pct) {
        super(x -> {
            return freq.value(x) / 2;
        }, 0, 2);
        this.pctFunction = (x) -> {
            return pct;
        };
    }

    public HarmonicPick(Function freq, Function pctFunction) {
        super(x -> {
            return freq.value(x) / 2;
        }, 0, 2);
        this.pctFunction = pctFunction;
    }

    @Override
    public double value(double x) {
        pct = pctFunction.value(x);
        return super.value(x);
    }

    @Override
    protected double base(double x) {
        if (x < pct) {
            return Math.sin(Math.PI * x / pct);
        }
        if (1 < x && x < 1 + pct) {
            return -Math.sin(Math.PI * (x - 1) / pct);
        }
        return 0;
    }
}
