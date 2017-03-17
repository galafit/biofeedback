package main;

import main.data.*;
import main.functions.Function;
import main.graph.GraphType;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gala on 04/03/17.
 */
public class Viewer extends JFrame {
    GraphViewer graphViewer;
    private final double PREVIEW_TIME_FREQUENCY = 50.0 / 750;
    private Function lastFunction;

    public Viewer() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton playButton = new JButton("play");
        JButton showButton = new JButton("show");
        JTextField durationField = new JTextField(3);
        playButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double startTime = graphViewer.getStart();
                double duration = 3;
                if(!durationField.getText().isEmpty()) {
                    duration = new Double(durationField.getText());
                }
                graphViewer.requestFocusInWindow();
                validate();
                StdAudio.play(lastFunction, startTime, duration, 3);
            }
        });

        showButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double startTime = graphViewer.getStart();
                double duration = 3;
               // DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
               // String timeStamp = dateFormat.format(new Date((long) (startTime * 1000)));
               // System.out.println("time "+timeStamp);

                if(!durationField.getText().isEmpty()) {
                    duration = new Double(durationField.getText());
                }
                graphViewer.requestFocusInWindow();
                validate();
                double[] graph = lastFunction.toNormalizedArray(startTime, graphViewer.getGraphsSamplingRate(), duration, 1);
                GraphViewer viewer = new GraphViewer(true, false);
                viewer.setPreviewFrequency(PREVIEW_TIME_FREQUENCY);
                JDialog dialog = new JDialog(Viewer.this);
                dialog.setPreferredSize(new Dimension(1000, 500));
                viewer.addGraph(arrToSeries(graph, graphViewer.getGraphsSamplingRate()));
                dialog.add(viewer,  BorderLayout.CENTER);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.show();

            }
        });
        controlPanel.add(durationField);
        controlPanel.add(new JLabel("Sec"));
        controlPanel.add(playButton);
        controlPanel.add(showButton);
        add(controlPanel, BorderLayout.NORTH);

        setTitle("Graphic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphViewer = new GraphViewer(true, false);
        graphViewer.setPreviewFrequency(PREVIEW_TIME_FREQUENCY);
        setPreferredSize(new Dimension(1100, 800));
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
        graphViewer.addGraphPanel(2, true);
        graphViewer.addGraph(dataSeries);
        this.lastFunction = dataSeries;
    }

    public void addGraph(Function f) {
        double from = 0;
        double sampleRate = 1;
        double duration = 1000;
        if(lastFunction != null) {
            from = graphViewer.getStart();
            sampleRate = graphViewer.getGraphsSamplingRate();
            duration = graphViewer.getGraphsSize() / sampleRate;
         }
        addGraph(f, from, sampleRate, duration);
    }


    public void addGraph(Function f, double from, double sampleRate, double duration) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;

            @Override
            public long size() {
                return (long) (duration * sampleRate);
            }

            @Override
            public int get(long index) {
                double x =  index / sampleRate + from;
                return (int) (f.value(x) * intScaling);
            }

            @Override
            public double start() {
                return from;
            }

            @Override
            public double sampleRate() {
                return sampleRate;
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setStart(from * 1000);
                scaling.setTimeSeries(true);
                scaling.setDataGain(1.0 / intScaling);
                scaling.setSamplingInterval(1.0 / sampleRate);
                return scaling;
            }
        };
        graphViewer.addGraphPanel(2, true);
        graphViewer.addGraph(dataSeries);
        this.lastFunction = f;
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
        graphViewer.addGraphPanel(2, true);
        graphViewer.addGraph(dataSeries);
        this.lastFunction = f;
    }

    public void addGraph(double[] data, double sampleRate) {
        addGraph(arrToSeries(data, sampleRate));
    }

    private DataSeries arrToSeries(double[] data, double sampleRate) {
        return new DataSeries() {
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
    }


}
