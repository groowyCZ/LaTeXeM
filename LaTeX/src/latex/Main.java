package latex;

import windows.MainWindow;

/**
 *
 * @author Groowy
 */
public class Main {

    public static void main(String[] args) {
        if(args.length > 0){
            Latex.EQUATIONS_PATH = args[0];
        }
        System.out.println("- " + Latex.EQUATIONS_PATH);
        MainWindow window = new MainWindow();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getSize());
        window.setVisible(true);
        Latex.textToTeXIcon("x");  // Preloads LaTeX stuffs
    }

}
