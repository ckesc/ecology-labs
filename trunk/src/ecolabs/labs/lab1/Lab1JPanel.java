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

import ecolabs.EcolabsView;
import ecolabs.labs.ScreenJPanel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Fh
 */
public class Lab1JPanel extends ScreenJPanel {

    class Variant {

        /**
         * Расход воды для орошения газа, м3/с;
         */
        public double Vaq;
        /**
         * объемный расход газа на выходе из скруббера при рабочих условиях,
         * м3/с;
         */
        public double Va;

        public double Dh2o1;
        public double Dh2o2;
        public double Dh2o3;
        
        /**
         *  Коэффициент захвата частицы каплей воды;
         */
        public double σ1;
        public double σ2;
    }
    private final String H0Default = "0.01";
    private final String dHDefault = "0.01";
    private final String HnDefault = "2.0";
    
    /**
     * Для завершения цикла если идёт слишком долго
     */
    Date date1;
    
    private static String fileName = "LabVariants_№1.txt";
    private Variant data = new Variant();
    private HashMap<Integer, Variant> variants = new HashMap<>();
    /**
     * Точки графика для первого размера частиц
     */
    HashMap<Double, Double> points1 = new HashMap<>();
    /**
     * Точки графика для второго размера частиц
     */
    HashMap<Double, Double> points2 = new HashMap<>();
    ArrayList<XYSeries> serieses1 = new ArrayList<>();
    ArrayList<XYSeries> serieses2 = new ArrayList<>();
    JFreeChart chart1;
    JFreeChart chart2;

    /** Creates new form Lab1JPanel */
    public Lab1JPanel(EcolabsView parent) {
        super(parent);
        initComponents();
        Title = "Лабораторная работа №1";
        Caption = "Расчет параметров полого форсуночного скруббера";
    }

    @Override
    public void ScreenInit() {
        super.ScreenInit();
        loadVariants();
        Execute();
    }
    
    /**
     * Степень очистки газа
     * @param H Высота скруббера,
     * @param Dsol Средний медианный размер частиц пыли
     * @return 
     */
    public double η(double H, double Dh2o, double Dsol) {
        double numerator = -3 * data.Vaq * Dsol * H;
        double denominator = 2 * data.Va * Dh2o * 0.001f;
        return 1 - (double) Math.exp(numerator / denominator);
    }

    /**
     * Вычисляет точки кривой
     * @param Dsol
     * @return 
     */
    public HashMap<Double, Double> Calculate(double Dh2o, double Dsol) throws Exception {
        HashMap<Double, Double> points = new HashMap<>();
        double H0 = Double.parseDouble(jTextFieldH0.getText());
        double dH = Double.parseDouble(jTextFielddH.getText());
        double Hn = Double.parseDouble(jTextFieldHn.getText());
        Date date2;
        for (double H = H0; H <= Hn; H += dH) {
            points.put(H, η(H, Dh2o, Dsol));
            date2 = new Date(); 
            if (date2.getTime() - date1.getTime() > 4000){
                throw new Exception("TimeException");
            }
        }

        return points;
    }

//    /**
//     * Выводит график
//     */
//    public void showChart(CombinedDomainXYPlot parent, JPanel jLabel, boolean showLegend) {
//        JFreeChart chart = new JFreeChart(parent);
//        BufferedImage image = chart.createBufferedImage(jLabel.getWidth(), jLabel.getHeight());
//        jLabel.getGraphics().drawImage(image, 0, 0, this);
//    }

