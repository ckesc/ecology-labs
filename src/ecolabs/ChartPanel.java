/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecolabs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author FormularHunter
 */
public class ChartPanel extends JComponent {

    private JFreeChart chart;

    public ChartPanel() {
        this.chart = chart;
    }


    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
                        if (chart == null) {
            return;
        }
        BufferedImage image = chart.createBufferedImage(getWidth(), getHeight());
        Graphics2D graphics2D = (Graphics2D) grphcs;
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
