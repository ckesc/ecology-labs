/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecolabs.labs;

import ecolabs.EcolabsView;

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
}
