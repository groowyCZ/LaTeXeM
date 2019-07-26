package windows;

import components.EquationRow;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.xml.stream.XMLStreamException;
import latex.Equation;
import latex.Latex;
import lib.Comparators;

/**
 *
 * @author Groowy
 */
public class MainWindow extends javax.swing.JFrame {

    private static final String ALL_STRING = "All";
    private static final String SOLVED_STRING = "Solved";
    private static final String UNSOLVED_STRING = "Unsolved";
    private final MyMouseAdaptor MY_MOUSE_ADAPTOR = new MyMouseAdaptor();
    private final JFileChooser J_FILE_CHOOSER = new JFileChooser(System.getProperty("user.dir"));
    private DefaultListModel<String> listModel;
    private int sourceIndex;
    private int equationSize;
    private ArrayList<String> classes;
    private ArrayList<String> categories;
    private ArrayList<Equation> equations;
    private ArrayList<Integer> linkedIndexes;
    private JPopupMenu popup;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {

        // FIELDS
        this.classes = new ArrayList();
        this.categories = new ArrayList();
        this.equations = new ArrayList();
        this.linkedIndexes = new ArrayList();
        this.listModel = new DefaultListModel();
        this.sourceIndex = -1;
        this.equationSize = 25;
        
        // COMPONENTS
        this.setTitle("LaTeX");
        initComponents();
        
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + Latex.SEPARATOR + "src" + Latex.SEPARATOR + "icon.png");
        this.setIconImage(icon.getImage());
        
        popup = new JPopupMenu();
        this.fillPopup();

