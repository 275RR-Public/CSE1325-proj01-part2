package view;

import java.util.Scanner;
import core.Player;

public class CharacterConfirmMenu extends Menu {
    
    private Player player;
    
    public CharacterConfirmMenu(Scanner in, Player player) {
        Menu.in = in;
        this.player = player;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Creation ***");
        System.out.println();
        System.out.println(player.toString());
        System.out.println("1. Reset Character");
        System.out.println("2. Confirm and Save");
        System.out.println();
        System.out.print("Enter Choice: ");
    }
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 2)) {
                clearConsole();
                return Integer.parseInt(user_input);
            }
            else {
                displayError();
            }
        }
    }
}
