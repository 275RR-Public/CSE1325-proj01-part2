package view;

import java.util.Scanner;

public class GameMode extends Menu {

    public GameMode(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MODE ***");
        System.out.println();
        System.out.println("1. Play with Random Monsters (PvPvE)");
        System.out.println("2. Play with Players Only (PvP)");
        System.out.println("3. Return to Main Menu");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 3)) {
                System.out.println();
                return Integer.parseInt(user_input);
            }
            else {
                displayError();
            }
        }
    }
}