    /**
     * Вычисляет все 3 кривых на одном графике
     * @param Dsol
     * @return 
     */
    public CombinedDomainXYPlot createChart(double Dsol) throws Exception {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
                new NumberAxis("η(H)"));
        ArrayList<Double> Dh2os = new ArrayList<>();
        Dh2os.add(Double.parseDouble(jTextFieldDh2o1.getText()));
        Dh2os.add(Double.parseDouble(jTextFieldDh2o2.getText()));
        Dh2os.add(Double.parseDouble(jTextFieldDh2o3.getText()));
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double Dh2o : Dh2os) {
            XYSeries series = new XYSeries(Dh2o);

            HashMap<Double, Double> points = Calculate(Dh2o, Dsol);

            for (Double H : points.keySet()) {
                series.add(H, points.get(H));
            }

            dataset.addSeries(series);
        }
        XYItemRenderer renderer = new StandardXYItemRenderer();
        XYPlot subplot = new XYPlot(dataset, null, new NumberAxis(null), renderer);
        plot.add(subplot);
        return plot;
    }

    private void Execute() {
        try {
            data.Vaq = Double.parseDouble(jTextFieldVaq.getText());
            data.Va = Double.parseDouble(jTextFieldVa.getText());
            
            date1 = new Date();
            chart1 = new JFreeChart(createChart(Double.parseDouble(jTextFieldDsol1.getText())));
            chart2 = new JFreeChart(createChart(Double.parseDouble(jTextFieldDsol2.getText())));

            chartPanel1.setChart(chart1);
            chartPanel2.setChart(chart2);
           
            parentFrame.setStatus("Расчёты произведены");
        } catch (Exception e) {
            if (e.getMessage().equals("TimeException"))
            {
                parentFrame.setStatus("Превышено время расчётов");
            } else {
                parentFrame.setStatus("Ошибка при расчётах");
            }
        }
    }
    
    /**
     * Загрузка вариантов
     */
    public void loadVariants() {
        jComboBoxVar.setModel(new DefaultComboBoxModel());
        ArrayList<String[]> lines = ScreenJPanel.loadVariants(fileName, 8);
        Variant v;
        int varNum;
        for (String[] parameters : lines) {
            try {
                v = new Variant();
                varNum = Integer.parseInt(parameters[0].trim());
                v.Vaq = Double.parseDouble(parameters[1].trim());
                v.Va = Double.parseDouble(parameters[2].trim());
                v.Dh2o1 = Double.parseDouble(parameters[3].trim());
                v.Dh2o2 = Double.parseDouble(parameters[4].trim());
                v.Dh2o3 = Double.parseDouble(parameters[5].trim());
                v.σ1 = Double.parseDouble(parameters[6].trim());
                v.σ2 = Double.parseDouble(parameters[7].trim());
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
        jLabel1 = new javax.swing.JLabel();
        jPanelPicture = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldVaq = new javax.swing.JTextField();
        jTextFieldVa = new javax.swing.JTextField();
        jLabelVaq = new javax.swing.JLabel();
        jLabelVa = new javax.swing.JLabel();
        jTextFieldH0 = new javax.swing.JTextField();
        jTextFielddH = new javax.swing.JTextField();
        jTextFieldHn = new javax.swing.JTextField();
        jLabelH0 = new javax.swing.JLabel();
        jLabeldH = new javax.swing.JLabel();
        jLabelHn = new javax.swing.JLabel();
        jLabelDh2o1 = new javax.swing.JLabel();
        jLabeldDh2o2 = new javax.swing.JLabel();
        jLabelDh2o3 = new javax.swing.JLabel();
        jTextFieldDh2o3 = new javax.swing.JTextField();
        jTextFieldDh2o2 = new javax.swing.JTextField();
        jTextFieldDh2o1 = new javax.swing.JTextField();
        jPanelCharts = new javax.swing.JPanel();
        jTextFieldDsol1 = new javax.swing.JTextField();
        jTextFieldDsol2 = new javax.swing.JTextField();
        jLabelDsol1 = new javax.swing.JLabel();
        chartPanel1 = new ecolabs.ChartPanel();
        chartPanel2 = new ecolabs.ChartPanel();

        jComboBoxVar.setName("jComboBoxVar"); // NOI18N
        jComboBoxVar.setOpaque(false);
        jComboBoxVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxVarItemStateChanged(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(Lab1JPanel.class);
        jLabelVar.setText(resourceMap.getString("jLabelVar.text")); // NOI18N
        jLabelVar.setName("jLabelVar"); // NOI18N

        jLabel1.setIcon(resourceMap.getIcon("jLabelFormula.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabel1.setName("jLabelFormula"); // NOI18N

        jPanelPicture.setName("jPanelScrubber"); // NOI18N
        jPanelPicture.setOpaque(false);

        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldVaq.setText(resourceMap.getString("jTextFieldVaq.text")); // NOI18N
        jTextFieldVaq.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldVaq.setName("jTextFieldVaq"); // NOI18N
        jTextFieldVaq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFieldVa.setText(resourceMap.getString("jTextFieldVa.text")); // NOI18N
        jTextFieldVa.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldVa.setName("jTextFieldVa"); // NOI18N
        jTextFieldVa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jLabelVaq.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelVaq.setText(resourceMap.getString("jLabelVaq.text")); // NOI18N
        jLabelVaq.setToolTipText(resourceMap.getString("jLabelVaq.toolTipText")); // NOI18N
        jLabelVaq.setName("jLabelVaq"); // NOI18N

        jLabelVa.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelVa.setText(resourceMap.getString("jLabelVa.text")); // NOI18N
        jLabelVa.setToolTipText(resourceMap.getString("jLabelVa.toolTipText")); // NOI18N
        jLabelVa.setName("jLabelVa"); // NOI18N

        jTextFieldH0.setText(resourceMap.getString("jTextFieldH0.text")); // NOI18N
        jTextFieldH0.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldH0.setName("jTextFieldH0"); // NOI18N
        jTextFieldH0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFielddH.setText(resourceMap.getString("jTextFielddH.text")); // NOI18N
        jTextFielddH.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFielddH.setName("jTextFielddH"); // NOI18N
        jTextFielddH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFieldHn.setText(resourceMap.getString("jTextFieldHn.text")); // NOI18N
        jTextFieldHn.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldHn.setName("jTextFieldHn"); // NOI18N
        jTextFieldHn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jLabelH0.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelH0.setText(resourceMap.getString("jLabelH0.text")); // NOI18N
        jLabelH0.setToolTipText(resourceMap.getString("jLabelH0.toolTipText")); // NOI18N
        jLabelH0.setName("jLabelH0"); // NOI18N

        jLabeldH.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabeldH.setText(resourceMap.getString("jLabeldH.text")); // NOI18N
        jLabeldH.setToolTipText(resourceMap.getString("jLabeldH.toolTipText")); // NOI18N
        jLabeldH.setName("jLabeldH"); // NOI18N

        jLabelHn.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelHn.setText(resourceMap.getString("jLabelHn.text")); // NOI18N
        jLabelHn.setToolTipText(resourceMap.getString("jLabelHn.toolTipText")); // NOI18N
        jLabelHn.setName("jLabelHn"); // NOI18N

        jLabelDh2o1.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelDh2o1.setText(resourceMap.getString("jLabelDh2o1.text")); // NOI18N
        jLabelDh2o1.setToolTipText(resourceMap.getString("jLabelDh2o1.toolTipText")); // NOI18N
        jLabelDh2o1.setName("jLabelDh2o1"); // NOI18N

        jLabeldDh2o2.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabeldDh2o2.setText(resourceMap.getString("jLabeldDh2o2.text")); // NOI18N
        jLabeldDh2o2.setToolTipText(resourceMap.getString("jLabeldDh2o2.toolTipText")); // NOI18N
        jLabeldDh2o2.setName("jLabeldDh2o2"); // NOI18N

        jLabelDh2o3.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelDh2o3.setText(resourceMap.getString("jLabelDh2o3.text")); // NOI18N
        jLabelDh2o3.setToolTipText(resourceMap.getString("jLabelDh2o3.toolTipText")); // NOI18N
        jLabelDh2o3.setName("jLabelDh2o3"); // NOI18N

        jTextFieldDh2o3.setText(resourceMap.getString("jTextFieldDh2o3.text")); // NOI18N
        jTextFieldDh2o3.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o3.setName("jTextFieldDh2o3"); // NOI18N
        jTextFieldDh2o3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFieldDh2o2.setText(resourceMap.getString("jTextFieldDh2o2.text")); // NOI18N
        jTextFieldDh2o2.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o2.setName("jTextFieldDh2o2"); // NOI18N
        jTextFieldDh2o2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFieldDh2o1.setText(resourceMap.getString("jTextFieldDh2o1.text")); // NOI18N
        jTextFieldDh2o1.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o1.setName("jTextFieldDh2o1"); // NOI18N
        jTextFieldDh2o1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanelPictureLayout = new javax.swing.GroupLayout(jPanelPicture);
        jPanelPicture.setLayout(jPanelPictureLayout);
        jPanelPictureLayout.setHorizontalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPictureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabeldH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFieldVaq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFielddH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldHn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldH0, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabeldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldDh2o3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPictureLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextFieldDh2o2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jTextFieldDh2o1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jTextFieldVa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        jPanelPictureLayout.setVerticalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPictureLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabeldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelPictureLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFielddH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabeldH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelVa.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabelVa.AccessibleContext.accessibleName")); // NOI18N

        jPanelCharts.setName("jPanelCharts"); // NOI18N
        jPanelCharts.setOpaque(false);
        jPanelCharts.setLayout(new java.awt.GridBagLayout());

        jTextFieldDsol1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDsol1.setText(resourceMap.getString("jTextFieldησ1.text")); // NOI18N
        jTextFieldDsol1.setName("jTextFieldησ1"); // NOI18N
        jTextFieldDsol1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        jPanelCharts.add(jTextFieldDsol1, gridBagConstraints);

        jTextFieldDsol2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDsol2.setText(resourceMap.getString("jTextFieldησ2.text")); // NOI18N
        jTextFieldDsol2.setName("jTextFieldησ2"); // NOI18N
        jTextFieldDsol2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDsol2ActionPerformed(evt);
            }
        });
        jTextFieldDsol2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        jPanelCharts.add(jTextFieldDsol2, gridBagConstraints);

        jLabelDsol1.setFont(resourceMap.getFont("jLabelDsol1.font")); // NOI18N
        jLabelDsol1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDsol1.setText(resourceMap.getString("jLabelDsol1.text")); // NOI18N
        jLabelDsol1.setToolTipText(resourceMap.getString("jLabelDsol1.toolTipText")); // NOI18N
        jLabelDsol1.setName("jLabelDsol1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelCharts.add(jLabelDsol1, gridBagConstraints);

        chartPanel1.setName("chartPanel1"); // NOI18N

        javax.swing.GroupLayout chartPanel1Layout = new javax.swing.GroupLayout(chartPanel1);
        chartPanel1.setLayout(chartPanel1Layout);
        chartPanel1Layout.setHorizontalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );
        chartPanel1Layout.setVerticalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        jPanelCharts.add(chartPanel1, gridBagConstraints);

        chartPanel2.setName("chartPanel2"); // NOI18N

        javax.swing.GroupLayout chartPanel2Layout = new javax.swing.GroupLayout(chartPanel2);
        chartPanel2.setLayout(chartPanel2Layout);
        chartPanel2Layout.setHorizontalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 265, Short.MAX_VALUE)
        );
        chartPanel2Layout.setVerticalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        jPanelCharts.add(chartPanel2, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelVar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVar)
                            .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanelPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jTextFieldDsol2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDsol2ActionPerformed
}//GEN-LAST:event_jTextFieldDsol2ActionPerformed

