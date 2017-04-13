package main.chart;

/**
 * Created by hdablin on 11.04.17.
 */
public class NormalizedNumber {
    double digits;
    int power;

    public NormalizedNumber(double value) {
        power = calculatePower(value);
        digits = value/Math.pow(10,power);
    }

    private int calculatePower(double value){

        if (value == 0) {return 0;}

        double power = Math.log10(Math.abs(value));
        int powerInt = (int) power;
        if ((power < 0) && (power != powerInt)) {
            powerInt = powerInt - 1;
        }
        return powerInt;
    }


    public double getDigits() {
        return digits;
    }

    public int getPower() {
        return power;
    }

    /**
     * Unit tests {@code NormalizedNumber}.
     *
     * String.format: https://www.dotnetperls.com/format-java
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String format = "%1$10s :  digits = %2$9s  power = %3$3s";
        double value = 100;
        NormalizedNumber number = new NormalizedNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 0.03234234;
        number = new NormalizedNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 7000.15;
        number = new NormalizedNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = -20;
        number = new NormalizedNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 0;
        number = new NormalizedNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));
    }
}
