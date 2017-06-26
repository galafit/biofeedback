package main.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by hdablin on 26.06.17.
 */
public class PreviewChartPanel extends JPanel {
    private PreviewChart previewChart;

    public PreviewChartPanel(PreviewChart previewChart) {
        this.previewChart = previewChart;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                previewChart.setCursorPosition(e.getX());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        previewChart.draw((Graphics2D) g, new Rectangle(0,0,getWidth(),getHeight()));
    }


}
