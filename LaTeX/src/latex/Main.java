package latex;

import windows.MainWindow;

/**
 *
 * @author Groowy
 */
public class Main {

    public static void main(String[] args) {
        if(args.length > 0){
            String str = args[0];
            if(str.lastIndexOf(".") < 0){
                if(str.charAt(str.length() - 1) != Latex.SEPARATOR){
                    str += Latex.SEPARATOR;
                }
                str += "equations.xml";
            }
            System.out.println(str);
            Latex.EQUATIONS_PATH = str;
        }
        System.out.println("- " + Latex.EQUATIONS_PATH);
        MainWindow window = new MainWindow();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getSize());
        window.setVisible(true);
        Latex.textToTeXIcon("x");  // Preloads LaTeX stuffs
    }

}
