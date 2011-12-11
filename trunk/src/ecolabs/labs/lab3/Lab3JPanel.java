/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * lab3JPanel.java
 *
 * Created on 06.12.2011, 13:33:12
 */
package ecolabs.labs.lab3;

import ecolabs.EcolabsView;
import ecolabs.labs.ScreenJPanel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Ск
 */
public class Lab3JPanel extends ScreenJPanel {

    class Variant {

        /**
         * Диаметр циклона. 1.N
         */
        double D = 1.1;
        /**
         * Скорость движения газа
         */
        double ω = 1;
        /**
         * средний размер частиц пыли, улавливаемых на 50 %, мкм;
         */
        double d = 1;
        /**
         * коэффициент, учитывающий тип циклона;
         */
        double k = 41.4d;
        /**
         * вязкость газа, Па*c;
         */
        double μ = 22.2e-6d;
        /**
         * плотность пылевых частиц, кг/м3.
         */
        double ρ = 2150;
    }
    Variant data = new Variant();
    private HashMap<Integer, Variant> variants = new HashMap<>();
    private static String fileName = "LabVariants_№3.txt";

    /**
     * Расчет минимального размера частиц пыли, улавливаемых циклоном
     */
    private double d_min(double D, double ω) {
        return (data.d * data.k * 1000d * Math.sqrt(1000d * D * data.μ / (data.ρ * ω)));
    }

    public Lab3JPanel(EcolabsView parent) {
        super(parent);
        initComponents();
        Title = "Лабораторная работа №3";
        Caption = "Определение минимального размера частиц пыли,улавливаемых циклоном";

//        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(Lab3JPanel.class);
//        String pattern = resourceMap.getString("jLabelVarInfo.text");
        jTextFieldD.setText(String.valueOf(data.D));
        jTextFieldd.setText(String.valueOf(data.d));
        jTextFieldk.setText(String.valueOf(data.k));
        jTextFieldp.setText(String.valueOf(data.ρ));
        jTextFieldw.setText(String.valueOf(data.ω));
    }

    @Override
    public void ScreenInit() {
        super.ScreenInit();
        loadVariants();
        Execute();
    }

    
    /**
     * Вычисляет точки кривой при постоянной D
     * @param w1 начальное значение w
     * @param w2 конечное хзначение w
     * @param w_delta приращение
     */
    public HashMap<Double, Double> Calc_constD(double D, double w1, double w2, double w_delta) {
        HashMap<Double, Double> points = new HashMap<>();

        for (double w = w1; w <= w2; w += w_delta) {
            points.put(w, d_min(D, w));
        }

        return points;
    }

    /**
     * Вычисляет точки кривой при постоянной W
     * @param D1 начальное значение D
     * @param D2 конечное значение D
     * @param D_delta приращение D
     */
    public HashMap<Double, Double> Calc_constW(double w, double D1, double D2, double D_delta) {
        HashMap<Double, Double> points = new HashMap<>();

        for (double current_D = D1; current_D <= D2; current_D += D_delta) {
            points.put(current_D, d_min(current_D, w));
        }

        return points;
    }

