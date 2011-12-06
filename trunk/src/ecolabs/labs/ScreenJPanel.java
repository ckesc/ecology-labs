/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecolabs.labs;

import ecolabs.EcolabsView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Экран - т.е. то что может быть отображено в центральной части
 * @author Ck
 */
public abstract class ScreenJPanel extends javax.swing.JPanel {

    /**
     * Заголовок короткий (для заголовка окна)
     */
    public String Title = "Лабораторная работа";
    /**
     * Полное название лабораторной исключая номер.
     */
    public String Caption = "Полное название лабораторной работы";
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
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
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
}
