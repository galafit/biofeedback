package main;


import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.EdfReader1;
import com.biorecorder.edflib.HeaderParsingException;
import com.biorecorder.edflib.base.SignalConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfReader edfReader;
    private int bufferSize=1024*8;
    private List<Long> samplePointersList  =  Collections.synchronizedList(new ArrayList<Long>());
    private List<int[]> buffersList = Collections.synchronizedList(new ArrayList<int[]>());


    public EdfData(File edfFile) throws IOException, HeaderParsingException {
        edfReader = new EdfReader(edfFile);
        edfReader.printHeaderInfo();
    }

    synchronized public DataSeries getChannelSeries(int channelNumber){
        buffersList.add(new int[bufferSize]);
        samplePointersList.add(new Long(0));
        final long  size;
        long size1;
        try {
            size1 = edfReader.getNumberOfSamples(channelNumber);
        } catch (IOException e) {
            e.printStackTrace();
            size1 = 0;
        }


        size = size1;
        fullBuffer (samplePointersList.get(channelNumber), channelNumber, buffersList.get(channelNumber));

        return new DataSeries() {
            @Override
            public long size() {
                return size;
                //return edfReader.getNumberOfSamples(channelNumber);
            }

            @Override
            public int get(long index) {
                return EdfData.this.get(channelNumber, index);
             }

            @Override
            public double start() {
                return edfReader.getHeaderInfo().getRecordingStartTime();
            }

            @Override
            public double sampleRate() {
                return edfReader.getHeaderInfo().getSignalConfig(channelNumber).getNumberOfSamplesInEachDataRecord() / edfReader.getHeaderInfo().getDurationOfDataRecord();
            }

            @Override
            public Scaling getScaling() {
                SignalConfig signalConfig = edfReader.getHeaderInfo().getSignalConfig(channelNumber);
                ScalingImpl scaling = new ScalingImpl();
                scaling.setSamplingInterval(1.0 / sampleRate());
                scaling.setTimeSeries(true);
                scaling.setStart(start());
                double gain = (signalConfig.getPhysicalMax() - signalConfig.getPhysicalMin()) / (signalConfig.getDigitalMax() - signalConfig.getDigitalMin());
                double offset = signalConfig.getPhysicalMin() - signalConfig.getDigitalMin() * gain;
             //   scaling.setDataGain(gain);
             //   scaling.setDataOffset(offset);
             //   scaling.setDataDimension(signalConfig.getPhysicalDimension());
                return scaling;
            }
        };

    }

    synchronized private int get(int channelNumber, long index) {
        if(index < samplePointersList.get(channelNumber) || index >= samplePointersList.get(channelNumber)+bufferSize){
            fullBuffer(index, channelNumber, buffersList.get(channelNumber));
        }
        return buffersList.get(channelNumber)[(int) (index-samplePointersList.get(channelNumber))];
    }


    synchronized private void fullBuffer(long index, int channelNumber, int[] bufferArray) {
        long newPosition = Math.max(0, index - bufferArray.length/2);
        edfReader.setSamplePosition(channelNumber, newPosition);
        samplePointersList.set(channelNumber,newPosition);
        try {
            edfReader.readDigitalSamples(channelNumber, bufferArray, 0, bufferSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
