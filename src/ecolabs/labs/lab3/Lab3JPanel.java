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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.imageio.ImageIO;
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
            JFreeChart chart1;
            JFreeChart chart2;
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
    /**
     * массив D для построения графика
     */
    ArrayList<Double> constD = new ArrayList<Double>();
    /**
     * массив ω для построения графика
     */
    ArrayList<Double> constW = new ArrayList<Double>();
    private HashMap<Integer, Variant> variants = new HashMap<Integer, Variant>();    
    private final String calcResultTemplate = "<html><b>d<sub>min</sub>(D,ω)</b> = %8.2f мкм";

    /**
     * Расчет минимального размера частиц пыли, улавливаемых циклоном
     */
    private double d_min(double D, double ω) {
        return (data.d * data.k * 1000d * Math.sqrt(1000d * D * data.μ / (data.ρ * ω)));
    }

    @Override
    public void export(File file) {
        super.export(file);
                        try {
            BufferedImage image1 = chart1.createBufferedImage(500, 500);
            BufferedImage image2 = chart2.createBufferedImage(500, 500);
            File imageFile1 = new File(file.getParent()+"\\chart1Lab3.png");
            File imageFile2 = new File(file.getParent()+"\\chart2Lab3.png");
            ImageIO.write(image1, "png", imageFile1);
            ImageIO.write(image2, "png", imageFile2);
            
            String htmlText = String.format("<html>"
                    + "<head><title>%s</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Исходные данные:</h1>"
                    + "Диаметр циклона, м = %s<br>"
                    + "скорость движения газа, м/с = %s<br>"
                    + "Cредний размер частиц пыли, улавливаемых на 50, мкм = %s<br>"
                    + "Коэффициент, учитывающий тип циклона = %s<br>"
                    + "Вязкость газа, Па.c = %s<br>"
                    + "Плотность пылевых частиц, кг/м<sub>3</sub> = %s<br>"
                    + "<h1>Результаты:</h1>"
                    + "%s<br>"
                    + "<h1>Графики влияния диаметра и скорости движения газа  на минимальный размер частиц пыли, улавливаемых циклоном:</h1>"
                    + "<br><img src=chart1Lab3.png></img><br>"
                    + "<br><img src=chart2Lab3.png></img><br>"
                    + "</body> "
                    + "</html>", 
                    Title + ". " + Caption,
                    jTextFieldD.getText(),
                    jTextFieldw.getText(),
                    jTextFieldd.getText(),
                    jTextFieldk.getText(),
                    jTextFieldu.getText(),
                    jTextFieldp.getText(),
                    jLabelCalcResult.getText());
            
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

    public Lab3JPanel(EcolabsView parent) {
        super(parent);
        initComponents();
        Title = "Лабораторная работа №3";
        Caption = "Определение минимального размера частиц пыли,улавливаемых циклоном";
        labDatabaseFileName = "LabVariants_№3.txt";

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
        jTextFieldConst_DKeyReleased(null);
        jTextFieldConst_wKeyReleased(null);
        Execute();
    }

    /**
     * Вычисляет точки кривой при постоянной D
     * @param w1 начальное значение w
     * @param w2 конечное хзначение w
     * @param w_delta приращение
     */
    public HashMap<Double, Double> Calc_constD(double D, double w1, double w2, double w_delta) {
        HashMap<Double, Double> points = new HashMap<Double, Double>();

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
        HashMap<Double, Double> points = new HashMap<Double, Double>();

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
//        ArrayList<Double> D_list = new ArrayList<>();
//        D_list.add(0.2d);
//        D_list.add(0.8d);
//        D_list.add(1.4d);
//        D_list.add(2.0d);
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double curr_D : constD) {
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
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double curr_W : constW) {
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
        String myError = "Ошибка в ходе вычислений. Возможно вы ввели не правильно число";
        if (myError.equals(parentFrame.getStatus())) {
            parentFrame.setStatus(null);
        }
        try {
            data.d = Double.valueOf(jTextFieldd.getText());
            data.k = Double.valueOf(jTextFieldk.getText());
            data.ρ = Double.valueOf(jTextFieldp.getText());
            data.μ = Double.valueOf(jTextFieldu.getText());

            chart1 = new JFreeChart(createChart_Dconst());
            chart2 = new JFreeChart(createChart_Wconst());

            chartPanel1.setChart(chart1);
            chartPanel2.setChart(chart2);

            jLabelCalcResult.setText(String.format(calcResultTemplate, d_min(data.D, data.ω)));
        } catch (Exception e) {
            parentFrame.setStatus(myError);
        }
    }

    /**
     * Загрузка вариантов
     */
    public void loadVariants() {
        jComboBoxVar.setModel(new DefaultComboBoxModel());
        ArrayList<String[]> lines = ScreenJPanel.loadVariants(labDatabaseFileName, 7);
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

    void splitAndPut(String str, ArrayList<Double> list) {
        String[] strs = str.split(",");
        list.clear();
        if (parentFrame.getStatus()!= null  &&parentFrame.getStatus().split("\\p{Space}").length > 0 && parentFrame.getStatus().split("\\p{Space}")[0].equals("Ошибка")) {
            parentFrame.setStatus(null);
        }
        for (String item : strs) {
            try {
                list.add(Double.valueOf(item));
            } catch (Exception e) {
                parentFrame.setStatus(String.format("Ошибка преобразования \"%s\". Проигнорированно", item));
            }
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

        jComboBoxVar = new javax.swing.JComboBox();
        jLabelVar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        chartPanel2 = new ecolabs.ChartPanel();
        chartPanel1 = new ecolabs.ChartPanel();
        jTextFieldConst_D = new javax.swing.JTextField();
        jTextFieldConst_w = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelFormula = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel_Img = new javax.swing.JLabel();
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
        jLabelCalcResult = new javax.swing.JLabel();

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

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        chartPanel2.setName("chartPanel2"); // NOI18N

        javax.swing.GroupLayout chartPanel2Layout = new javax.swing.GroupLayout(chartPanel2);
        chartPanel2.setLayout(chartPanel2Layout);
        chartPanel2Layout.setHorizontalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        chartPanel2Layout.setVerticalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
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
            .addGap(0, 254, Short.MAX_VALUE)
        );
        chartPanel1Layout.setVerticalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(chartPanel1, gridBagConstraints);

        jTextFieldConst_D.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldConst_D.setText(resourceMap.getString("jTextFieldConst_D.text")); // NOI18N
        jTextFieldConst_D.setName("jTextFieldConst_D"); // NOI18N
        jTextFieldConst_D.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldConst_DKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jTextFieldConst_D, gridBagConstraints);

        jTextFieldConst_w.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldConst_w.setText(resourceMap.getString("jTextFieldConst_w.text")); // NOI18N
        jTextFieldConst_w.setName("jTextFieldConst_w"); // NOI18N
        jTextFieldConst_w.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldConst_wKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jTextFieldConst_w, gridBagConstraints);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 7, 0);
        jPanel2.add(jLabel8, gridBagConstraints);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(jLabel9, gridBagConstraints);

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 7, 0);
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabelFormula.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFormula.setIcon(resourceMap.getIcon("jLabelFormula.icon")); // NOI18N
        jLabelFormula.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabelFormula.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelFormula.setIconTextGap(10);
        jLabelFormula.setName("jLabelFormula"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);

        jLabel_Img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Img.setIcon(resourceMap.getIcon("jLabel_Img.icon")); // NOI18N
        jLabel_Img.setText(resourceMap.getString("jLabel_Img.text")); // NOI18N
        jLabel_Img.setName("jLabel_Img"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont)); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);

        jTextFieldD.setToolTipText(resourceMap.getString("jTextFieldD.toolTipText")); // NOI18N
        jTextFieldD.setName("jTextFieldD"); // NOI18N
        jTextFieldD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldw.setText(resourceMap.getString("jTextFieldw.text")); // NOI18N
        jTextFieldw.setToolTipText(resourceMap.getString("jTextFieldw.toolTipText")); // NOI18N
        jTextFieldw.setName("jTextFieldw"); // NOI18N
        jTextFieldw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldd.setText(resourceMap.getString("jTextFieldd.text")); // NOI18N
        jTextFieldd.setToolTipText(resourceMap.getString("jTextFieldd.toolTipText")); // NOI18N
        jTextFieldd.setName("jTextFieldd"); // NOI18N
        jTextFieldd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldk.setText(resourceMap.getString("jTextFieldk.text")); // NOI18N
        jTextFieldk.setToolTipText(resourceMap.getString("jTextFieldk.toolTipText")); // NOI18N
        jTextFieldk.setName("jTextFieldk"); // NOI18N
        jTextFieldk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jTextFieldp.setText(resourceMap.getString("jTextFieldp.text")); // NOI18N
        jTextFieldp.setToolTipText(resourceMap.getString("jTextFieldp.toolTipText")); // NOI18N
        jTextFieldp.setName("jTextFieldp"); // NOI18N
        jTextFieldp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDKeyTyped(evt);
            }
        });

        jLabel1.setLabelFor(jLabel1);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(resourceMap.getString("jLabel1.toolTipText")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setLabelFor(jLabel2);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setToolTipText(resourceMap.getString("jLabel2.toolTipText")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setLabelFor(jLabel3);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(resourceMap.getString("jLabel3.toolTipText")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setLabelFor(jLabel4);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setToolTipText(resourceMap.getString("jLabel4.toolTipText")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setLabelFor(jLabel5);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setToolTipText(resourceMap.getString("jLabel5.toolTipText")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setLabelFor(jLabel6);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setToolTipText(resourceMap.getString("jLabel6.toolTipText")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldd)
                    .addComponent(jTextFieldw)
                    .addComponent(jTextFieldD, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldu, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldk, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                            .addComponent(jLabel6)))
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
                            .addComponent(jLabel3))))
                .addContainerGap())
        );

        jLabelCalcResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCalcResult.setText(resourceMap.getString("jLabelCalcResult.text")); // NOI18N
        jLabelCalcResult.setToolTipText(resourceMap.getString("jLabelCalcResult.toolTipText")); // NOI18N
        jLabelCalcResult.setName("jLabelCalcResult"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel_Img, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelCalcResult, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel_Img)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCalcResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabelVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelFormula))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVar)
                    .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jLabelFormula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE))
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

    private void jTextFieldConst_DKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConst_DKeyReleased
        splitAndPut(jTextFieldConst_D.getText(), constD);
        Execute();
    }//GEN-LAST:event_jTextFieldConst_DKeyReleased

    private void jTextFieldConst_wKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConst_wKeyReleased
        splitAndPut(jTextFieldConst_w.getText(), constW);
        Execute();
    }//GEN-LAST:event_jTextFieldConst_wKeyReleased
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCalcResult;
    private javax.swing.JLabel jLabelFormula;
    private javax.swing.JLabel jLabelVar;
    private javax.swing.JLabel jLabel_Img;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextFieldConst_D;
    private javax.swing.JTextField jTextFieldConst_w;
    private javax.swing.JTextField jTextFieldD;
    private javax.swing.JTextField jTextFieldd;
    private javax.swing.JTextField jTextFieldk;
    private javax.swing.JTextField jTextFieldp;
    private javax.swing.JTextField jTextFieldu;
    private javax.swing.JTextField jTextFieldw;
    // End of variables declaration//GEN-END:variables
}
