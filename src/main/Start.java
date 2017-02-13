package main;


import java.io.File;

/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
        play(10, 1);
        File file = new File(System.getProperty("user.dir"), "cat_purr.wav");
        StdAudio.playFile(file, 1);
        StdAudio.playFile(file, 3);

    }


    // create a pure tone of the given frequency for the given duration
    public static double[] tone(double hz, double duration) {
        int n = (int) (StdAudio.SAMPLE_RATE * duration);
        System.out.println("StdAudio.SAMPLE_RATE "+StdAudio.SAMPLE_RATE);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        }
        return a;
    }

    // create a pure tone of the given frequency for the given duration
    public static double[] rectangle(double hz, double duration) {
        int n = (int) (StdAudio.SAMPLE_RATE * duration);
        System.out.println("StdAudio.SAMPLE_RATE "+StdAudio.SAMPLE_RATE);
        double[] a = new double[n+1];

        for (int i = 0; i <= n; i++) {
            a[i] = Math.signum(Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE));
            a[i] = Math.sin((a[i]*2 * Math.PI * i) / n );
            System.out.println(a[i]);
        }
        return a;
    }

    public static void play(double hz, double duration) {

        // frequency
        // number of seconds to play the note
        // create the array
        // double[] a = tone(hz, duration);

        double[] a = rectangle(hz, duration);
        StdAudio.play(a);
    }

}


