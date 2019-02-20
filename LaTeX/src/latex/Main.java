package latex;

import windows.MainWindow;

/**
 *
 * @author Groowy
 */
public class Main {

    public static void main(String[] args) {
        if(args.length > 0){
            String newPath = args[0];
            newPath = newPath.substring(0, newPath.length()-7) + "equations.xml";
            Latex.EQUATIONS_PATH = newPath;
        }
        System.out.println("- " + Latex.EQUATIONS_PATH);
        MainWindow window = new MainWindow();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getSize());
        window.setVisible(true);
        Latex.textToTeXIcon("x");  // Preloads LaTeX stuffs
    }

}
