package view;

import java.util.Scanner;

public class GameMenu extends Menu {
    private int round;
    
    public GameMenu(Scanner in, int round) {
        Menu.in = in;
        this.round = round;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MENU ***");
        System.out.println("___  Round " + round + "  ___");
        System.out.println();
        System.out.println("1. Start Round");
        System.out.println("2. Return to Main Menu");
        System.out.println();
        System.out.print("Enter choice: ");
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
