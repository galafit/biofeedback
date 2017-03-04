package main;

import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;

import java.io.File;
import java.io.IOException;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfList implements TimeSeries {
    private File edfFile;
    private int [] buffer;
    private EdfReader edfReader;
    private int channelNumber=0;
    private int bufferSize=1024*2;
    private long samplePointer;

    public EdfList(File edfFile) throws IOException, HeaderParsingException {
        this.edfFile = edfFile;
        edfReader = new EdfReader(edfFile);
        buffer = new int[bufferSize];
        buffer = fullBuffer(samplePointer);

    }

    @Override
    public long size() {
        try {
            return edfReader.getNumberOfSamples(channelNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int get(long index) {
        if(index <= samplePointer || index >= samplePointer+bufferSize){
            buffer = fullBuffer(index);
        }

        return buffer[(int) (index-samplePointer)];
    }

    @Override
    public long start() {
        return edfReader.getHeaderInfo().getRecordingStartTime();
    }

    @Override
    public double sampleRate() {
           return edfReader.getHeaderInfo().getSignalConfig(channelNumber).getNumberOfSamplesInEachDataRecord() / edfReader.getHeaderInfo().getDurationOfDataRecord();
    }

    private int[] fullBuffer(long index) {
        edfReader.setSamplePosition(channelNumber, index);
        samplePointer = index;
        try {
            return edfReader.readDigitalSamples(channelNumber, bufferSize);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
