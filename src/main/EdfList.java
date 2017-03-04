package main;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfList implements DataProvider {
    private File edfFile;
    private int [] buffer;
    private com.biorecorder.edflib.src.com.biorecorder.edflib.EdfReader edfReader;
    private int channelNumber=0;
    private int bufferSize=1024*2;
    private long samplePointer;

    public EdfList(File edfFile) {
        this.edfFile = edfFile;
        edfReader = new com.biorecorder.edflib.src.com.biorecorder.edflib.EdfReader(edfFile);
        buffer = new int[bufferSize];
        buffer = fullBuffer(samplePointer);

    }

    @Override
    public long size() {
        return edfReader.getNumberOfSamples(channelNumber);
    }

    @Override
    public double get(long index) {

        if(index>samplePointer || index>samplePointer+bufferSize){
            buffer = fullBuffer(index);
        }

        return buffer[index-samplePointer];
    }

    private int[] fullBuffer(long index){
        edfReader.setSamplePosition(index);
        samplePointer = index;
        return edfReader.readDigitalSamples(channelNumber, bufferSize);
        }


}
