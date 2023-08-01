package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Player;
import core.Creature;

public class LoadMenu extends Menu {
    
    private List<Creature> creature_list;
    private ArrayList<Player> loaded_player_list;
    
    public LoadMenu(Scanner in, List<Creature> creature_list, ArrayList<Player> loaded_player_list) {
        Menu.in = in;
        this.creature_list = creature_list;
        this.loaded_player_list = loaded_player_list;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Load ***");
        System.out.println();
        System.out.printf("   %-15s%-15s%-4s%-4s%-4s%-4s%-4s\n", "NAME", "WEAPON", "HP", "AC", "STR", "DEX", "CON");
        int i = 1;
        for(var p : loaded_player_list) {
            System.out.printf("%d. %-15s%-15s%-4d%-4d%-4d%-4d%-4d\n", i, p.getName(),
                            p.getWeapon().getName(), p.getHP(), p.getAC(), p.getSTR(), p.getDEX(), p.getCON());
            i++;
        }
        System.out.println();
        System.out.println(i + ". Return to Main Menu");
        System.out.println();
        System.out.print("Select Character to load: ");
    }
    
    
    public void show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, loaded_player_list.size() + 1)) {
                System.out.println();
                int input = Integer.parseInt(user_input);   //input 1 indexed
                if(input == loaded_player_list.size() + 1) {
                    return;
                }
                else if(!isAlreadyLoaded(input)) {
                    //arraylist 0 indexed
                    creature_list.add(loaded_player_list.get(input - 1));
                    System.out.println(loaded_player_list.get(input - 1).getName() + " has been loaded.");
                    System.out.print("Press Enter to continue...");
                    in.nextLine();
                    return;
                }
            }
            else {
                displayError();
            }
        }
    }

    private boolean isAlreadyLoaded (int choice) {
        for(var creature : creature_list) {
            if(creature instanceof Player) {
                Player p = (Player) creature;
                //arraylist 0 indexed
                if(p.getName().equals(loaded_player_list.get(choice - 1).getName())) {
                    System.out.println(p.getName() + " has already been loaded.");
                    System.out.print("Press Enter to try again...");
                    in.nextLine();
                    return true;
                }
            }
        };
        return false;
    }
}