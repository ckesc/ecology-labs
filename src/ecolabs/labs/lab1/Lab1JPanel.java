/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Lab1JPanel.java
 *
 * Created on 19.11.2011, 13:59:10
 */
package ecolabs.labs.lab1;

import ecolabs.labs.LabJPanel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Ск
 */
public class Lab1JPanel extends LabJPanel {

    /**
     * Средний медианный размер капель воды, мм
     */
    //double Dh2o = 10;
    /**
     * Расход воды для орошения газа, м3/с;
     */
    double Vaq = 2;
    /**
     * объемный расход газа на выходе из скруббера при рабочих условиях,
     * м3/с;
     */
    double Va = 20;
    /**
     *  Коэффициент захвата частицы каплей воды;
     */
    double ησ = 1.2;
    
    /**
     * Точки графика для первого размера частиц
     */
    HashMap<Double, Double> points1 = new HashMap<Double, Double>();
    
    /**
     * Точки графика для второго размера частиц
     */
    HashMap<Double, Double> points2 = new HashMap<Double, Double>();
    
    ArrayList<XYSeries> serieses1 = new ArrayList<XYSeries>();
    ArrayList<XYSeries> serieses2 = new ArrayList<XYSeries>();
    
    /** Creates new form Lab1JPanel */
    public Lab1JPanel() {
        initComponents();
        Caption = "Лабораторная работа №1";
    }

    /**
     * Степень очистки газа
     * @param H Высота скруббера,
     * @param Dsol Средний медианный размер частиц пыли
     * @return 
     */
    public double η(double H, double Dh2o, double Dsol) {
        double numerator  = -3 * Vaq * ησ * H;
        double denominator = 2 * Va * Dh2o * 0.001f;
        return 1 - (double) Math.exp(numerator / denominator);
    }
    
    /**
     * Вычисляет точки кривой
     * @param Dsol
     * @return 
     */
    public HashMap<Double, Double> Calculate(double Dh2o, double Dsol)
    {   
        HashMap<Double, Double> points = new HashMap<Double, Double>();
        
        for (double H = 0.02; H <= 2; H += 0.02) {
            points.put(H, η(H, Dh2o, Dsol));
        }
        
        return points;
    }
    
    /**
     * Выводит график
     */
    public void showChart(CombinedDomainXYPlot parent, JLabel jLabel, boolean showLegend)
    {     
        JFreeChart chart = new JFreeChart(parent);
        BufferedImage image = chart.createBufferedImage(jLabel.getWidth(),jLabel.getHeight());
        jLabel.setIcon(new ImageIcon(image));
    }

    /**
     * Вычистяет все 3 кривых на одном графике
     * @param Dsol
     * @return 
     */
    public CombinedDomainXYPlot createChart(double Dsol)
    {
        CombinedDomainXYPlot parent = new CombinedDomainXYPlot(
                        new NumberAxis("x-angle argument"));
        
        for (int Dh2o = 1; Dh2o <= 3; Dh2o++)
        {
            XYSeries series = new XYSeries(Dh2o);
            
            HashMap<Double, Double> points = 
                    Calculate(Dh2o, Dsol);
            
            for (Double H : points.keySet()) {
                series.add(H, points.get(H));
            }
            
            XYDataset dataset = new XYSeriesCollection(series);
            
            XYItemRenderer renderer = new StandardXYItemRenderer();
            
            XYPlot subplot = new XYPlot(dataset, null, new NumberAxis("η(H)"),renderer);
            
            NumberAxis axis = (NumberAxis)subplot.getRangeAxis();
            axis.setTickLabelFont(new Font("Monospaced", Font.PLAIN,7));
            axis.setLabelFont(new Font("SansSerif", Font.PLAIN,7));
            axis.setAutoRangeIncludesZero(false);
            
            parent.add(subplot, 1); 
        }
        return parent;
    }
            
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jComboBoxVar = new javax.swing.JComboBox();
        jLabelVar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanelPicture = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldVaq = new javax.swing.JTextField();
        jTextFieldVa = new javax.swing.JTextField();
        jTextFieldησ = new javax.swing.JTextField();
        jButtonExecute = new javax.swing.JButton();
        jLabelVaq = new javax.swing.JLabel();
        jLabelVa = new javax.swing.JLabel();
        jLabelησ = new javax.swing.JLabel();
        jPanelCharts = new javax.swing.JPanel();
        jLabelChart1 = new javax.swing.JLabel();
        jLabelChart2 = new javax.swing.JLabel();
        jTextFieldDsol1 = new javax.swing.JTextField();
        jTextFieldDsol2 = new javax.swing.JTextField();
        jLabelDsol2 = new javax.swing.JLabel();
        jLabelDsol1 = new javax.swing.JLabel();

        jComboBoxVar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1" }));
        jComboBoxVar.setName("jComboBoxVar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(Lab1JPanel.class);
        jLabelVar.setText(resourceMap.getString("jLabelVar.text")); // NOI18N
        jLabelVar.setName("jLabelVar"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabel1.setName("jLabelFormula"); // NOI18N

        jPanelPicture.setName("jPanelScrubber"); // NOI18N
        jPanelPicture.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanelPicture.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 270));

