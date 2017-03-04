package main;

/**
 * Created by hdablin on 02.03.17.
 */
public class ArrayFunction implements Function {
    double[] args;
    double sampleRate;

    public ArrayFunction(double[] args, double sampleRate) {
        this.args = args;
        this.sampleRate = sampleRate;
    }

    @Override
    public double value(double x) {
        x = x * sampleRate;
        if (x<0);
        if (x>args.length){
            return 0;
        }
        return args[(int)x];
    }
}
