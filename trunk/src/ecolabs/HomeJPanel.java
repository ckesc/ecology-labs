/*
 * HomeJPanel.java
 *
 * Created on 19.11.2011, 16:52:21
 */
package ecolabs;

import ecolabs.labs.ScreenJPanel;
import javax.swing.JButton;

/**
 * Панель с кнопками-лабораторными
 * @author Ск
 */
public class HomeJPanel extends ScreenJPanel {

    /** Creates new form HomeJPanel */
    public HomeJPanel(EcolabsView parent) {
        super(parent);        
        initComponents();
        initButtons();
    }
    
    private void initButtons(){        
        JButton[] buttons = {jButton1,jButton2,jButton3,jButton4,jButton5,jButton6};
        for(int i=0;i<6;i++) {
            if (parentFrame.LabScreens[i]!=null) {
                buttons[i].setText(String.format(
                        "<html><b>%s</b><br>%s</html>",
                        parentFrame.LabScreens[i].Title,
                        parentFrame.LabScreens[i].Caption
                        ));
                //buttons[i].setIcon(parentFrame.LabScreens[i].ScreenIcon);
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

        jLabelHead = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(HomeJPanel.class);
        jLabelHead.setFont(resourceMap.getFont("jLabelHead.font")); // NOI18N
        jLabelHead.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHead.setText(resourceMap.getString("jLabelHead.text")); // NOI18N
        jLabelHead.setName("jLabelHead"); // NOI18N

        jPanelCenter.setName("jPanelCenter"); // NOI18N
        jPanelCenter.setLayout(new java.awt.GridLayout(3, 2, 10, 10));

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setMnemonic('1');
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setActionCommand(resourceMap.getString("jButton1.actionCommand")); // NOI18N
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton1);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setMnemonic('2');
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setActionCommand(resourceMap.getString("jButton2.actionCommand")); // NOI18N
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton2);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setMnemonic('3');
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setActionCommand(resourceMap.getString("jButton3.actionCommand")); // NOI18N
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton3);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setMnemonic('4');
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setActionCommand(resourceMap.getString("jButton4.actionCommand")); // NOI18N
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton4);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setMnemonic('5');
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setActionCommand(resourceMap.getString("jButton5.actionCommand")); // NOI18N
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton5);

        jButton6.setIcon(resourceMap.getIcon("jButton6.icon")); // NOI18N
        jButton6.setMnemonic('6');
        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setActionCommand(resourceMap.getString("jButton6.actionCommand")); // NOI18N
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelCenter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                    .addComponent(jLabelHead, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelHead, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void LabSelect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabSelect
        
        int no = Integer.parseInt(evt.getActionCommand());
        parentFrame.ShowScreen(no);
        
        
    }//GEN-LAST:event_LabSelect
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabelHead;
    private javax.swing.JPanel jPanelCenter;
    // End of variables declaration//GEN-END:variables
}
