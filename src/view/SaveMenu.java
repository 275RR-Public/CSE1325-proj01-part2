package view;

import java.util.ArrayList;
import java.util.Scanner;

import core.Player;

public class SaveMenu extends Menu {
    
    private ArrayList<Player> player_list;
    
    public SaveMenu(Scanner in, ArrayList<Player> player_list) {
        Menu.in = in;
        this.player_list = player_list;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Save ***");
        System.out.println();
        System.out.printf("   %-15s%-15s%-4s%-4s%-4s%-4s%-4s\n", "NAME", "WEAPON", "HP", "AC", "STR", "DEX", "CON");
        int i = 1;
        for(var p : player_list) {
            System.out.printf("%d. %-15s%-15s%-4d%-4d%-4d%-4d%-4d\n", i, p.getName(),
                            p.getWeapon().getName(), p.getHP(), p.getAC(), p.getSTR(), p.getDEX(), p.getCON());
            i++;
        }
        System.out.println();
        System.out.print("Select Character to save: ");
    }
    
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, player_list.size())) {
                System.out.println();
                int input = Integer.parseInt(user_input);   //input 1 indexed
                return (input - 1);                         //arraylist 0 indexed
            }
            else {
                displayError();
            }
        }
    }
}
