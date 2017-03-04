package main;

/**
 * Created by hdablin on 02.03.17.
 */
public class Plus implements Function {
    public Function f1, f2;

    public Plus(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public double value(double x) {
        return f1.value(x) + f2.value(x);
    }

}
