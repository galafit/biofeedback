package main;

import com.biorecorder.edflib.EdfFileReader;
import com.biorecorder.edflib.HeaderConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import uk.me.berndporr.iirj.Cascade;

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
    private EdfFileReader edfReader;


    public ChannelData(int channelNumber, EdfFileReader edfReader) {
        this(defaultBufferSize, channelNumber, edfReader);
    }

    public ChannelData(int bufferSize, int channelNumber, EdfFileReader edfReader) {
        buffer = new int[bufferSize];
        this.edfReader = edfReader;
        this.channelNumber = channelNumber;
        size = edfReader.getNumberOfSamples(channelNumber);
        fullBuffer(pointer);

    }

    synchronized public void update() {
        fullBuffer(pointer);
        size = edfReader.getNumberOfSamples(channelNumber);
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
        edfReader.readDigitalSamples(channelNumber, buffer, 0, defaultBufferSize);
        filterList.forEach(x -> {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (int) x.filter(buffer[i]);
            }
        });
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
        return edfReader.getHeader().getRecordingStartDateTimeMs() / 1000;
    }

    @Override
    public double sampleRate() {
        return edfReader.getHeader().getSampleFrequency(channelNumber);
    }

    @Override
    public Scaling getScaling() {
        HeaderConfig edfHeader = edfReader.getHeader();
        ScalingImpl scaling = new ScalingImpl();
        scaling.setSamplingInterval(1.0 / sampleRate());
        scaling.setTimeSeries(true);
        scaling.setStart(start() * 1000);
        double gain = (edfHeader.getPhysicalMax(channelNumber) - edfHeader.getPhysicalMin(channelNumber)) / (edfHeader.getDigitalMax(channelNumber) - edfHeader.getDigitalMin(channelNumber));
        double offset = edfHeader.getPhysicalMin(channelNumber) - edfHeader.getDigitalMin(channelNumber) * gain;
        scaling.setDataGain(gain);
        scaling.setDataOffset(offset);
        scaling.setDataDimension(edfHeader.getPhysicalDimension(channelNumber));
        return scaling;
    }
}
