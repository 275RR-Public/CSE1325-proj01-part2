package view;

import java.util.Scanner;

public class MonsterMenu extends Menu {

    public MonsterMenu(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Monster Menu ***");
        System.out.println();
        System.out.println("How many monsters would you like to create?");
        System.out.println("Pick between 1 to 7.");
        System.out.println();
        System.out.print("Enter number: ");
    }
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 7)) {
                System.out.println();
                return Integer.parseInt(user_input);
            }
            else {
                displayError();
            }
        }
    }
}