    /**
     * Вычистяет все 3 кривых на одном графике
     */
    public CombinedDomainXYPlot createChart_Dconst() {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
                new NumberAxis("ω"));
        ArrayList<Double> D_list = new ArrayList<>();
        D_list.add(0.2d);
        D_list.add(0.8d);
        D_list.add(1.4d);
        D_list.add(2.0d);
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double curr_D : D_list) {
            XYSeries series = new XYSeries(curr_D);

            HashMap<Double, Double> points = Calc_constD(curr_D, 1.2, 10, 0.1);

            for (Double w : points.keySet()) {
                series.add(w, points.get(w));
            }

            dataset.addSeries(series);
        }
        XYItemRenderer renderer = new StandardXYItemRenderer();
        XYPlot subplot = new XYPlot(dataset, null, new NumberAxis(null), renderer);
        plot.add(subplot);
        return plot;
    }

    /**
     * Вычистяет все 3 кривых на одном графике для постоянной ω
     */
    public CombinedDomainXYPlot createChart_Wconst() {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
                new NumberAxis("D"));
        ArrayList<Double> W_list = new ArrayList<>(Arrays.asList(1d, 4d, 7d, 10d));
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double curr_W : W_list) {
            XYSeries series = new XYSeries(curr_W);

            HashMap<Double, Double> points = Calc_constW(curr_W, 0.1d, 2d, 0.01d);

            for (Double D : points.keySet()) {
                series.add(D, points.get(D));
            }

            dataset.addSeries(series);
        }
        XYItemRenderer renderer = new StandardXYItemRenderer();
        XYPlot subplot = new XYPlot(dataset, null, new NumberAxis(null), renderer);
        plot.add(subplot);
        return plot;
    }

    /**
     * Строить графики
     */
    private void Execute() {
        parentFrame.setStatus(null);
        try {
            data.d = Double.valueOf(jTextFieldd.getText());
            data.k = Double.valueOf(jTextFieldk.getText());
            data.ρ = Double.valueOf(jTextFieldp.getText());
            data.μ = Double.valueOf(jTextFieldu.getText());

            JFreeChart chart1;
            JFreeChart chart2;

            chart1 = new JFreeChart(createChart_Dconst());
            chart2 = new JFreeChart(createChart_Wconst());

            chartPanel1.setChart(chart1);
            chartPanel2.setChart(chart2);
        } catch (Exception e) {
            parentFrame.setStatus("Ошибка в ходе вычислений. Возможно вы ввели не правильно число");
        }
    }

    /**
     * Загрузка вариантов
     */
    public void loadVariants() {
        jComboBoxVar.setModel(new DefaultComboBoxModel());
        ArrayList<String[]> lines = ScreenJPanel.loadVariants(fileName, 7);
        Variant v;
        int varNum;
        for (String[] parameters : lines) {
            try {
                v = new Variant();
                varNum = Integer.parseInt(parameters[0].trim());
                v.d = Double.parseDouble(parameters[1].trim());
                v.k = Double.parseDouble(parameters[2].trim());
                v.ρ = Double.parseDouble(parameters[3].trim());
                v.μ = Double.parseDouble(parameters[4].trim());
                v.D = Double.parseDouble(parameters[5].trim());
                v.ω = Double.parseDouble(parameters[6].trim());
            } catch (Exception e) {
                // Произошла ошибка??
                // Да ну её к чёёёёёёёёрту!
                // Пойдём я тебе лучше пааааасеку покажу
                parentFrame.setStatus("Ошибка при обработке файла с вариантами в строке " + (lines.indexOf(parameters) + 1));
                continue;
            }
            variants.put(varNum, v);
            jComboBoxVar.addItem(varNum);
        }
        jComboBoxVarItemStateChanged(null);
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
        jPanel1 = new javax.swing.JPanel();
        jTextFieldD = new javax.swing.JTextField();
        jTextFieldw = new javax.swing.JTextField();
        jTextFieldd = new javax.swing.JTextField();
        jTextFieldk = new javax.swing.JTextField();
        jTextFieldu = new javax.swing.JTextField();
        jTextFieldp = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        chartPanel2 = new ecolabs.ChartPanel();
        chartPanel1 = new ecolabs.ChartPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabelFormula = new javax.swing.JLabel();
        jLabelImg = new javax.swing.JLabel();
        jLabel_Img = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        jComboBoxVar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1" }));
        jComboBoxVar.setName("jComboBoxVar"); // NOI18N
        jComboBoxVar.setOpaque(false);
        jComboBoxVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxVarItemStateChanged(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(Lab3JPanel.class);
        jLabelVar.setText(resourceMap.getString("jLabelVar.text")); // NOI18N
        jLabelVar.setName("jLabelVar"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);

        jTextFieldD.setToolTipText(resourceMap.getString("jTextFieldD.toolTipText")); // NOI18N
        jTextFieldD.setName("jTextFieldD"); // NOI18N
        jTextFieldD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldw.setText(resourceMap.getString("jTextFieldw.text")); // NOI18N
        jTextFieldw.setToolTipText(resourceMap.getString("jTextFieldw.toolTipText")); // NOI18N
        jTextFieldw.setName("jTextFieldw"); // NOI18N
        jTextFieldw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldd.setText(resourceMap.getString("jTextFieldd.text")); // NOI18N
        jTextFieldd.setToolTipText(resourceMap.getString("jTextFieldd.toolTipText")); // NOI18N
        jTextFieldd.setName("jTextFieldd"); // NOI18N
        jTextFieldd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldk.setText(resourceMap.getString("jTextFieldk.text")); // NOI18N
        jTextFieldk.setToolTipText(resourceMap.getString("jTextFieldk.toolTipText")); // NOI18N
        jTextFieldk.setName("jTextFieldk"); // NOI18N
        jTextFieldk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldu.setText(resourceMap.getString("jTextFieldu.text")); // NOI18N
        jTextFieldu.setToolTipText(resourceMap.getString("jTextFieldu.toolTipText")); // NOI18N
        jTextFieldu.setName("jTextFieldu"); // NOI18N
        jTextFieldu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFielduActionPerformed(evt);
            }
        });
        jTextFieldu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldp.setText(resourceMap.getString("jTextFieldp.text")); // NOI18N
        jTextFieldp.setToolTipText(resourceMap.getString("jTextFieldp.toolTipText")); // NOI18N
        jTextFieldp.setName("jTextFieldp"); // NOI18N
        jTextFieldp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jLabel1.setLabelFor(jLabel1);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setLabelFor(jLabel2);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setLabelFor(jLabel3);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setLabelFor(jLabel4);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setLabelFor(jLabel5);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setLabelFor(jLabel6);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldp, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jTextFieldu, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jTextFieldk, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jTextFieldD, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jTextFieldw, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jTextFieldd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        chartPanel2.setName("chartPanel2"); // NOI18N

        javax.swing.GroupLayout chartPanel2Layout = new javax.swing.GroupLayout(chartPanel2);
        chartPanel2.setLayout(chartPanel2Layout);
        chartPanel2Layout.setHorizontalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );
        chartPanel2Layout.setVerticalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(chartPanel2, gridBagConstraints);

        chartPanel1.setName("chartPanel1"); // NOI18N

        javax.swing.GroupLayout chartPanel1Layout = new javax.swing.GroupLayout(chartPanel1);
        chartPanel1.setLayout(chartPanel1Layout);
        chartPanel1Layout.setHorizontalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );
        chartPanel1Layout.setVerticalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(chartPanel1, gridBagConstraints);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jTextField1, gridBagConstraints);

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jTextField2, gridBagConstraints);

        jLabelFormula.setIcon(resourceMap.getIcon("jLabelFormula.icon")); // NOI18N
        jLabelFormula.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabelFormula.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelFormula.setIconTextGap(10);
        jLabelFormula.setName("jLabelFormula"); // NOI18N

        jLabelImg.setText(resourceMap.getString("jLabelImg.text")); // NOI18N
        jLabelImg.setName("jLabelImg"); // NOI18N

        jLabel_Img.setIcon(resourceMap.getIcon("jLabel_Img.icon")); // NOI18N
        jLabel_Img.setText(resourceMap.getString("jLabel_Img.text")); // NOI18N
        jLabel_Img.setName("jLabel_Img"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelImg)
                    .addComponent(jLabelFormula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel_Img, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVar)
                            .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFormula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelImg)
                            .addComponent(jLabel_Img, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFielduActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFielduActionPerformed
    }//GEN-LAST:event_jTextFielduActionPerformed

    private void jTextFieldDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDKeyTyped
        Execute();
    }//GEN-LAST:event_jTextFieldDKeyTyped

private void jComboBoxVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxVarItemStateChanged
    Variant var;
    try {
        int varNum = Integer.parseInt(jComboBoxVar.getSelectedItem().toString());
        var = variants.get(varNum);
    } catch (Exception e) {
        return;
    }
    jTextFieldD.setText(Double.toString(var.D));
    jTextFieldd.setText(Double.toString(var.d));
    jTextFieldk.setText(Double.toString(var.k));
    jTextFieldp.setText(Double.toString(var.ρ));
    jTextFieldu.setText(Double.toString(var.μ));
    jTextFieldw.setText(Double.toString(var.ω));
    Execute();
}//GEN-LAST:event_jComboBoxVarItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ecolabs.ChartPanel chartPanel1;
    private ecolabs.ChartPanel chartPanel2;
    private javax.swing.JComboBox jComboBoxVar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelFormula;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JLabel jLabelVar;
    private javax.swing.JLabel jLabel_Img;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFieldD;
    private javax.swing.JTextField jTextFieldd;
    private javax.swing.JTextField jTextFieldk;
    private javax.swing.JTextField jTextFieldp;
    private javax.swing.JTextField jTextFieldu;
    private javax.swing.JTextField jTextFieldw;
    // End of variables declaration//GEN-END:variables
}
