package main;

import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfList {
    private File edfFile;
    private EdfReader edfReader;
    private int bufferSize=1024*2;
    private ArrayList<Long> samplePointersList  =  new ArrayList<Long>();
    private ArrayList<int[]> buffersList = new ArrayList<int[]>();

    public EdfList(File edfFile) throws IOException, HeaderParsingException {
        this.edfFile = edfFile;
        edfReader = new EdfReader(edfFile);
    }

    public TimeSeries getChannelSeries(int channelNumber){
        buffersList.add(new int[bufferSize]);
        samplePointersList.add(new Long(0));

        fullBuffer (samplePointersList.get(channelNumber), channelNumber, buffersList.get(channelNumber));

        return new TimeSeries() {
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
                if(index <= samplePointersList.get(channelNumber) || index >= samplePointersList.get(channelNumber)+bufferSize){
                    fullBuffer(index, channelNumber, buffersList.get(channelNumber));
                }

                return buffersList.get(channelNumber)[(int) (index-samplePointersList.get(channelNumber))];
            }

            @Override
            public long start() {
                return edfReader.getHeaderInfo().getRecordingStartTime();
            }

            @Override
            public double sampleRate() {
                return edfReader.getHeaderInfo().getSignalConfig(channelNumber).getNumberOfSamplesInEachDataRecord() / edfReader.getHeaderInfo().getDurationOfDataRecord();
            }

        };

    }


    private void fullBuffer(long index, int channelNumber, int[] bufferArray) {
        edfReader.setSamplePosition(channelNumber, index);
        samplePointersList.set(channelNumber,index);
        try {
            edfReader.readDigitalSamples(channelNumber, bufferArray, 0, bufferSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
