package latex;

import windows.MainWindow;

/**
 *
 * @author Groowy
 */
public class Main {

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getSize());
        window.setVisible(true);
        Latex.textToTeXIcon("x");  // Preloads LaTeX stuffs
    }

}
