/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecolabs;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;

/**
 *
 * @author FormularHunter
 */
public class ChartPanel extends JComponent {

    private JFreeChart chart;

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D graphics2D = (Graphics2D) grphcs;
        super.paint(grphcs);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (chart == null) {
            //Если нечего отображать  
            graphics2D.setPaint(new GradientPaint(0, 0, Color.lightGray, getHeight(),getWidth(),Color.gray));
//            graphics2D.setPaint(new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
//                    getHeight() / 2.0), getHeight(),
//                    new float[]{0.0f, 1.0f},
//                    new Color[]{new Color(6, 76, 160, 127),
//                        new Color(0.0f, 0.0f, 0.0f, 0.8f)}));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setPaint(Color.black);
            graphics2D.drawRect(0, 0, getWidth()-1, getHeight()-1);
            graphics2D.drawString("График ещё не построен", 5, 15);
            return;
        }
        BufferedImage image = chart.createBufferedImage(getWidth(), getHeight());
        graphics2D.drawImage(image, 0, 0, this);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
        repaint();
    }
}
