package main;


import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;
import com.biorecorder.edflib.base.SignalConfig;
import com.github.rjeschke.neetutils.audio.FIRUtils;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfReader edfReader;
    private int defaultBufferSize =1024*16;
    private Map<Integer, Channel> channelMap = Collections.synchronizedMap(new HashMap<Integer, Channel>());



    public EdfData(File edfFile) throws IOException, HeaderParsingException {
        edfReader = new EdfReader(edfFile);
        edfReader.printHeaderInfo();
    }

     public DataSeries getChannelSeries(int channelNumber){
         return getChannelSeries(channelNumber, defaultBufferSize);
     }

    synchronized public DataSeries getChannelSeries(int channelNumber, int bufferSize){

        if (channelMap.get(channelNumber) == null) {
            channelMap.put(channelNumber, new Channel(bufferSize));
            try {
                channelMap.get(channelNumber).setSize(edfReader.getNumberOfSamples(channelNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fullBuffer (channelMap.get(channelNumber).getPointer(), channelNumber);
        }

        return new DataSeries() {
            @Override
            public long size() {
                return channelMap.get(channelNumber).getSize();
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
        if(index < channelMap.get(channelNumber).getPointer() || index >= channelMap.get(channelNumber).getPointer()+ defaultBufferSize){
            fullBuffer(index, channelNumber);
        }
        return channelMap.get(channelNumber).getBuffer()[(int) (index- channelMap.get(channelNumber).getPointer())];
    }

    synchronized private void update(){
        channelMap.forEach((k,v)->{
            fullBuffer(v.getPointer(),k);
            try {
                v.setSize(edfReader.getNumberOfSamples(k));
            } catch (IOException e) {

            }
        });
    }

    synchronized private void fullBuffer(long index, int channelNumber) {

        long newPosition = Math.max(0, index - channelMap.get(channelNumber).getBuffer().length/2);
        edfReader.setSamplePosition(channelNumber, newPosition);
        channelMap.get(channelNumber).setPointer(newPosition);
        try {
            edfReader.readDigitalSamples(channelNumber, channelMap.get(channelNumber).getBuffer(), 0, defaultBufferSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Channel {

        private int[] buffer;
        private long pointer;
        private long size;
        private double[] fir;

        public Channel(int bufferSize) {
            buffer = new int[bufferSize];
            fir = FIRUtils.createLowpass(10, 0.1, 250);
        }

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



