/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Lab4JPanel.java
 *
 * Created on 11.12.2011, 16:14:42
 */
package ecolabs.labs.lab4;

import ecolabs.EcolabsView;
import ecolabs.labs.ScreenJPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;
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
 * @author FormularHunter
 */
public class Lab4JPanel extends ScreenJPanel {

    class Variant {
        public double µ;
        public double E;
        public double d;
        //public double w;
        public double V;
        public double F;
    }
    /**
     * Для завершения цикла если идёт слишком долго
     */
    Date date1;
        
    private Variant data = new Variant();
    private HashMap<Integer, Variant> variants = new HashMap<Integer, Variant>();
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
    JFreeChart chart1;
    JFreeChart chart2;
    
    /** Creates new form Lab4JPanel */
    public Lab4JPanel(EcolabsView parent) {
        super(parent);
        initComponents();
        Title = "Лабораторная работа №4";
        Caption = "Расчёт электрофильтра";
        labDatabaseFileName = "LabVariants_№4.txt";
    }

    @Override
    public void ScreenInit() {
        super.ScreenInit();
        loadVariants();
        Execute();
    }

    public double w(double E, double d)
    {
        return 0.059 * 10e-10 * E*E * d * Math.pow(10, -6) / data.µ;
    }
    
    public double η(double F, double E, double d){
        return (1 - (double) Math.exp( - w(E, d) * F / data.V));
    }

     /**
     * Вычисляет точки кривой
     * @return 
     */
    public HashMap<Double, Double> Calculateηd() throws Exception {
        HashMap<Double, Double> points = new HashMap<Double, Double>();
        double d0 = 0; //Double.parseDouble(jTextFieldH0.getText());
        double dh = 0.1;//Double.parseDouble(jTextFielddH.getText());
        double dn = 50;// Double.parseDouble(jTextFieldHn.getText());
        Date date2;
        for (double d = d0; d <= dn; d += dh) {
            points.put(d, η(data.F, data.E, d));
            date2 = new Date(); 
            if (date2.getTime() - date1.getTime() > 4000){
                throw new Exception("TimeException");
            }
        }

        return points;
    }

     /**
     * Вычисляет точки кривой
     * @return 
     */
    public HashMap<Double, Double> CalculateηE(double F, double d) throws Exception {
        HashMap<Double, Double> points = new HashMap<Double, Double>();
        double E0 = 0; //Double.parseDouble(jTextFieldH0.getText());
        double dE = 100;//Double.parseDouble(jTextFielddH.getText());
        double En = 300000;// Double.parseDouble(jTextFieldHn.getText());
        Date date2;
        for (double E = E0; E <= En; E += dE) {
            points.put(E, η(F, E, d));
            date2 = new Date(); 
            if (date2.getTime() - date1.getTime() > 4000){
                throw new Exception("TimeException");
            }
        }

        return points;
    }

    /**
     * Вычисляет зависимость η(F) 
     * @param Dsol
     * @return 
     */
    public CombinedDomainXYPlot createChartηd() throws Exception {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
                new NumberAxis("η(d)"));
        XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series = new XYSeries("");

            HashMap<Double, Double> points = Calculateηd();

            for (Double H : points.keySet()) {
                series.add(H, points.get(H));
            }

