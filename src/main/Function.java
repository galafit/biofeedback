package main;

/**
 * Created by recovery on 13.02.2017.
 */
public interface Function {
    double value (double x);

    static Function sin(double freq) {
        Function sin = (x) -> Math.sin(2 * Math.PI * x * freq);
        return sin;
    }
}