private void jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKeyTyped
    Execute();
}//GEN-LAST:event_jTextFieldKeyTyped

    /**
     * Выбор элемента списка вариантов
     * @param evt 
     */
private void jComboBoxVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxVarItemStateChanged

   Variant var;
    try {
        int varNum = Integer.parseInt(jComboBoxVar.getSelectedItem().toString());
        var = variants.get(varNum);
    } catch (Exception e) {
        return;
    }
    jTextFieldDh2o1.setText(Double.toString(var.Dh2o1));
    jTextFieldDh2o2.setText(Double.toString(var.Dh2o2));
    jTextFieldDh2o3.setText(Double.toString(var.Dh2o3));
    jTextFieldDsol1.setText(Double.toString(var.σ1));
    jTextFieldDsol2.setText(Double.toString(var.σ2));
    jTextFieldH0.setText(H0Default);
    jTextFieldHn.setText(HnDefault);
    jTextFieldVa.setText(Double.toString(var.Va));
    jTextFieldVaq.setText(Double.toString(var.Vaq));
    jTextFielddH.setText(dHDefault);
    Execute();
}//GEN-LAST:event_jComboBoxVarItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ecolabs.ChartPanel chartPanel1;
    private ecolabs.ChartPanel chartPanel2;
    private javax.swing.JComboBox jComboBoxVar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDh2o1;
    private javax.swing.JLabel jLabelDh2o3;
    private javax.swing.JLabel jLabelDsol1;
    private javax.swing.JLabel jLabelH0;
    private javax.swing.JLabel jLabelHn;
    private javax.swing.JLabel jLabelVa;
    private javax.swing.JLabel jLabelVaq;
    private javax.swing.JLabel jLabelVar;
    private javax.swing.JLabel jLabeldDh2o2;
    private javax.swing.JLabel jLabeldH;
    private javax.swing.JPanel jPanelCharts;
    private javax.swing.JPanel jPanelPicture;
    private javax.swing.JTextField jTextFieldDh2o1;
    private javax.swing.JTextField jTextFieldDh2o2;
    private javax.swing.JTextField jTextFieldDh2o3;
    private javax.swing.JTextField jTextFieldDsol1;
    private javax.swing.JTextField jTextFieldDsol2;
    private javax.swing.JTextField jTextFieldH0;
    private javax.swing.JTextField jTextFieldHn;
    private javax.swing.JTextField jTextFieldVa;
    private javax.swing.JTextField jTextFieldVaq;
    private javax.swing.JTextField jTextFielddH;
    // End of variables declaration//GEN-END:variables
}
