
/*
 * Lab1JPanel.java
 *
 * Created on 19.11.2011, 13:59:10
 */
package ecolabs.labs.lab1;

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
//import org.docx4j.XmlUtils;
//import org.docx4j.openpackaging.io.SaveToZipFile;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
//import org.docx4j.wml.Document;
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
    //private static String fileName = "LabVariants_№1.txt";
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

    /** Creates new form Lab1JPanel */
    public Lab1JPanel(EcolabsView parent) {
        super(parent);
        initComponents();
        Title = "Лабораторная работа №1";
        Caption = "Расчет параметров полого форсуночного скруббера";
        labDatabaseFileName = "LabVariants_№1.txt";
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
        HashMap<Double, Double> points = new HashMap<Double, Double>();
        double H0 = Double.parseDouble(jTextFieldH0.getText());
        double dH = Double.parseDouble(jTextFielddH.getText());
        double Hn = Double.parseDouble(jTextFieldHn.getText());
        Date date2;
        for (double H = H0; H <= Hn; H += dH) {
            points.put(H, η(H, Dh2o, Dsol));
            date2 = new Date();
            if (date2.getTime() - date1.getTime() > 4000) {
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
        ArrayList<Double> Dh2os = new ArrayList<Double>();
        Dh2os.add(Double.parseDouble(jTextFieldDh2o1.getText()));
        Dh2os.add(Double.parseDouble(jTextFieldDh2o2.getText()));
        Dh2os.add(Double.parseDouble(jTextFieldDh2o3.getText()));
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Double Dh2o : Dh2os) {
            XYSeries series = new XYSeries("DH2O" + " = " + Dh2o);

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
            if (e.getMessage().equals("TimeException")) {
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
        ArrayList<String[]> lines = ScreenJPanel.loadVariants(labDatabaseFileName, 8);
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

    @Override
    public void export(File file) {
        super.export(file);

        try {
            BufferedImage image1 = chart1.createBufferedImage(500, 500);
            BufferedImage image2 = chart2.createBufferedImage(500, 500);
            File imageFile1 = new File(file.getParent()+"\\chart1Lab1.png");
            File imageFile2 = new File(file.getParent()+"\\chart2Lab1.png");
            ImageIO.write(image1, "png", imageFile1);
            ImageIO.write(image2, "png", imageFile2);
            
            String htmlText = String.format("<html>"
                    + "<head> <title> %s </title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Исходные данные:</h1>"
                    + "Объемный расход орошающей жидкости, м<sup>3</sup>/с = %s<br>"
                    + "Объемный расход газа на выходе из скруббера при рабочих условиях, м<sup>3</sup>/с = %s<br>"
                    + "<h1>Графики зависимости степени очистки газа от высоты скруббера:</h1>"
                    + "Коэффициент захвата частицы каплей воды σ: %s<br><img src=chart1Lab1.png></img><br>"
                    + "Коэффициент захвата частицы каплей воды σ: %s<br><img src=chart2Lab1.png></img><br>"
                    + "</body> "
                    + "</html>", 
                    Title + ". " + Caption,
                    jTextFieldVaq.getText(),
                    jTextFieldVa.getText(),
                    jTextFieldDsol1.getText(),
                    jTextFieldDsol2.getText());
            
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

        jComboBoxVar = new javax.swing.JComboBox();
        jLabelVar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanelPicture = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldH0 = new javax.swing.JTextField();
        jLabelH0 = new javax.swing.JLabel();
        jLabeldH = new javax.swing.JLabel();
        jTextFielddH = new javax.swing.JTextField();
        jTextFieldHn = new javax.swing.JTextField();
        jLabelHn = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelVa = new javax.swing.JLabel();
        jTextFieldVaq = new javax.swing.JTextField();
        jLabelVaq = new javax.swing.JLabel();
        jTextFieldVa = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldDh2o1 = new javax.swing.JTextField();
        jLabelDh2o1 = new javax.swing.JLabel();
        jLabeldDh2o2 = new javax.swing.JLabel();
        jTextFieldDh2o2 = new javax.swing.JTextField();
        jTextFieldDh2o3 = new javax.swing.JTextField();
        jLabelDh2o3 = new javax.swing.JLabel();
        jPanelCharts = new javax.swing.JPanel();
        jTextFieldDsol1 = new javax.swing.JTextField();
        jTextFieldDsol2 = new javax.swing.JTextField();
        jLabelDsol1 = new javax.swing.JLabel();
        chartPanel1 = new ecolabs.ChartPanel();
        chartPanel2 = new ecolabs.ChartPanel();
        jLabel3 = new javax.swing.JLabel();

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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(resourceMap.getIcon("jLabelFormula.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabelFormula.text")); // NOI18N
        jLabel1.setName("jLabelFormula"); // NOI18N

        jPanelPicture.setName("jPanelScrubber"); // NOI18N
        jPanelPicture.setOpaque(false);

        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont)); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);

        jTextFieldH0.setText(resourceMap.getString("jTextFieldH0.text")); // NOI18N
        jTextFieldH0.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldH0.setName("jTextFieldH0"); // NOI18N
        jTextFieldH0.addKeyListener(new java.awt.event.KeyAdapter() {
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

        jLabelHn.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelHn.setText(resourceMap.getString("jLabelHn.text")); // NOI18N
        jLabelHn.setToolTipText(resourceMap.getString("jLabelHn.toolTipText")); // NOI18N
        jLabelHn.setName("jLabelHn"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabeldH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldHn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFielddH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldH0, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelH0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFielddH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabeldH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont, resourceMap.getColor("jPanel2.border.titleColor"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);

        jLabelVa.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelVa.setText(resourceMap.getString("jLabelVa.text")); // NOI18N
        jLabelVa.setToolTipText(resourceMap.getString("jLabelVa.toolTipText")); // NOI18N
        jLabelVa.setName("jLabelVa"); // NOI18N

        jTextFieldVaq.setText(resourceMap.getString("jTextFieldVaq.text")); // NOI18N
        jTextFieldVaq.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldVaq.setName("jTextFieldVaq"); // NOI18N
        jTextFieldVaq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jLabelVaq.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelVaq.setText(resourceMap.getString("jLabelVaq.text")); // NOI18N
        jLabelVaq.setToolTipText(resourceMap.getString("jLabelVaq.toolTipText")); // NOI18N
        jLabelVaq.setName("jLabelVaq"); // NOI18N

        jTextFieldVa.setText(resourceMap.getString("jTextFieldVa.text")); // NOI18N
        jTextFieldVa.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldVa.setMinimumSize(new java.awt.Dimension(12, 20));
        jTextFieldVa.setName("jTextFieldVa"); // NOI18N
        jTextFieldVa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldVaq, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldVa, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabelVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextFieldVaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextFieldVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabelVa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabelVa.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabelVa.AccessibleContext.accessibleName")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, likeFont)); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);

        jTextFieldDh2o1.setText(resourceMap.getString("jTextFieldDh2o1.text")); // NOI18N
        jTextFieldDh2o1.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o1.setName("jTextFieldDh2o1"); // NOI18N
        jTextFieldDh2o1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jLabelDh2o1.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelDh2o1.setText(resourceMap.getString("jLabelDh2o1.text")); // NOI18N
        jLabelDh2o1.setToolTipText(resourceMap.getString("jLabelDh2o1.toolTipText")); // NOI18N
        jLabelDh2o1.setName("jLabelDh2o1"); // NOI18N

        jLabeldDh2o2.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabeldDh2o2.setText(resourceMap.getString("jLabeldDh2o2.text")); // NOI18N
        jLabeldDh2o2.setToolTipText(resourceMap.getString("jLabeldDh2o2.toolTipText")); // NOI18N
        jLabeldDh2o2.setName("jLabeldDh2o2"); // NOI18N

        jTextFieldDh2o2.setText(resourceMap.getString("jTextFieldDh2o2.text")); // NOI18N
        jTextFieldDh2o2.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o2.setName("jTextFieldDh2o2"); // NOI18N
        jTextFieldDh2o2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jTextFieldDh2o3.setText(resourceMap.getString("jTextFieldDh2o3.text")); // NOI18N
        jTextFieldDh2o3.setMaximumSize(new java.awt.Dimension(12, 20));
        jTextFieldDh2o3.setName("jTextFieldDh2o3"); // NOI18N
        jTextFieldDh2o3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });

        jLabelDh2o3.setFont(resourceMap.getFont("jLabeldDh2o2.font")); // NOI18N
        jLabelDh2o3.setText(resourceMap.getString("jLabelDh2o3.text")); // NOI18N
        jLabelDh2o3.setToolTipText(resourceMap.getString("jLabelDh2o3.toolTipText")); // NOI18N
        jLabelDh2o3.setName("jLabelDh2o3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabeldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFieldDh2o3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldDh2o2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDh2o1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabeldDh2o2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDh2o3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanelPictureLayout = new javax.swing.GroupLayout(jPanelPicture);
        jPanelPicture.setLayout(jPanelPictureLayout);
        jPanelPictureLayout.setHorizontalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelPictureLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelPictureLayout.setVerticalGroup(
            jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPictureLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanelCharts.setName("jPanelCharts"); // NOI18N
        jPanelCharts.setOpaque(false);
        jPanelCharts.setLayout(new java.awt.GridBagLayout());

        jTextFieldDsol1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDsol1.setText(resourceMap.getString("jTextFieldησ1.text")); // NOI18N
        jTextFieldDsol1.setName("jTextFieldησ1"); // NOI18N
        jTextFieldDsol1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        jPanelCharts.add(jTextFieldDsol2, gridBagConstraints);

        jLabelDsol1.setFont(resourceMap.getFont("jLabelDsol1.font")); // NOI18N
        jLabelDsol1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDsol1.setText(resourceMap.getString("jLabelDsol1.text")); // NOI18N
        jLabelDsol1.setToolTipText(resourceMap.getString("jLabelDsol1.toolTipText")); // NOI18N
        jLabelDsol1.setName("jLabelDsol1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 4, 0);
        jPanelCharts.add(jLabelDsol1, gridBagConstraints);

        chartPanel1.setName("chartPanel1"); // NOI18N

        javax.swing.GroupLayout chartPanel1Layout = new javax.swing.GroupLayout(chartPanel1);
        chartPanel1.setLayout(chartPanel1Layout);
        chartPanel1Layout.setHorizontalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        chartPanel1Layout.setVerticalGroup(
            chartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
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
            .addGap(0, 307, Short.MAX_VALUE)
        );
        chartPanel2Layout.setVerticalGroup(
            chartPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        jPanelCharts.add(chartPanel2, gridBagConstraints);

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        jPanelCharts.add(jLabel3, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabelVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jPanelCharts, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelCharts, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVar)
                            .addComponent(jComboBoxVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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
