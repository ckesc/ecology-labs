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

    private void initButtons() {
        JButton[] buttons = {jButton1, jButton2, jButton3, /*jButton4, jButton5, jButton6*/};
        for (int i = 0; i < 6; i++) {
            if (parentFrame.LabScreens[i] != null) {
                buttons[i].setText(String.format(
                        "<html><b>%s</b><br>%s</html>",
                        parentFrame.LabScreens[i].Title,
                        parentFrame.LabScreens[i].Caption));
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
        jPanelOuterCenter = new javax.swing.JPanel();
        jPanelCenter = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(791, 483));
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ecolabs.EcolabsApp.class).getContext().getResourceMap(HomeJPanel.class);
        jLabelHead.setFont(resourceMap.getFont("jLabelHead.font")); // NOI18N
        jLabelHead.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHead.setText(resourceMap.getString("jLabelHead.text")); // NOI18N
        jLabelHead.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelHead.setName("jLabelHead"); // NOI18N

        jPanelOuterCenter.setName("jPanelOuterCenter"); // NOI18N
        jPanelOuterCenter.setOpaque(false);
        jPanelOuterCenter.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanelOuterCenter.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanelOuterCenterComponentResized(evt);
            }
        });
        jPanelOuterCenter.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jPanelOuterCenterPropertyChange(evt);
            }
        });
        jPanelOuterCenter.setLayout(null);

        jPanelCenter.setMaximumSize(new java.awt.Dimension(800, 600));
        jPanelCenter.setMinimumSize(new java.awt.Dimension(490, 347));
        jPanelCenter.setName("jPanelCenter"); // NOI18N
        jPanelCenter.setOpaque(false);
        jPanelCenter.setLayout(new javax.swing.BoxLayout(jPanelCenter, javax.swing.BoxLayout.PAGE_AXIS));

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setMnemonic('1');
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setActionCommand(resourceMap.getString("jButton1.actionCommand")); // NOI18N
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setOpaque(false);
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
        jButton2.setOpaque(false);
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
        jButton3.setOpaque(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabSelect(evt);
            }
        });
        jPanelCenter.add(jButton3);

        jPanelOuterCenter.add(jPanelCenter);
        jPanelCenter.setBounds(20, 0, 540, 300);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelHead, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
            .addComponent(jPanelOuterCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelHead, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelOuterCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
        );

        jLabelHead.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabelHead.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void LabSelect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabSelect

        int no = Integer.parseInt(evt.getActionCommand());
        parentFrame.ShowScreen(no);


    }//GEN-LAST:event_LabSelect

    private void jPanelOuterCenterComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelOuterCenterComponentResized
        int maxW = 700;
        int maxH = 600;
        java.awt.Dimension currentSize = jPanelCenter.getSize();
        java.awt.Dimension outerSize = jPanelOuterCenter.getSize();

        if (outerSize.width > maxW) {
            currentSize.width = maxW;
        } else {
            currentSize.width = outerSize.width;
        }

        if (outerSize.height > maxH) {
            currentSize.height = maxH;
        } else {
            currentSize.height = outerSize.height;
        }

        jPanelCenter.setSize(currentSize);    
        jPanelOuterCenter.invalidate();
        jPanelCenter.setLocation(
                (jPanelOuterCenter.getWidth() - jPanelCenter.getWidth())/2 ,
                (jPanelOuterCenter.getHeight() - jPanelCenter.getHeight())/2 );

    }//GEN-LAST:event_jPanelOuterCenterComponentResized

    private void jPanelOuterCenterPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanelOuterCenterPropertyChange
        
    }//GEN-LAST:event_jPanelOuterCenterPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabelHead;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelOuterCenter;
    // End of variables declaration//GEN-END:variables
}
