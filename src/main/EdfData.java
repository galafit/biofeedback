package main;


import com.biorecorder.edflib.EdfReader;
import com.biorecorder.edflib.HeaderParsingException;
import com.biorecorder.edflib.base.SignalConfig;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import uk.me.berndporr.iirj.Cascade;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfReader edfReader;
    private List<ChannelData> channelDataList = new ArrayList<>();


    public EdfData(File edfFile) throws IOException, HeaderParsingException {
        edfReader = new EdfReader(edfFile);
        edfReader.printHeaderInfo();
    }

    public ChannelData getChannelData(int channelNumber) {
        ChannelData channelData = new ChannelData(channelNumber, edfReader);
        channelDataList.add(channelData);
        return channelData;
    }

    public ChannelData getChannelData(int channelNumber, int bufferSize) {
        ChannelData channelData = new ChannelData(bufferSize, channelNumber, edfReader);
        channelDataList.add(channelData);
        return channelData;
    }


    synchronized private void update() {
        channelDataList.forEach(channelData -> channelData.update());
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


}





