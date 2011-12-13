/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecolabs.labs;

import ecolabs.EcolabsView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Экран - т.е. то что может быть отображено в центральной части
 * @author Ck
 */
public abstract class ScreenJPanel extends javax.swing.JPanel {

    /**
     * Заголовок короткий (для заголовка окна)
     */
    public String Title = "Лабораторные работы \"Очистка запылённого воздуха\"";
    /**
     * Полное название лабораторной исключая номер.
     */
    public String Caption = "Полное название лабораторной работы";
    /**
     * Иконка лаборатоной работы
     */
    public Icon ScreenIcon = new ImageIcon("../resources/lab_icon.png");
    /**
     * ссылка на главное окно
     */
    protected EcolabsView parentFrame;

    /**
     * Переключает на начальный экран
     */
    public void BackToHome() {
        parentFrame.ShowScreen(-1);
    }

    /**
     * Создаёт новый экран
     * @param parent ссылка на главную форму
     */
    public ScreenJPanel(EcolabsView parent) {
        this.parentFrame = parent;
        this.setOpaque(false);

    }

    private static void correntFont(JComponent comp) {
        for (Component component : comp.getComponents()) {
            try {
                Font oldFont = ((JComponent) component).getFont();
                if (oldFont.getName().equals("tahoma") || oldFont.getName().equals("sansserif")) {
                    ((JComponent) component).setFont(
                            new Font("Century Gothic",
                            oldFont.getStyle(),
                            oldFont.getSize()));
                }
                correntFont((JComponent) component);
            } catch (Exception e) {
            }
        }
    }

    public void ScreenInit() {
        correntFont(this);
    }

    /**
     * Загрузка вариантов из файла
     * Формат файла:
     *  1-я строка - заголовок таблицы
     *  в остальных: параметр 1 | параметр 2 | ... |параметр n
     * @param fileName Имя файла
     * @param parNumber Число параметров в одном варианте
     * @return 
     */
    public static ArrayList<String[]> loadVariants(String fileName, int parNumber) {
        ArrayList<String[]> lines = new ArrayList<String[]>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line == "") {
                    continue;
                }
                String[] parameters = line.split("\\|");

                if (parameters.length != parNumber) {
                    continue;
                }

                lines.add(parameters);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new RadialGradientPaint(
                new Point(getWidth() / 2, getHeight() / 2),
                Math.min(getHeight(), getWidth()),
                new float[]{0.0f, 1.0f},
                new Color[]{
                    new Color(6, 76, 160, 127),
                    this.getBackground()}));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paint(g);
    }
}
