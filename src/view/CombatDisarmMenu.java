package view;

import java.util.ArrayList;
import java.util.Scanner;

import core.Creature;
import core.Map;
import core.Player;

public class CombatDisarmMenu extends Menu {
    private int round;
    private Player current_player;
    private ArrayList<Creature> sorted_list;
    private ArrayList<Creature> targets = new ArrayList<>();
    private int num_of_targets = 0;
    
    public CombatDisarmMenu(Scanner in, int round, Player current_player, ArrayList<Creature> sorted_list) {
        Menu.in = in;
        this.round = round;
        this.current_player = current_player;
        this.sorted_list = sorted_list;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MENU ***");
        System.out.println("Round " + round + ": " + current_player.getName() + " disarms!");
        System.out.println();
        for(var c : sorted_list) {
            if(c.getName().equals(current_player.getName()))
                continue;
            if(Map.canDisarm(current_player, c)) {
                num_of_targets++;
                System.out.printf("%d. %s %s\n", num_of_targets, c.getPosition(), c.getName());
                targets.add(c);
            }
        }
        if(num_of_targets == 0) {
            System.out.println(current_player.getName() + " attempts to disarm but no Players are near!");
            System.out.println();
            System.out.print("Press Enter to continue...");
            in.nextLine();
            return;
        }
        System.out.println();
        System.out.print("Select Target: ");
    }
        
    public void show() {
        while(true) {
            clearConsole();
            displayMenu();

            if(num_of_targets == 0) return;
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, num_of_targets)) {
                System.out.println();
                int input = Integer.parseInt(user_input); //1-indexed
                current_player.disarm((Player) targets.get(input - 1)); //0-indexed
                return;
            }
            else {
                displayError();
                num_of_targets = 0;
                targets.clear();
            }
        }
    }
}
