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
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        double num = 100;
        NormalizedNumber value = new NormalizedNumber(num);
        System.out.println(num + " digits = " + value.getDigits() + " power = " + value.getPower());

        num = 0.003234234;
        value = new NormalizedNumber(num);
        System.out.println(num + " digits = " + value.getDigits() + " power = " + value.getPower());

        num = 7000.15;
        value = new NormalizedNumber(num);
        System.out.println(num + " digits = " + value.getDigits() + " power = " + value.getPower());

        num = -20;
        value = new NormalizedNumber(num);
        System.out.println(num + " digits = " + value.getDigits() + " power = " + value.getPower());

        num = 0;
        value = new NormalizedNumber(num);
        System.out.println(num + " digits = " + value.getDigits() + " power = " + value.getPower());

    }
}
