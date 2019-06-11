package components;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import latex.Latex;

/**
 *
 * @author Groowy
 */
public class EquationRow extends JPanel{
    private int equationSize;
    private Border rightBorder;
    private JLabel equationLabel;
    private JLabel resultLabel;
    private JLabel commentLabel;
    
    public EquationRow(String equation, String result, String comment, int equationSize, Color bg) {
        super(new GridLayout());
        
        this.equationSize = equationSize;
        this.rightBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
        this.equationLabel = new JLabel(Latex.textToTeXIcon(equation, this.equationSize));
        this.equationLabel.setVerticalAlignment(JLabel.CENTER);
        this.equationLabel.setBackground(bg);
        JPanel equationPanel = new JPanel(new GridBagLayout());
        equationPanel.add(this.equationLabel);
        equationPanel.setBackground(bg);
        equationPanel.setBorder(this.rightBorder);
        this.add(equationPanel);

        this.resultLabel = new JLabel(Latex.textToTeXIcon(result, this.equationSize));
        this.resultLabel.setVerticalAlignment(JLabel.CENTER);
        resultLabel.setBackground(bg);
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.add(resultLabel);
        resultPanel.setBackground(bg);
        resultPanel.setBorder(rightBorder);
        this.add(resultPanel);

        this.commentLabel = new JLabel(comment);
        this.commentLabel.setVerticalAlignment(JLabel.CENTER);
        JPanel commentPanel = new JPanel(new GridBagLayout());
        commentPanel.add(commentLabel);
        commentPanel.setBackground(bg);
        this.add(commentPanel);
    }
    
    public EquationRow(String equation, String result, String comment, int equationSize) {
        this(equation, result, comment, equationSize, Color.WHITE);
    }
    
    public EquationRow(String equation, String result, String comment, Color bg) {
        this(equation, result, comment, 25, bg);
    }
    
    public EquationRow(String equation, String result, String comment) {
        this(equation, result, comment, 25, Color.WHITE);
    }
    
}
