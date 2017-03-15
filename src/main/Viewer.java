package main;

import main.data.CompressionType;
import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphType;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by gala on 04/03/17.
 */
public class Viewer extends JFrame {
    GraphViewer graphViewer;
    private final double PREVIEW_TIME_FREQUENCY = 50.0 / 750;
    private DataSeries dataSeries;

    public Viewer() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton playButton = new JButton("play");
        JTextField durationField = new JTextField(3);
        JTextField maxField = new JTextField(6);
        playButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long startIndex = graphViewer.getStartIndex();
                double duration = new Double(durationField.getText());
                double max = new Double(maxField.getText());
                System.out.println("duration "+duration);
                graphViewer.requestFocusInWindow();
                validate();
                Function f = new SeriesFunction(dataSeries, startIndex, max);
               /* Viewer viewer = new Viewer();
                viewer.addGraph(f, duration);*/
                StdAudio.play(f, duration, 5);
            }
        });
        controlPanel.add(durationField);
        controlPanel.add(new JLabel("Sec"));
        controlPanel.add(maxField);
        controlPanel.add(new JLabel("Max"));

        controlPanel.add(playButton);
        add(controlPanel, BorderLayout.NORTH);

        setTitle("Graphic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphViewer = new GraphViewer(true, false);
        graphViewer.setPreviewFrequency(PREVIEW_TIME_FREQUENCY);
        setPreferredSize(new Dimension(1100, 600));
        add(graphViewer, BorderLayout.CENTER);
        pack();
        setVisible(true);
        graphViewer.requestFocusInWindow();
        validate();
    }

    public void addPreview(DataSeries dataSeries) {
        graphViewer.addPreview(dataSeries, GraphType.VERTICAL_LINE, CompressionType.MAX);
    }



    public void addGraph(DataSeries dataSeries) {
        graphViewer.addGraph(dataSeries);
        this.dataSeries = dataSeries;
    }


    public void addGraph(Function f, double from, double duration, double sampleRate) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;

            @Override
            public long size() {
                return (int) (duration * sampleRate);
            }

            @Override
            public int get(long index) {
                double x = (double) (index) / sampleRate;

                return (int) (f.value(x) * intScaling);
            }

            @Override
            public double start() {
                return (long) from;
            }

            @Override
            public double sampleRate() {
                return sampleRate;
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setStart(from);
                scaling.setDataGain(1.0 / intScaling);
                scaling.setSamplingInterval(1.0 / sampleRate);
                return scaling;
            }
        };
        addGraph(dataSeries);
    }


    public void addGraph(Function f, double duration) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;
            int numberOfPoints = 1000;
            int sampleRate = (int) (numberOfPoints / duration);

            @Override
            public long size() {
                return (int) (duration * sampleRate);
            }

            @Override
            public int get(long index) {
                double x = (double) (index) / sampleRate;

                return (int) (f.value(x) * intScaling);
            }

            @Override
            public double start() {
                return 0;
            }

            @Override
            public double sampleRate() {
                return sampleRate;
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0 / intScaling);
                scaling.setSamplingInterval(1.0 / sampleRate);
                return scaling;
            }
        };
        addGraph(dataSeries);
    }

    public void addGraph(double[] data, int sampleRate) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;

            @Override
            public long size() {
                return data.length;
            }

            @Override
            public int get(long index) {
                return (int) (data[(int)index] * intScaling);
            }

            @Override
            public double start() {
                return 0;
            }

            @Override
            public double sampleRate() {
                return sampleRate;
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0 / intScaling);
                scaling.setSamplingInterval(1.0 / sampleRate);
                return scaling;
            }
        };
        addGraph(dataSeries);
    }



}
