package view;

import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Menu {
    
    protected static Scanner in;
    
    protected abstract void displayMenu();
    
    protected boolean verifyChoice(String user_input, int num_of_menu_options) {
        if(user_input == null) {
            return false;
        }
        String pattern = "^[1-" + num_of_menu_options + "]$";
        Pattern p = Pattern.compile(pattern);
        return p.matcher(user_input).matches();
    }
    
    protected void clearConsole() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    protected void displayError() {
        System.out.println();
        System.out.println("Invalid Input.");
        System.out.print("Press Enter to try again...");
        in.nextLine();
    }
}