        jTextFieldVaq.setText(resourceMap.getString("jTextFieldVaq.text")); // NOI18N
        jTextFieldVaq.setName("jTextFieldVaq"); // NOI18N
        jPanelPicture.add(jTextFieldVaq, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 90, -1));

        jTextFieldVa.setText(resourceMap.getString("jTextFieldVa.text")); // NOI18N
        jTextFieldVa.setName("jTextFieldVa"); // NOI18N
        jPanelPicture.add(jTextFieldVa, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, -1));

        jTextFieldησ.setText(resourceMap.getString("jTextFieldησ.text")); // NOI18N
        jTextFieldησ.setName("jTextFieldησ"); // NOI18N
        jPanelPicture.add(jTextFieldησ, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 90, -1));

        jButtonExecute.setLabel(resourceMap.getString("jButtonExecute.label")); // NOI18N
        jButtonExecute.setName("jButtonExecute"); // NOI18N
        jButtonExecute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonExecuteMouseClicked(evt);
            }
        });
        jPanelPicture.add(jButtonExecute, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, -1, -1));

        jLabelVaq.setText(resourceMap.getString("jLabelVaq.text")); // NOI18N
        jLabelVaq.setName("jLabelVaq"); // NOI18N
        jPanelPicture.add(jLabelVaq, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        jLabelVa.setText(resourceMap.getString("jLabelVa.text")); // NOI18N
        jLabelVa.setName("jLabelVa"); // NOI18N
        jPanelPicture.add(jLabelVa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        jLabelησ.setText(resourceMap.getString("jLabelησ.text")); // NOI18N
        jLabelησ.setName("jLabelησ"); // NOI18N
        jPanelPicture.add(jLabelησ, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        jPanelCharts.setName("jPanelCharts"); // NOI18N
        jPanelCharts.setLayout(new java.awt.GridBagLayout());

        jLabelChart1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelChart1.setText(resourceMap.getString("jLabelChart1.text")); // NOI18N
        jLabelChart1.setName("jLabelChart1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.ipady = 250;
        jPanelCharts.add(jLabelChart1, gridBagConstraints);

        jLabelChart2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelChart2.setText(resourceMap.getString("jLabelChart2.text")); // NOI18N
        jLabelChart2.setName("jLabelChart2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.ipady = 250;
        jPanelCharts.add(jLabelChart2, gridBagConstraints);

        jTextFieldDsol1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDsol1.setText(resourceMap.getString("jTextFieldDsol1.text")); // NOI18N
        jTextFieldDsol1.setName("jTextFieldDsol1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        jPanelCharts.add(jTextFieldDsol1, gridBagConstraints);

        jTextFieldDsol2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDsol2.setText(resourceMap.getString("jTextFieldDsol2.text")); // NOI18N
        jTextFieldDsol2.setName("jTextFieldDsol2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        jPanelCharts.add(jTextFieldDsol2, gridBagConstraints);

        jLabelDsol2.setFont(resourceMap.getFont("jLabelDsol2.font")); // NOI18N
        jLabelDsol2.setText(resourceMap.getString("jLabelDsol2.text")); // NOI18N
        jLabelDsol2.setName("jLabelDsol2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelCharts.add(jLabelDsol2, gridBagConstraints);

        jLabelDsol1.setFont(resourceMap.getFont("jLabelDsol1.font")); // NOI18N
        jLabelDsol1.setText(resourceMap.getString("jLabelDsol1.text")); // NOI18N
        jLabelDsol1.setName("jLabelDsol1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelCharts.add(jLabelDsol1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCharts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVar)
                            .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButtonExecuteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonExecuteMouseClicked
    try {
        Vaq = Double.parseDouble(jTextFieldVaq.getText()); 
        Va = Double.parseDouble(jTextFieldVa.getText()); 
        ησ = Double.parseDouble(jTextFieldησ.getText());
        
        showChart(createChart(Double.parseDouble(jTextFieldDsol1.getText())), jLabelChart1, true);
        showChart(createChart(Double.parseDouble(jTextFieldDsol2.getText())), jLabelChart2, true);

    } catch (Exception e) {
    }
}//GEN-LAST:event_jButtonExecuteMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExecute;
    private javax.swing.JComboBox jComboBoxVar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelChart1;
    private javax.swing.JLabel jLabelChart2;
    private javax.swing.JLabel jLabelDsol1;
    private javax.swing.JLabel jLabelDsol2;
    private javax.swing.JLabel jLabelVa;
    private javax.swing.JLabel jLabelVaq;
    private javax.swing.JLabel jLabelVar;
    private javax.swing.JLabel jLabelησ;
    private javax.swing.JPanel jPanelCharts;
    private javax.swing.JPanel jPanelPicture;
    private javax.swing.JTextField jTextFieldDsol1;
    private javax.swing.JTextField jTextFieldDsol2;
    private javax.swing.JTextField jTextFieldVa;
    private javax.swing.JTextField jTextFieldVaq;
    private javax.swing.JTextField jTextFieldησ;
    // End of variables declaration//GEN-END:variables
}