            dataset.addSeries(series);
        XYItemRenderer renderer = new StandardXYItemRenderer();
        XYPlot subplot = new XYPlot(dataset, null, new NumberAxis(null), renderer);
        plot.add(subplot);
        return plot;
    }
    
        /**
     * Вычисляет зависимость η(E) 
     * @param Dsol
     * @return 
     */
    public CombinedDomainXYPlot createChartηE(double d) throws Exception {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
                new NumberAxis("η(E)"));
        double F = Double.parseDouble(jTextFieldF.getText());
        XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series = new XYSeries(F);

            HashMap<Double, Double> points = CalculateηE(F, d);

            for (Double H : points.keySet()) {
                series.add(H, points.get(H));
            }

            dataset.addSeries(series);
        XYItemRenderer renderer = new StandardXYItemRenderer();
        XYPlot subplot = new XYPlot(dataset, null, new NumberAxis(null), renderer);
        plot.add(subplot);
        return plot;
    }
    
    /**
     * Загрузка вариантов
     */
    private void loadVariants() {
        jComboBoxVar.setModel(new DefaultComboBoxModel());
        ArrayList<String[]> lines = ScreenJPanel.loadVariants(labDatabaseFileName, 6);
        Variant v;
        int varNum;
        for (String[] parameters : lines) {
            try {
                v = new Variant();
                varNum = Integer.parseInt(parameters[0].trim());
                v.F = Double.parseDouble(parameters[1].trim());
                v.E = Double.parseDouble(parameters[2].trim());
                v.d = Double.parseDouble(parameters[3].trim());
                v.µ = Double.parseDouble(parameters[4].trim());
                v.V = Double.parseDouble(parameters[5].trim());
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

    /**
     * Выполнение расчётов
     */
    private void Execute() {
        try {
            data.V = Double.parseDouble(jTextFieldV.getText());
            data.µ = Double.parseDouble(jTextFieldµ.getText());
            data.d = Double.parseDouble(jTextFieldd.getText());
            data.E = Double.parseDouble(jTextFieldE.getText());
            data.F = Double.parseDouble(jTextFieldF.getText());
            
            date1 = new Date();
            chart1 = new JFreeChart(createChartηd());
            chart2 = new JFreeChart(createChartηE(data.d));
            chart1.removeLegend();
            chart2.removeLegend();
            
            chartPanel1.setChart(chart1);
            chartPanel2.setChart(chart2);

            jLabelw.setText(String.format("<html><b>ω<sub>д</sub></b> = %6.10f м/с</html>", w(data.E, data.d)));
            jLabeln.setText(String.format("<html><b>η</b> = %6.10f</html>", η(data.F, data.E, data.d)));
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

    @Override
    public void export(File file) {
        super.export(file);
                                try {
            BufferedImage image1 = chart1.createBufferedImage(500, 500);
            BufferedImage image2 = chart2.createBufferedImage(500, 500);
            File imageFile1 = new File(file.getParent()+"\\chart1Lab4.png");
            File imageFile2 = new File(file.getParent()+"\\chart2Lab4.png");
            ImageIO.write(image1, "png", imageFile1);
            ImageIO.write(image2, "png", imageFile2);
            
            String htmlText = String.format("<html>"
                    + "<head><title>%s</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Исходные данные:</h1>"
                    + "Поверхность осаждения фильтра = %s<br>"
                    + "Напряженность электрического поля = %s<br>"
                    + "Вязкость газа = %s<br>"
                    + "Диаметр частицы пыли = %s<br>"
                    + "Объём газа = %s<br>"
                    + "<h1>Результаты:</h1>"
                    + "Скорость дрейфа частиц %s<br>"
                    + "Эффективность очистки газа %s<br>"
                    + "<h1>Зависимость эффективности очистки от диаметра частицы пыли::</h1>"
                    + "<br><img src=chart1Lab4.png></img><br>"
                    + "<h1>Зависимость эффективности очистки от напряженности электирческого поля::</h1>"
                    + "<br><img src=chart2Lab4.png></img><br>"
                    + "</body> "
                    + "</html>", 
                    Title + ". " + Caption,
                    jTextFieldF.getText(),
                    jTextFieldE.getText(),
                    jTextFieldµ.getText(),
                    jTextFieldd.getText(),
                    jTextFieldV.getText(),
                    jLabelw.getText(),
                    jLabeln.getText());
            
            // Создаём поток записи
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter, true);
            printWriter.print(htmlText);
            fileWriter.close();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        jPanelCharts = new javax.swing.JPanel();
        chartPanel1 = new ecolabs.ChartPanel();
        chartPanel2 = new ecolabs.ChartPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxVar = new javax.swing.JComboBox();
        jLabelVar = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelScheme = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldF = new javax.swing.JTextField();
        jLabelDsol1 = new javax.swing.JLabel();
        jLabelDsol2 = new javax.swing.JLabel();
        jTextFieldE = new javax.swing.JTextField();
        jLabelVa = new javax.swing.JLabel();
        jTextFieldV = new javax.swing.JTextField();
        jTextFieldd = new javax.swing.JTextField();
        jLabelDh2o1 = new javax.swing.JLabel();
        jLabelDsol3 = new javax.swing.JLabel();
        jTextFieldµ = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabeln = new javax.swing.JLabel();
        jLabelw = new javax.swing.JLabel();
        jLabelFormula = new javax.swing.JLabel();
        jLabelFormula1 = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        jPanelCharts.setName("jPanelCharts"); // NOI18N
        jPanelCharts.setOpaque(false);
        jPanelCharts.setLayout(new java.awt.GridBagLayout());

        chartPanel1.setName("chartPanel1"); // NOI18N

        javax.swing.GroupLayout chartPanel1Layout = new javax.swing.GroupLayout(chartPanel1);
        chartPanel1.setLayout(chartPanel1Layout);
        chartPanel1Layout.setHorizontalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 574, Short.MAX_VALUE)
        );
        chartPanel1Layout.setVerticalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
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
            .addGap(0, 574, Short.MAX_VALUE)
        );
        chartPanel2Layout.setVerticalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        jPanelCharts.add(chartPanel2, gridBagConstraints);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(Lab4JPanel.class);
        jLabel2.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 4, 0);
        jPanelCharts.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(resourceMap.getString("jLabel3.toolTipText")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 4, 0);
        jPanelCharts.add(jLabel3, gridBagConstraints);

        jLabel1.setName("jLabel1"); // NOI18N

        jComboBoxVar.setName("jComboBoxVar"); // NOI18N
        jComboBoxVar.setOpaque(false);
        jComboBoxVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxVarItemStateChanged(evt);
            }
        });

        jLabelVar.setText(resourceMap.getString("jLabelVar.text")); // NOI18N
        jLabelVar.setName("jLabelVar"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);

        jLabelScheme.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelScheme.setIcon(resourceMap.getIcon("jLabelScheme.icon")); // NOI18N
        jLabelScheme.setText(resourceMap.getString("jLabelScheme.text")); // NOI18N
        jLabelScheme.setName("jLabelScheme"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont)); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);

        jTextFieldF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldF.setText(resourceMap.getString("jTextFieldF.text")); // NOI18N
        jTextFieldF.setName("jTextFieldF"); // NOI18N
        jTextFieldF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFActionPerformed(evt);
            }
        });
        jTextFieldF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Lab4JPanel.this.keyReleased(evt);
            }
        });

        jLabelDsol1.setFont(resourceMap.getFont("jLabelF.font")); // NOI18N
        jLabelDsol1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDsol1.setText(resourceMap.getString("jLabelF.text")); // NOI18N
        jLabelDsol1.setToolTipText(resourceMap.getString("jLabelF.toolTipText")); // NOI18N
        jLabelDsol1.setName("jLabelF"); // NOI18N

        jLabelDsol2.setFont(resourceMap.getFont("jLabelE.font")); // NOI18N
        jLabelDsol2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDsol2.setText(resourceMap.getString("jLabelE.text")); // NOI18N
        jLabelDsol2.setToolTipText(resourceMap.getString("jLabelE.toolTipText")); // NOI18N
        jLabelDsol2.setName("jLabelE"); // NOI18N

        jTextFieldE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldE.setText(resourceMap.getString("jTextFieldE.text")); // NOI18N
        jTextFieldE.setName("jTextFieldE"); // NOI18N
        jTextFieldE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Lab4JPanel.this.keyReleased(evt);
            }
        });

        jLabelVa.setText(resourceMap.getString("jLabelVa.text")); // NOI18N
        jLabelVa.setToolTipText(resourceMap.getString("jLabelVa.toolTipText")); // NOI18N
        jLabelVa.setName("jLabelVa"); // NOI18N

        jTextFieldV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldV.setText(resourceMap.getString("jTextFieldV.text")); // NOI18N
        jTextFieldV.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldV.setName("jTextFieldV"); // NOI18N
        jTextFieldV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Lab4JPanel.this.keyReleased(evt);
            }
        });

        jTextFieldd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldd.setText(resourceMap.getString("jTextFieldd.text")); // NOI18N
        jTextFieldd.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldd.setName("jTextFieldd"); // NOI18N
        jTextFieldd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Lab4JPanel.this.keyReleased(evt);
            }
        });

        jLabelDh2o1.setText(resourceMap.getString("jLabelDh2o1.text")); // NOI18N
        jLabelDh2o1.setToolTipText(resourceMap.getString("jLabelDh2o1.toolTipText")); // NOI18N
        jLabelDh2o1.setName("jLabelDh2o1"); // NOI18N

        jLabelDsol3.setFont(resourceMap.getFont("jLabelμ.font")); // NOI18N
        jLabelDsol3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDsol3.setText(resourceMap.getString("jLabelμ.text")); // NOI18N
        jLabelDsol3.setToolTipText(resourceMap.getString("jLabelμ.toolTipText")); // NOI18N
        jLabelDsol3.setName("jLabelμ"); // NOI18N

        jTextFieldµ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldµ.setText(resourceMap.getString("jTextFieldµ.text")); // NOI18N
        jTextFieldµ.setName("jTextFieldµ"); // NOI18N
        jTextFieldµ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Lab4JPanel.this.keyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelDsol3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldµ))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelDsol1)
                        .addGap(5, 5, 5)
                        .addComponent(jTextFieldF, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelDsol2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldd, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldV, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDsol1))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDsol2))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldµ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDsol3)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextFieldV.getAccessibleContext().setAccessibleName(resourceMap.getString("jTextFieldV.AccessibleContext.accessibleName")); // NOI18N
        jLabelDh2o1.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabelDh2o1.AccessibleContext.accessibleName")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont)); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);

        jLabeln.setFont(resourceMap.getFont("jLabeln.font")); // NOI18N
        jLabeln.setText(resourceMap.getString("jLabeln.text")); // NOI18N
        jLabeln.setToolTipText(resourceMap.getString("jLabeln.toolTipText")); // NOI18N
        jLabeln.setName("jLabeln"); // NOI18N

        jLabelw.setFont(resourceMap.getFont("jLabeln.font")); // NOI18N
        jLabelw.setText(resourceMap.getString("jLabelw.text")); // NOI18N
        jLabelw.setToolTipText(resourceMap.getString("jLabelw.toolTipText")); // NOI18N
        jLabelw.setName("jLabelw"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelw)
                    .addComponent(jLabeln))
                .addContainerGap(256, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabelw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabeln))
        );

        jLabelFormula.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFormula.setIcon(resourceMap.getIcon("jLabelFormula.icon")); // NOI18N
        jLabelFormula.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabelFormula.setName("jLabelFormula"); // NOI18N

        jLabelFormula1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFormula1.setIcon(resourceMap.getIcon("jLabelFormula1.icon")); // NOI18N
        jLabelFormula1.setText(resourceMap.getString("jLabelFormula1.text")); // NOI18N
        jLabelFormula1.setName("jLabelFormula1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelFormula, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
            .addComponent(jLabelFormula1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
            .addComponent(jLabelScheme, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelFormula1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFormula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelScheme)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabelVar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVar)
                    .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jTextFieldFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFActionPerformed

}//GEN-LAST:event_jTextFieldFActionPerformed

