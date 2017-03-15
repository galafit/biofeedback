package main;


import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;
import com.biorecorder.edflib.base.SignalConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channel;
import java.util.*;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfReader edfReader;
    private int bufferSize=1024*8;
 //   private List<Long> samplePointersList  =  Collections.synchronizedList(new ArrayList<Long>());
 //   private List<int[]> buffersList = Collections.synchronizedList(new ArrayList<int[]>());
//    private Map<Integer, int[]> buffersMap =  Collections.synchronizedMap(new HashMap<Integer, int[]>());

    private Map<Integer, Channel> channelMap = Collections.synchronizedMap(new HashMap<Integer, Channel>());



    public EdfData(File edfFile) throws IOException, HeaderParsingException {
        edfReader = new EdfReader(edfFile);
        edfReader.printHeaderInfo();
    }

    synchronized public DataSeries getChannelSeries(int channelNumber){
        channelMap.put(channelNumber, new Channel());
        final long  size;
        long size1;
        try {
            size1 = edfReader.getNumberOfSamples(channelNumber);
        } catch (IOException e) {
            e.printStackTrace();
            size1 = 0;
        }


        size = size1;

        fullBuffer (channelMap.get(channelNumber).getPointer(), channelNumber);

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
                return edfReader.getHeaderInfo().getRecordingStartTime() / 1000;
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
                scaling.setStart(start() * 1000);
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
        if(index < channelMap.get(channelNumber).getPointer() || index >= channelMap.get(channelNumber).getPointer()+bufferSize){
            fullBuffer(index, channelNumber);
        }
        return channelMap.get(channelNumber).getBuffer()[(int) (index- channelMap.get(channelNumber).getPointer())];
    }

    

    synchronized private void fullBuffer(long index, int channelNumber) {
        long newPosition = Math.max(0, index - channelMap.get(channelNumber).getBuffer().length/2);
        edfReader.setSamplePosition(channelNumber, newPosition);
        channelMap.get(channelNumber).setPointer(newPosition);
        try {
            edfReader.readDigitalSamples(channelNumber, channelMap.get(channelNumber).getBuffer(), 0, bufferSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Channel {
        private int[] buffer = new int[bufferSize];
        private long pointer;
        private long size;

        public int[] getBuffer() {
            return buffer;
        }

        public long getPointer() {
            return pointer;
        }

        public long getSize() {
            return size;
        }

        public void setPointer(long pointer) {
            this.pointer = pointer;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }

}



