package main;

import main.data.*;
import main.graph.GraphType;
import main.graph.GraphView;
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
    private DataSeries lastDataSeries;

    public Viewer() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton playButton = new JButton("play");
        JButton showButton = new JButton("show");
        JTextField durationField = new JTextField(3);
        playButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long startIndex = graphViewer.getStartIndex();
                double startTime =  (lastDataSeries.start() + startIndex / lastDataSeries.sampleRate());
               // DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                // String timeStamp = dateFormat.format(new Date((long) (startTime * 1000)));

                double duration = 10;
                if(!durationField.getText().isEmpty()) {
                    duration = new Double(durationField.getText());
                }
                graphViewer.requestFocusInWindow();
                validate();
                StdAudio.play(lastDataSeries, startTime, duration, 5);
            }
        });

        showButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long startIndex = graphViewer.getStartIndex();
                double startTime =  (lastDataSeries.start() + startIndex / lastDataSeries.sampleRate());
                double duration = 10;
                if(!durationField.getText().isEmpty()) {
                    duration = new Double(durationField.getText());
                }
                graphViewer.requestFocusInWindow();
                validate();
                double[] graph = lastDataSeries.toNormalizedArray(startTime, duration, 1, lastDataSeries.sampleRate());
                GraphViewer viewer = new GraphViewer(true, false);
                viewer.setPreviewFrequency(PREVIEW_TIME_FREQUENCY);
                JDialog dialog = new JDialog(Viewer.this);
                dialog.setPreferredSize(new Dimension(1000, 500));
                viewer.addGraph(arrToSeries(graph, lastDataSeries.sampleRate()));
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
        this.lastDataSeries = dataSeries;
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