        equationList.setModel(listModel);
        this.setListRenderer();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveEquations();
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        // LOAD
        loadFromXML();
    }

    private void loadFromXML() {
        loadFromXML(Latex.EQUATIONS_PATH);
    }
    
    private void loadFromXML(String path) {
        try {
            this.equations = Latex.loadEquations(path);
            this.classes = Latex.loadClasses(path);
            this.categories = Latex.loadCategories(path);
        } catch (XMLStreamException | FileNotFoundException e) {
            this.classes.addAll(Arrays.asList(new String[]{"1.D", "2.D", "3.D"}));
            this.categories.addAll(Arrays.asList(new String[]{"linear", "quadratic"}));
        }
        refreshEquationList();
        refreshChoosers();
    }

    private void filterEquationList() {
        this.listModel = new DefaultListModel<>();
        this.linkedIndexes.clear();
        boolean categoryIsAll = String.valueOf(categoryChooser.getSelectedItem()).equals(MainWindow.ALL_STRING);
        boolean stateIsAll = String.valueOf(stateChooser.getSelectedItem()).equals(MainWindow.ALL_STRING);

        int index = 0;
        for (Equation equation : this.equations) {
            boolean done = equation.getDoneBy().contains(String.valueOf(classChooser.getSelectedItem()));

            boolean categoryPass = categoryIsAll || String.valueOf(categoryChooser.getSelectedItem()).equals(equation.getCategory());
            boolean statePass = stateIsAll || (done ? String.valueOf(stateChooser.getSelectedItem()).equals(MainWindow.SOLVED_STRING) : String.valueOf(stateChooser.getSelectedItem()).equals(MainWindow.UNSOLVED_STRING));

            if (categoryPass && statePass) {
                this.listModel.addElement(equation.getEquation());
                this.linkedIndexes.add(index);
            }
            index++;
        }
        this.equationList.setModel(listModel);
        this.equationList.repaint();
    }

    private void addEquation() {
        int selectedCategoryIndex = equationList.getSelectedIndex() >= 0 ? categories.indexOf(equations.get(linkedIndexes.get(equationList.getSelectedIndex())).getCategory()) : 0;
        EquationEditor eq = new EquationEditor(this.categories, selectedCategoryIndex);
        eq.setLocationRelativeTo(null);
        eq.setVisible(true);
        if (eq.isConfirmed()) {
            this.equations.add(eq.getEquation());
        }
        this.filterEquationList();
    }

    private void saveEquations() {
        saveEquations(Latex.EQUATIONS_PATH);
    }
    
    private void saveEquations(String path) {
        try {
            Latex.writeEquations(this.classes, this.categories, this.equations, path);
        } catch (XMLStreamException | IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Chyba při ukládání rovnicí", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshEquationList() {
        this.listModel = new DefaultListModel();
        this.linkedIndexes.clear();
        int index = 0;
        for (Equation equation : this.equations) {
            this.listModel.addElement(equation.getEquation());
            this.linkedIndexes.add(index);
            index++;
        }
        this.equationList.setModel(listModel);
        this.equationList.repaint();
    }

    private void refreshChoosers() {
        Collections.sort(this.classes, Comparators.STRING_COMPARATOR);
        Collections.sort(this.categories, Comparators.STRING_COMPARATOR);
        String[] cats = new String[this.categories.size() + 1];
        String[] clas = new String[this.classes.size()];
        cats[0] = MainWindow.ALL_STRING;
        for (int i = 0; i < (cats.length - 1); i++) {
            cats[i + 1] = categories.get(i);
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
        this.popup.add(item);
        item = new JMenuItem("Edit");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                int index = equationList.getSelectedIndex();
                int realIndex = this.linkedIndexes.get(index);
                Equation selectedEq = this.equations.get(realIndex);
                int selectedCategoryIndex = categories.indexOf(equations.get(linkedIndexes.get(equationList.getSelectedIndex())).getCategory());
                EquationEditor ee = new EquationEditor(this.categories, selectedCategoryIndex, selectedEq);
                ee.setLocationRelativeTo(null);
                ee.setVisible(true);
                if (ee.isConfirmed()) {
                    this.equations.set(realIndex, ee.getEquation());
                    filterEquationList();
                }
            }
        });
        this.popup.add(item);
        item = new JMenuItem("Change status");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                int index = equationList.getSelectedIndex();
                int realIndex = this.linkedIndexes.get(index);
                Equation selectedEquation = this.equations.get(realIndex);
                EquationStateEditor ese = new EquationStateEditor(this.classes, selectedEquation.getDoneBy());
                ese.setMinimumSize(ese.getSize());
                ese.setLocationRelativeTo(null);
                ese.setVisible(true);
                if(ese.isConfirmed()){
                    selectedEquation.setDoneBy(ese.getDoneBy());
                    this.equations.set(realIndex, selectedEquation);
                }
            }
        });
        this.popup.add(item);
        item = new JMenuItem("Delete");
        item.addActionListener((ActionEvent e) -> {
            if (equationList.getSelectedIndex() > -1) {
                this.equations.remove((int) this.linkedIndexes.get(this.equationList.getSelectedIndex()));
                this.filterEquationList();
            }
        });
        this.popup.add(item);
    }

    private void setListRenderer() {
        this.equationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                int realIndex = linkedIndexes.get(index);
                JLabel equationLabel = (JLabel) super.getListCellRendererComponent(list, value, realIndex, isSelected, cellHasFocus);
                int[][] blue = {{200, 100, 100}, {200, 100, 95}};
                int[][] grey = {{200, 0, 75}, {200, 0, 70}};
                //if this equation was solved by selected class set background color to green, otherwise set background color to yellow
                int[][] color = equations.get(realIndex).getDoneBy().contains(classChooser.getSelectedItem().toString()) ? blue : grey;
                int ci = sourceIndex == index ? 1 : 0;
                Color backgroundColor = Color.getHSBColor(color[ci][0]/360f, color[ci][1]/100f, color[ci][2]/100f);
                Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
                equationLabel.setHorizontalAlignment(JLabel.CENTER);
                equationLabel.setIcon(Latex.textToTeXIcon(equationLabel.getText(), equationSize));
                equationLabel.setText("");
                
                if (showResultsCheckBox.isSelected()) {
                    EquationRow row = new EquationRow(equations.get(realIndex).getEquation(), equations.get(realIndex).getResult(), equations.get(realIndex).getComment(), equationSize, backgroundColor);
                    row.setBorder(lineBorder);
                    return row;
                } else {
                    equationLabel.setBackground(backgroundColor);
                    equationLabel.setBorder(lineBorder);
                    return equationLabel;
                }
            }
        });
        this.equationList.addMouseListener(this.MY_MOUSE_ADAPTOR);
        equationList.addMouseMotionListener(this.MY_MOUSE_ADAPTOR);
    }

    private class MyMouseAdaptor extends MouseInputAdapter {

        private boolean dragging;

        @Override
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList) evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                int realIndex = linkedIndexes.get(index);
                if (index > -1) {
                    EquationViewer ev = new EquationViewer(equations.get(realIndex));
                    ev.setLocationRelativeTo(null);
                    ev.setVisible(true);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            sourceIndex = equationList.getSelectedIndex();
            this.dragging = true;

            // UPDATES
            checkPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int targetIndex = equationList.getSelectedIndex();
            if (targetIndex != sourceIndex && sourceIndex != -1) {
                int realSourceIndex = linkedIndexes.get(sourceIndex);
                int realTargetIndex = linkedIndexes.get(targetIndex);
                Equation source = equations.get(realSourceIndex);
                Equation target = equations.get(realTargetIndex);
                equations.set(realSourceIndex, target);
                equations.set(realTargetIndex, source);
                filterEquationList();
            }
            this.dragging = false;

            // UPDATES
            checkPopup(e);
            checkHover(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            sourceIndex = -1;
            equationList.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (!dragging) {
                checkHover(e);
            }
        }

        public void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) { //if the event shows the menu
                equationList.setSelectedIndex(equationList.locationToIndex(e.getPoint())); //select the item
                popup.show(equationList, e.getX(), e.getY()); //and show the menu
            }
        }

        public void checkHover(MouseEvent e) {
            JList list = ((JList) e.getSource());
            int index = list.locationToIndex(e.getPoint());
            if (index != sourceIndex) {
                sourceIndex = index;
                equationList.repaint();
            }
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

        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        equationList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        classChooser = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        categoryChooser = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        stateChooser = new javax.swing.JComboBox<>();
        showResultsCheckBox = new javax.swing.JCheckBox();
        MenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveFileMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        exitWithoutSavingMenuItem = new javax.swing.JMenuItem();
        resetStatusesMenuItem = new javax.swing.JMenuItem();
        equationMenu = new javax.swing.JMenu();
        addEquationMenuItem = new javax.swing.JMenuItem();
        addCategoryMenuItem = new javax.swing.JMenuItem();
        addClassMenuItem = new javax.swing.JMenuItem();
        changeEquationSizeMenuItem = new javax.swing.JMenuItem();

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

        stateChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { MainWindow.ALL_STRING, MainWindow.SOLVED_STRING, MainWindow.UNSOLVED_STRING }));
        stateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateChooserActionPerformed(evt);
            }
        });

        showResultsCheckBox.setText("Show results");
        showResultsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showResultsCheckBoxActionPerformed(evt);
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

        importMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        importMenuItem.setText("Import");
        importMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importMenuItem);

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exportMenuItem.setText("Export");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportMenuItem);

        exitWithoutSavingMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitWithoutSavingMenuItem.setText("Exit without saving");
        exitWithoutSavingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitWithoutSavingMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitWithoutSavingMenuItem);

        resetStatusesMenuItem.setText("Reset statuses");
        resetStatusesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetStatusesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(resetStatusesMenuItem);

        MenuBar.add(fileMenu);

        equationMenu.setText("Equation");

        addEquationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
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

        changeEquationSizeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        changeEquationSizeMenuItem.setText("Change equation size");
        changeEquationSizeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeEquationSizeMenuItemActionPerformed(evt);
            }
        });
        equationMenu.add(changeEquationSizeMenuItem);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(showResultsCheckBox)
                .addContainerGap())
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
                    .addComponent(stateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showResultsCheckBox))
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
        ArrayListEditor ale = new ArrayListEditor(this.categories, "Edit categories", "Category: ", "(?!.*, )^.*");
        ale.setMinimumSize(ale.getSize());
        ale.setLocationRelativeTo(null);
        ale.setVisible(true);
        ArrayList<String> newCategories = ale.getItems();
        if(ale.isConfirmed() && !newCategories.equals(this.categories)){
            this.categories = newCategories;
            this.refreshChoosers();
            this.filterEquationList();
        }
    }//GEN-LAST:event_addCategoryMenuItemActionPerformed

    private void addClassMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassMenuItemActionPerformed
        ArrayListEditor ale = new ArrayListEditor(this.classes, "Edit classes", "Class: ", "(?!.*, )^\\w+\\.\\w+");
        ale.setMinimumSize(ale.getSize());
        ale.setLocationRelativeTo(null);
        ale.setVisible(true);
        ArrayList<String> newClasses = ale.getItems();
        if(ale.isConfirmed() && !newClasses.equals(this.classes)){
            this.classes = ale.getItems();
            this.refreshChoosers();
            this.filterEquationList();
        }
    }//GEN-LAST:event_addClassMenuItemActionPerformed

    private void saveFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileMenuItemActionPerformed
        saveEquations();
    }//GEN-LAST:event_saveFileMenuItemActionPerformed

    private void exitWithoutSavingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitWithoutSavingMenuItemActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_exitWithoutSavingMenuItemActionPerformed

    private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMenuItemActionPerformed
        int r = J_FILE_CHOOSER.showOpenDialog(this);
        if(r == 0){
            loadFromXML(J_FILE_CHOOSER.getSelectedFile().toString());
        }
    }//GEN-LAST:event_importMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        int r = J_FILE_CHOOSER.showSaveDialog(this);
        if(r == 0){
            saveEquations(J_FILE_CHOOSER.getSelectedFile().toString());
        }
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void resetStatusesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetStatusesMenuItemActionPerformed
        int r = JOptionPane.showOptionDialog(this, "You are about to reset the status of ALL equations!", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        if(r == 0){
            for(int i = 0; i < equations.size(); ++i){
                Equation eq = equations.get(i);
                eq.setDoneBy(new ArrayList());
                equations.set(i, eq);
            }
        }
    }//GEN-LAST:event_resetStatusesMenuItemActionPerformed

    private void showResultsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showResultsCheckBoxActionPerformed
        refreshEquationList();
    }//GEN-LAST:event_showResultsCheckBoxActionPerformed

    private void changeEquationSizeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeEquationSizeMenuItemActionPerformed
       SizeSelectorDialog selector = new SizeSelectorDialog("Set equation size", "Set site", 1, 500, this.equationSize);
       if(!selector.isCanceled()){
           this.equationSize = selector.getValue();
           refreshEquationList();
       }
    }//GEN-LAST:event_changeEquationSizeMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem addCategoryMenuItem;
    private javax.swing.JMenuItem addClassMenuItem;
    private javax.swing.JMenuItem addEquationMenuItem;
    private javax.swing.JComboBox<String> categoryChooser;
    private javax.swing.JMenuItem changeEquationSizeMenuItem;
    private javax.swing.JComboBox<String> classChooser;
    private javax.swing.JList<String> equationList;
    private javax.swing.JMenu equationMenu;
    private javax.swing.JMenuItem exitWithoutSavingMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem resetStatusesMenuItem;
    private javax.swing.JMenuItem saveFileMenuItem;
    private javax.swing.JCheckBox showResultsCheckBox;
    private javax.swing.JComboBox<String> stateChooser;
    // End of variables declaration//GEN-END:variables
}
