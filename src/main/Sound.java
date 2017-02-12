package main;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

/**
 * Created by recovery on 12.02.2017.
 */
public class Sound {
    public static void Something () {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            channels[0].programChange(41);
            channels[0].noteOn(65, 80);
            Thread.sleep(1000); // in milliseconds
            channels[0].noteOff(65);
            synth.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
