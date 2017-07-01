package main.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by hdablin on 26.06.17.
 */
public class PreviewChartPanel extends JPanel {
    private ChartWithPreview chartWithPreview;

    public PreviewChartPanel(ChartWithPreview chartWithPreview) {
        this.chartWithPreview = chartWithPreview;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                chartWithPreview.setCursorPosition(e.getX());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chartWithPreview.draw((Graphics2D) g, new Rectangle(0,0,getWidth(),getHeight()));
    }


}
