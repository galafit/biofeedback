package main;

import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.base.SignalConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import uk.me.berndporr.iirj.Cascade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.03.17.
 */


class ChannelData implements DataSeries {
    private int[] buffer;
    private long pointer;
    private long size;
    private int channelNumber;
    private static final int defaultBufferSize = 1024 * 16;
    private List<Cascade> filterList = new ArrayList<Cascade>();
    private EdfReader edfReader;


    public ChannelData(int channelNumber, EdfReader edfReader) {
        this(defaultBufferSize, channelNumber, edfReader);
    }

    public ChannelData(int bufferSize, int channelNumber, EdfReader edfReader) {
        buffer = new int[bufferSize];
        this.edfReader = edfReader;
        this.channelNumber = channelNumber;

        try {
            size = edfReader.getNumberOfSamples(channelNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fullBuffer(pointer);

    }

    synchronized public void update() {
        fullBuffer(pointer);
        try {
            size = edfReader.getNumberOfSamples(channelNumber);
        } catch (IOException e) {

        }
    }


    synchronized public void setFilter(Cascade filter) {
        this.filterList.add(filter);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (int) filter.filter(buffer[i]);
        }
    }

    synchronized private void fullBuffer(long index) {
        pointer = Math.max(0, index - buffer.length / 2);
        edfReader.setSamplePosition(channelNumber, pointer);
        try {
            edfReader.readDigitalSamples(channelNumber, buffer, 0, defaultBufferSize);
            filterList.forEach(x -> {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (int) x.filter(buffer[i]);
                }
            });

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
