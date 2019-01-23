package windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.xml.stream.XMLStreamException;
import latex.Equation;
import latex.Latex;

/**
 *
 * @author Groowy
 */
public class MainWindow extends javax.swing.JFrame {

    private DefaultListModel<String> listModel;
    private int lastHoveredIndex;
    private ArrayList<String> classes;
    private ArrayList<String> categories;
    private ArrayList<Equation> equations;
    private JPopupMenu popup;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        this.setTitle("LaTeX");
        initComponents();

        //private variables init
        classes = new ArrayList();
        categories = new ArrayList();
        equations = new ArrayList();
        listModel = new DefaultListModel();
        lastHoveredIndex = -1;

        popup = new JPopupMenu();
        this.fillPopup();

        equationList.setModel(listModel);
        this.setListRenderer();
        
        //load equations
        loadFromXML();
    }
    
    private void loadFromXML(){
        try {
            equations = Latex.loadEquations();
            classes = Latex.loadClasses();
            categories = Latex.loadCategories();
        } catch (XMLStreamException | FileNotFoundException ex) {
            classes.addAll(Arrays.asList(new String[]{"1.D", "2.D", "3.D"}));
            categories.addAll(Arrays.asList(new String[]{"linear", "quadratic"}));
        }
        refreshListModel();
    }

    private void refreshListModel() {
        this.listModel = new DefaultListModel<>();
        for (Equation equation : this.equations) {
            boolean done = equation.getDoneBy().contains(String.valueOf(classChooser.getSelectedItem()));
            
            boolean statePass = (done && String.valueOf(stateChooser.getSelectedItem()).equals("Solved"))
                    || (!done && String.valueOf(stateChooser.getSelectedItem()).equals("Unsolved"))
                    || String.valueOf(stateChooser.getSelectedItem()).equals("All");
            
            boolean categoryPass = String.valueOf(categoryChooser.getSelectedItem()).equals("All")
                    || String.valueOf(categoryChooser.getSelectedItem()).equals(equation.getCategory());
            
            if (categoryPass && statePass) {
                this.listModel.addElement(equation.getEquation());
            }
        }
        equationList.repaint();
    }

    private void fillPopup() {
        JMenuItem item = new JMenuItem("Delete");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                listModel.remove(equationList.getSelectedIndex());
            }
        });
        popup.add(item);
        item = new JMenuItem("Add");
        item.addActionListener((ActionEvent e) -> {
            EquationEditor eq = new EquationEditor(classes);
            eq.setLocationRelativeTo(null);
            eq.setVisible(true);
            this.equations.add(eq.getEquation());
            refreshListModel();
        });
        popup.add(item);
    }

    private void setListRenderer() {
        equationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Icon icon = Latex.textToTeXIcon(label.getText(), 25);
                int[] yellow = {60, 100, 50};
                int[] green = {120, 100, 50};
                Color backgroundColor = lastHoveredIndex == index ? Color.getHSBColor(yellow[0], yellow[1], yellow[2]) : Color.getHSBColor(yellow[0], yellow[1] - 50, yellow[2]);
                label.setIcon(icon);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(backgroundColor);
                label.setText("");
                return label;
            }
        });
        equationList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    System.out.println(listModel.getElementAt(index));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            public void check(MouseEvent e) {
                if (e.isPopupTrigger()) { //if the event shows the menu
                    equationList.setSelectedIndex(equationList.locationToIndex(e.getPoint())); //select the item
                    popup.show(equationList, e.getX(), e.getY()); //and show the menu
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                lastHoveredIndex = -1;
                equationList.repaint();
            }
        });
        equationList.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent evt) {
                JList list = ((JList) evt.getSource());
                int index = list.locationToIndex(evt.getPoint());
                if (index != lastHoveredIndex) {
                    lastHoveredIndex = index;
                    equationList.repaint();
                }
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        equationList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        classChooser = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        categoryChooser = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        stateChooser = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        addEquationMenuItem = new javax.swing.JMenuItem();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        equationList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(equationList);

        jLabel1.setText("Class:");

        classChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        classChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classChooserActionPerformed(evt);
            }
        });

        jLabel2.setText("Category:");

        categoryChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        categoryChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryChooserActionPerformed(evt);
            }
        });

        jLabel3.setText("State:");

        stateChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Solved", "Unsolved" }));
        stateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateChooserActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        addEquationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        addEquationMenuItem.setText("Add equation");
        addEquationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEquationMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(addEquationMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(classChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(categoryChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(stateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addEquationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEquationMenuItemActionPerformed
        EquationEditor editor = new EquationEditor(classes);
        editor.setVisible(true);
        this.equations.add(editor.getEquation());
        refreshListModel();
    }//GEN-LAST:event_addEquationMenuItemActionPerformed

    private void classChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classChooserActionPerformed
        refreshListModel();
    }//GEN-LAST:event_classChooserActionPerformed

    private void categoryChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryChooserActionPerformed
        refreshListModel();
    }//GEN-LAST:event_categoryChooserActionPerformed

    private void stateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateChooserActionPerformed
        refreshListModel();
    }//GEN-LAST:event_stateChooserActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addEquationMenuItem;
    private javax.swing.JComboBox<String> categoryChooser;
    private javax.swing.JComboBox<String> classChooser;
    private javax.swing.JList<String> equationList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox<String> stateChooser;
    // End of variables declaration//GEN-END:variables
}
