package view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import core.Creature;
import core.Map;
import core.Player;

public class CombatMenu extends Menu {
    private int round;
    private Player current_player;
    private ArrayList<Creature> sorted_list;
    private int act_pts;
    private int move_pts;
    private boolean disarmed;
    private boolean loop = true;
    
    public CombatMenu(Scanner in, int round, Player current_player, ArrayList<Creature> sorted_list, int act_pts, int move_pts) {
        Menu.in = in;
        this.round = round;
        this.current_player = current_player;
        this.sorted_list = sorted_list;
        this.act_pts = act_pts;
        this.move_pts = move_pts;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MENU ***");
        System.out.println("Round " + round + ": Combat for " + current_player.getName());
        System.out.println();
        Map.drawMap(current_player, sorted_list);
        System.out.println();
        if(disarmed) {
            System.out.println("*DISARMED for " + current_player.getDisarmed() + " rounds*");
            act_pts = 0;
            System.out.println("Actions remaining: " + act_pts);
        }
        else {
            System.out.println("Actions remaining: " + act_pts);
        }
        System.out.println("Movement remaining: " + move_pts);
        System.out.println();
        System.out.println("1. Action: Attack");
        System.out.println("2. Action: Disarm");
        System.out.println("3. Move");
        System.out.println("4. View Players Stats");
        System.out.println("5. End Turn");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public int show() {
        int choice = 0;
        while(loop) {
            clearConsole();
            disarmed = isDisarmed();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 5)) {
                System.out.println();
                choice = Integer.parseInt(user_input);
                loop = !isValidChoice(choice);
            }
            else {
                displayError();
            }
        }
        return choice;
    }

    private boolean isValidChoice(int choice) {
        if(act_pts == 0 && (choice == 1 || choice == 2)) {
            System.out.println("No Actions remaining.");
            System.out.print("Press Enter to try again...");
            in.nextLine();
            return false;
        }
        else if(move_pts == 0 && choice == 3) {
            System.out.println("No Movement remaining.");
            System.out.print("Press Enter to try again...");
            in.nextLine();
            return false;
        }
        return true;
    }

    private boolean isDisarmed() {
        if(current_player.getDisarmed() == 0) {
            return false;
        }
        return true;
    }
}
