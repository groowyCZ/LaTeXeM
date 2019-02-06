package windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

        // FIELDS
        classes = new ArrayList();
        categories = new ArrayList();
        equations = new ArrayList();
        listModel = new DefaultListModel();
        lastHoveredIndex = -1;

        // COMPONENTS
        this.setTitle("LaTeX");
        initComponents();

        popup = new JPopupMenu();
        this.fillPopup();

        equationList.setModel(listModel);
        this.setListRenderer();

        // LOAD
        loadFromXML();
    }

    private void loadFromXML() {
        try {
            equations = Latex.loadEquations();
            classes = Latex.loadClasses();
            categories = Latex.loadCategories();
        } catch (XMLStreamException | FileNotFoundException e) {
            classes.addAll(Arrays.asList(new String[]{"1.D", "2.D", "3.D"}));
            categories.addAll(Arrays.asList(new String[]{"All", "linear", "quadratic"}));
        }
        refreshEquationList();
        refreshChoosers();
    }

    private void filterEquationList() {
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
        equationList.setModel(listModel);
        equationList.repaint();
    }

    private void addEquation() {
        EquationEditor eq = new EquationEditor(this.categories);
        eq.setLocationRelativeTo(null);
        eq.setVisible(true);
        if (!eq.isQuited()) {
            this.equations.add(eq.getEquation());
        }
        refreshEquationList();
    }

    private void refreshEquationList() {
        this.listModel = new DefaultListModel<>();
        for (Equation equation : this.equations) {
            listModel.addElement(equation.getEquation());
        }
        equationList.setModel(listModel);
        equationList.repaint();
    }

    private void refreshChoosers() {
        String[] cats = new String[this.categories.size()];
        String[] clas = new String[this.classes.size()];
        for (int i = 0; i < cats.length; i++) {
            cats[i] = categories.get(i);
        }
        for (int i = 0; i < clas.length; i++) {
            clas[i] = classes.get(i);
        }
        this.categoryChooser.setModel(new DefaultComboBoxModel(cats));
        this.classChooser.setModel(new DefaultComboBoxModel(clas));
    }

    private void fillPopup() {
        JMenuItem item = new JMenuItem("Add");
        item.addActionListener((ActionEvent e) -> {
            addEquation();
        });
        popup.add(item);
        item = new JMenuItem("Edit");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                int index = equationList.getSelectedIndex();
                Equation selectedEq = this.equations.get(index);
                EquationEditor ee = new EquationEditor(this.categories, selectedEq);
                ee.setLocationRelativeTo(null);
                ee.setVisible(true);
                if (!ee.isQuited()) {
                    this.equations.remove(index);
                    this.equations.add(index, ee.getEquation());
                    refreshEquationList();
                }
            }
        });
        popup.add(item);
        item = new JMenuItem("Change status");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                int index = equationList.getSelectedIndex();
                Equation selectedEquation = this.equations.get(index);
                EquationStateEditor ese = new EquationStateEditor(this.classes, selectedEquation.getDoneBy());
                ese.setMinimumSize(ese.getSize());
                ese.setLocationRelativeTo(null);
                ese.setVisible(true);
                selectedEquation.setDoneBy(ese.getDoneBy());
                this.equations.remove(index);
                this.equations.add(index, selectedEquation);
            }
        });
        popup.add(item);
        item = new JMenuItem("Delete");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                this.equations.remove(equationList.getSelectedIndex());
                refreshEquationList();
            }
        });
        popup.add(item);
    }

    private void setListRenderer() {
        equationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Icon icon = Latex.textToTeXIcon(label.getText(), 25);
                float[] yellow = {120, 100, 100};
                float[] green = new float[3];
                Color.RGBtoHSB(0, 200, 0, green);
                //if this euation was solved by selected class set background color to green, otherwise set background color to yellow
                float[] color = equations.get(index).getDoneBy().contains(String.valueOf(classChooser.getSelectedItem())) ? green : yellow;
                Color backgroundColor = lastHoveredIndex == index ? Color.getHSBColor(color[0], color[1], color[2]) : Color.getHSBColor(color[0], color[1], color[2] - 50);
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
                    if (index > -1) {
                        EquationViewer ev = new EquationViewer(equations.get(index));
                        ev.setLocationRelativeTo(null);
                        ev.setVisible(true);
                    }
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
        MenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveFileMenuItem = new javax.swing.JMenuItem();
        equationMenu = new javax.swing.JMenu();
        addEquationMenuItem = new javax.swing.JMenuItem();
        addCategoryMenuItem = new javax.swing.JMenuItem();
        addClassMenuItem = new javax.swing.JMenuItem();

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

        fileMenu.setText("File");

        saveFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveFileMenuItem.setText("Save");
        saveFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveFileMenuItem);

        MenuBar.add(fileMenu);

        equationMenu.setText("Equation");

        addEquationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        addEquationMenuItem.setText("Add equation");
        addEquationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEquationMenuItemActionPerformed(evt);
            }
        });
        equationMenu.add(addEquationMenuItem);

        addCategoryMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        addCategoryMenuItem.setText("Add/remove category");
        addCategoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryMenuItemActionPerformed(evt);
            }
        });
        equationMenu.add(addCategoryMenuItem);

        addClassMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        addClassMenuItem.setText("Add/remove class");
        addClassMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassMenuItemActionPerformed(evt);
            }
        });
        equationMenu.add(addClassMenuItem);

        MenuBar.add(equationMenu);

        setJMenuBar(MenuBar);

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
        addEquation();
    }//GEN-LAST:event_addEquationMenuItemActionPerformed

    private void classChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classChooserActionPerformed
        filterEquationList();
    }//GEN-LAST:event_classChooserActionPerformed

    private void categoryChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryChooserActionPerformed
        filterEquationList();
    }//GEN-LAST:event_categoryChooserActionPerformed

    private void stateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateChooserActionPerformed
        filterEquationList();
    }//GEN-LAST:event_stateChooserActionPerformed

    private void addCategoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryMenuItemActionPerformed
        ArrayListEditor ale = new ArrayListEditor(this.categories, "Edit categories", "Category: ");
        ale.setMinimumSize(ale.getSize());
        ale.setLocationRelativeTo(null);
        ale.setVisible(true);
        this.categories = ale.getItems();
        this.refreshChoosers();
    }//GEN-LAST:event_addCategoryMenuItemActionPerformed

    private void addClassMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassMenuItemActionPerformed
        ArrayListEditor ale = new ArrayListEditor(this.classes, "Edit classes", "Class: ");
        ale.setMinimumSize(ale.getSize());
        ale.setLocationRelativeTo(null);
        ale.setVisible(true);
        this.categories = ale.getItems();
        this.refreshChoosers();
    }//GEN-LAST:event_addClassMenuItemActionPerformed

    private void saveFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileMenuItemActionPerformed
        try {
            //these temp variables are here to prevent null pointer exception because of referencing error
            ArrayList<String> tmp_cl = this.classes;
            ArrayList<String> tmp_cat = this.categories;
            ArrayList<Equation> tmp_eq = this.equations;
            Latex.writeEquations(tmp_cl, tmp_cat, tmp_eq);
        } catch (XMLStreamException | IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error occured when writing equations", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveFileMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem addCategoryMenuItem;
    private javax.swing.JMenuItem addClassMenuItem;
    private javax.swing.JMenuItem addEquationMenuItem;
    private javax.swing.JComboBox<String> categoryChooser;
    private javax.swing.JComboBox<String> classChooser;
    private javax.swing.JList<String> equationList;
    private javax.swing.JMenu equationMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem saveFileMenuItem;
    private javax.swing.JComboBox<String> stateChooser;
    // End of variables declaration//GEN-END:variables
}
