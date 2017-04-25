package main.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by gala on 20/04/17.
 */
public class TestPane extends JPanel {
    public TestPane() {
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font  = new Font("Tahoma", Font.PLAIN, 12);
        g.setFont(font);
        String str1= "HelloWorld";
        String str2 = "1";
        String str3 = "BIG LABEL ON HTE screen";

        System.out.println("font "+font.getSize());

        Dimension d;
        d = getSize1(str1, font);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        d = getSize2(str1, (Graphics2D) g);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        System.out.println();

        d = getSize1(str2, font);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        d = getSize2(str2, (Graphics2D) g);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        System.out.println();

        d = getSize1(str3, font);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        d = getSize2(str3, (Graphics2D) g);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        System.out.println();


        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);
        d = getSize1(str3, rotatedFont);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        d = getSize2(str3, (Graphics2D) g);
        System.out.println(d.getWidth() + "  "+d.getHeight());
        System.out.println();
        g.setColor(Color.cyan);
        g.drawString(str3, 50, 50);

    }

    private Dimension getSize2(String text, Graphics2D g) {
        TextLayout layout = new TextLayout(text, g.getFont(), g.getFontRenderContext());
        Rectangle2D labelBounds = layout.getBounds();
        return new Dimension((int)labelBounds.getWidth(), (int)labelBounds.getHeight());
    }

    private Dimension getSize1(String text, Font font) {
        AffineTransform affinetransform = font.getTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
        int textheight = (int)(font.getStringBounds(text, frc).getHeight());
        return new Dimension(textwidth, textheight);

    }
}
