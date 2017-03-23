package main;


import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;
import com.biorecorder.edflib.base.SignalConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import uk.me.berndporr.iirj.Butterworth;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfReader edfReader;
    private int defaultBufferSize = 1024 * 16;
    private List<Channel> channelList = new ArrayList<>();


    public EdfData(File edfFile) throws IOException, HeaderParsingException {
        edfReader = new EdfReader(edfFile);
        edfReader.printHeaderInfo();
    }

    public DataSeries getChannelData(int channelNumber){
        return getChannelData(channelNumber,defaultBufferSize);
    }

    public DataSeries getChannelData(int channelNumber, int bufferSize) {
        Channel channel = new Channel(bufferSize, channelNumber);
        channelList.add(channel);
        return channel;
    }


    synchronized private void update() {
        channelList.forEach(channel -> channel.update());
    }

 /*   synchronized private void fullBuffer(long index, int channelNumber) {
        long newPosition = Math.max(0, index - channelMap.get(channelNumber).getBuffer().length / 2);
        edfReader.setSamplePosition(channelNumber, newPosition);
        channelMap.get(channelNumber).setPointer(newPosition);
        try {
            int[] tmpBuffer = edfReader.readDigitalSamples(channelNumber, defaultBufferSize);
            for (int i = 0; i < tmpBuffer.length; i++) {
                //channelMap.get(channelNumber).getBuffer()[i] = (int)channelMap.get(channelNumber).getFilter().filter(tmpBuffer[i]);
                double sample = channelMap.get(channelNumber).getFilter().filter(tmpBuffer[i]);
                channelMap.get(channelNumber).getBuffer()[i] = (int) sample;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    } */


    class Channel  implements DataSeries {
        private int[] buffer;
        private long pointer;
        private long size;
        private int channelNumber;

        public Channel(int bufferSize, int channelNumber) {
            buffer = new int[bufferSize];
            this.channelNumber = channelNumber;

            try {
                size = edfReader.getNumberOfSamples(channelNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fullBuffer(pointer);

        }

        synchronized private void update() {
            fullBuffer(pointer);
            try {
                size = edfReader.getNumberOfSamples(channelNumber);
            } catch (IOException e) {

            }
        }

        synchronized private void fullBuffer(long index) {

            pointer = Math.max(0, index - buffer.length / 2);
            edfReader.setSamplePosition(channelNumber, pointer);
            try {
                edfReader.readDigitalSamples(channelNumber, buffer, 0, defaultBufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public synchronized int get(long index) {
            if (index < pointer || index >= pointer + defaultBufferSize) {
                fullBuffer(index);
            }
            return buffer[(int) (index - pointer)];
        }

        @Override
        public long size() {
            return size;
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
    }

}





