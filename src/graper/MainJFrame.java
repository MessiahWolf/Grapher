/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author rcher
 */
public class MainJFrame extends javax.swing.JFrame {

    // -> Variable Declaration
    private Graph graph;
    private final Point position = new Point();
    // <- End of Variable Declaration

    public static void main(String[] args) {
        MainJFrame frame = new MainJFrame();
        frame.setVisible(true);
    }

    public MainJFrame() {

        //
        initComponents();

        //
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Bar");
        model.addElement("Ellipse");
        model.addElement("Pie");
        jComboBox1.setModel(model);
        jComboBox1.setSelectedIndex(0);
    }

    private void customPaint(Graphics monet) {

        // Paint the graph
        if (graph != null) {
            graph.paint(monet, position);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainJPanel = new javax.swing.JPanel() {
            @Override public void paint(Graphics g) {
                super.paint(g);
                customPaint((Graphics2D)g);
            }
        };
        controlJPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(420, 336));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        mainJPanel.setMaximumSize(new java.awt.Dimension(620, 430));
        mainJPanel.setMinimumSize(new java.awt.Dimension(380, 240));
        mainJPanel.setPreferredSize(new java.awt.Dimension(380, 240));
        mainJPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mainJPanelComponentResized(evt);
            }
        });
        mainJPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mainJPanelMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(mainJPanel);
        mainJPanel.setLayout(mainJPanelLayout);
        mainJPanelLayout.setHorizontalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        mainJPanelLayout.setVerticalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );

        controlJPanel.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        controlJPanel.setMinimumSize(new java.awt.Dimension(130, 24));
        controlJPanel.setPreferredSize(new java.awt.Dimension(312, 24));
        controlJPanel.setLayout(new javax.swing.BoxLayout(controlJPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Datum:");
        jLabel1.setMaximumSize(new java.awt.Dimension(48, 24));
        jLabel1.setMinimumSize(new java.awt.Dimension(48, 24));
        jLabel1.setPreferredSize(new java.awt.Dimension(48, 24));
        controlJPanel.add(jLabel1);

        jTextField1.setMaximumSize(new java.awt.Dimension(32767, 24));
        jTextField1.setMinimumSize(new java.awt.Dimension(128, 24));
        jTextField1.setPreferredSize(new java.awt.Dimension(128, 24));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        controlJPanel.add(jTextField1);
        controlJPanel.add(filler1);

        jLabel2.setText("Style:");
        jLabel2.setMaximumSize(new java.awt.Dimension(48, 24));
        jLabel2.setMinimumSize(new java.awt.Dimension(48, 24));
        jLabel2.setPreferredSize(new java.awt.Dimension(48, 24));
        controlJPanel.add(jLabel2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setMaximumSize(new java.awt.Dimension(128, 24));
        jComboBox1.setMinimumSize(new java.awt.Dimension(128, 24));
        jComboBox1.setPreferredSize(new java.awt.Dimension(128, 24));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        controlJPanel.add(jComboBox1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(controlJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void mainJPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainJPanelComponentResized

        // TODO add your handling code here:
        if (graph != null) {
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());
            repaint();
        }
    }//GEN-LAST:event_mainJPanelComponentResized

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        // TODO add your handling code here:
        final String style = String.valueOf(jComboBox1.getSelectedItem());

        // Currently supported styles
        if (style.equalsIgnoreCase("Bar")) {
            graph = new BarGraph();
            graph.init(new float[]{120.0f, 65.0f, 800f, 121.6f, 900.0f, 980.0f, 1200.0f, 1280.0f, 12, 232, 320, 535, 65, 160});
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());
        } else if (style.equalsIgnoreCase("Ellipse")) {
            graph = new ScatterGraph();
            graph.init(new float[]{1, 1.3f, 1.5f, 1.6f, 1.7f, 2, 2.4f, 2.7f, 2.76f, 3, 4, 5, 6, 7});
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());
        } else if (style.equalsIgnoreCase("Pie")) {
            graph = new PieGraph();
            graph.init(new float[]{349.0f, 540.f, 200.0f, 10.5f, 60.0f, 826f, 324f, 75f, 133f, 1000, 170});
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());
        }

        // Repaint everything.
        repaint();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void mainJPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainJPanelMouseMoved
        // TODO add your handling code here:
        position.setLocation(evt.getPoint());
        repaint();
    }//GEN-LAST:event_mainJPanelMouseMoved

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:        
        if (graph != null) {
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());
            repaint();
        }
    }//GEN-LAST:event_formComponentResized

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        if (graph != null) {

            //
            final String[] split = jTextField1.getText().split(",");
            final float[] parsed = new float[split.length];

            //
            try {

                //
                for (int i = 0; i < split.length; i++) {
                    parsed[i] = Float.parseFloat(split[i]);
                }
            } catch (NumberFormatException pe) {
                return;
            }

            //
            graph.init(parsed);
            graph.resize(mainJPanel.getWidth(), mainJPanel.getHeight());

            // Clear the text
            jTextField1.setText("");

            //
            repaint();
        }
    }//GEN-LAST:event_jTextField1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlJPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel mainJPanel;
    // End of variables declaration//GEN-END:variables
}
