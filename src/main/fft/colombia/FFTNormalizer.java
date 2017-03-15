package main.fft.colombia;


import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

public class FFTNormalizer implements DataSeries {

    double[] re;
    double[] im;
    double frequency;


    public FFTNormalizer(double[] re, double[] im, double frequency) {
        this.re = re;
        this.im = im;
        this.frequency = frequency;
    }

    private int getN() { // number of main.data points for FFT
        return re.length;
    }

    private double getFrequencyStep() {
        return frequency / getN();
    }

    public long size() {
        return getN()/2;
    }

    public double getAmplitude(int i) {
        double amplitude =  Math.sqrt(re[i] * re[i] + im[i] * im[i])/ getN();
        if(i > 0) {
            amplitude = amplitude * 2;
        }

        return Math.abs(amplitude);
    }

    @Override
    public int get(long index) {
        return (int) getAmplitude((int)index);
    }

    @Override
    public Scaling getScaling() {
        ScalingImpl scaling = new ScalingImpl();
        scaling.setSamplingInterval(getFrequencyStep());
        return scaling;
    }

    @Override
    public double start() {
        return 0;
    }

    @Override
    public double sampleRate() {
        return 1.0/getFrequencyStep();
    }
}

