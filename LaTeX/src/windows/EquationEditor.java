/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.Color;
import java.util.ArrayList;
import latex.Equation;
import latex.Latex;

/**
 *
 * @author groowy
 */
public class EquationEditor extends javax.swing.JDialog {

    private Equation equation;

    /**
     * Creates new form EquationEditor
     *
     * @param classes
     * @param equation
     */
    private boolean quited = true;

    public EquationEditor(ArrayList<String> classes, Equation equation) {
        super(new javax.swing.JFrame(), true);
        initComponents();
        this.setTitle("Edit equation");
        okButton.setEnabled(false);
    }

    public EquationEditor(ArrayList<String> classes) {
        super(new javax.swing.JFrame(), true);
        initComponents();
        this.setTitle("Add equation");
        equation = new Equation();
        okButton.setEnabled(false);
    }

    //for test purposes only
    public EquationEditor() {
        super(new javax.swing.JFrame(), true);
        initComponents();
        equation = new Equation();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        equationText = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultsText = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        categoriesChooser = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        equationPreview = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        resultsPreview = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        commentText = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("jButton1");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Equation:");

        equationText.setColumns(20);
        equationText.setRows(5);
        equationText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                equationTextKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(equationText);

        resultsText.setColumns(20);
        resultsText.setRows(5);
        resultsText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                resultsTextKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(resultsText);

        jLabel2.setText("Results:");

        categoriesChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Category:");

        jLabel4.setText("Preview:");

        equationPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        equationPreview.setText("...");

        jLabel5.setText("Preview:");

        resultsPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resultsPreview.setText("...");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        commentText.setColumns(20);
        commentText.setRows(5);
        jScrollPane3.setViewportView(commentText);

        jLabel6.setText("Comment:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(equationPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoriesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(resultsPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(okButton, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoriesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(equationPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(resultsPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkTextfields() {
        if (equationText.getText().equals("") || resultsText.getText().equals("")) {
            okButton.setEnabled(false);
        } else {
            okButton.setEnabled(true);
        }
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        quited = false;
        this.equation.setEquation(equationText.getText());
        this.equation.setResult(resultsText.getText());
        this.equation.setComment(commentText.getText());
        this.equation.setCategory((String) categoriesChooser.getSelectedItem());
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void equationTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_equationTextKeyReleased
        checkTextfields();
        try {
            equationPreview.setIcon(Latex.textToTeXIcon(equationText.getText()));
            equationText.setBackground(Color.white);
            equationPreview.setText("");
        } catch (Exception e) {
            equationText.setBackground(Color.red);
            okButton.setEnabled(false);
        }
    }//GEN-LAST:event_equationTextKeyReleased

    private void resultsTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultsTextKeyReleased
        checkTextfields();
        try {
            resultsPreview.setIcon(Latex.textToTeXIcon(resultsText.getText()));
            resultsText.setBackground(Color.white);
            resultsPreview.setText("");
        } catch (Exception e) {
            resultsText.setBackground(Color.red);
            okButton.setEnabled(false);
        }
    }//GEN-LAST:event_resultsTextKeyReleased

    public Equation getEquation() {
        return equation;
    }

    public boolean isQuited() {
        return quited;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> categoriesChooser;
    private javax.swing.JTextArea commentText;
    private javax.swing.JLabel equationPreview;
    private javax.swing.JTextArea equationText;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel resultsPreview;
    private javax.swing.JTextArea resultsText;
    // End of variables declaration//GEN-END:variables
}
