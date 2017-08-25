package main;


import com.biorecorder.edflib.EdfFileReader;
import com.biorecorder.edflib.HeaderConfig;
import com.biorecorder.edflib.exceptions.EdfHeaderRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by hdablin on 04.03.17.
 */
public class EdfData {
    private EdfFileReader edfReader;
    private List<ChannelData> channelDataList = new ArrayList<>();
    private HeaderConfig header;


    public EdfData(File edfFile) throws IOException, EdfHeaderRuntimeException {
        edfReader = new EdfFileReader(edfFile);
        header = edfReader.getHeader();
        System.out.println(header);
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

    public int getNumberOfChannels() {
        return header.getNumberOfSignals();
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





