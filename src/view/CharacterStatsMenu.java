package view;

import java.util.Scanner;

public class CharacterStatsMenu extends Menu {
    
    public CharacterStatsMenu(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Creation ***");
        System.out.println();
        System.out.println("1. Manual Stats");
        System.out.println("2. Random Stats");
        System.out.println();
        System.out.print("Manual or Random Stats? ");
    }
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 2)) {
                System.out.println();
                return Integer.parseInt(user_input);
            }
            else {
                displayError();
            }
        }
    }
}