private void jComboBoxVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxVarItemStateChanged

    if (jComboBoxVar.getSelectedItem() == null)
    {
        return;
    }
    int varNum = Integer.parseInt(jComboBoxVar.getSelectedItem().toString());
    Variant var = variants.get(varNum);
    jTextFieldE.setText(Double.toString(var.E));
    jTextFieldF.setText(Double.toString(var.F));
    jTextFieldV.setText(Double.toString(var.V));
    jTextFieldd.setText(Double.toString(var.d));
    jTextFieldµ.setText(Double.toString(var.µ));
    Execute();
}//GEN-LAST:event_jComboBoxVarItemStateChanged

private void keyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyReleased
    Execute();
}//GEN-LAST:event_keyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ecolabs.ChartPanel chartPanel1;
    private ecolabs.ChartPanel chartPanel2;
    private javax.swing.JComboBox jComboBoxVar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelDh2o1;
    private javax.swing.JLabel jLabelDsol1;
    private javax.swing.JLabel jLabelDsol2;
    private javax.swing.JLabel jLabelDsol3;
    private javax.swing.JLabel jLabelFormula;
    private javax.swing.JLabel jLabelFormula1;
    private javax.swing.JLabel jLabelScheme;
    private javax.swing.JLabel jLabelVa;
    private javax.swing.JLabel jLabelVar;
    private javax.swing.JLabel jLabeln;
    private javax.swing.JLabel jLabelw;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelCharts;
    private javax.swing.JTextField jTextFieldE;
    private javax.swing.JTextField jTextFieldF;
    private javax.swing.JTextField jTextFieldV;
    private javax.swing.JTextField jTextFieldd;
    private javax.swing.JTextField jTextFieldµ;
    // End of variables declaration//GEN-END:variables
}
