package view;

import java.util.ArrayList;
import java.util.Scanner;

import core.Creature;
import util.InitiativeUtility;

public class CombatInitiativeMenu extends Menu {
    
    private int round;
    private ArrayList<Creature> creature_list;
    
    public CombatInitiativeMenu(Scanner in, ArrayList<Creature> creature_list, int round) {
        Menu.in = in;
        this.round = round;
        this.creature_list = creature_list;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MENU ***");
        System.out.println("Round " + round + ": " + "Initiative");
        System.out.println();
    }
    
    public ArrayList<Creature> show() {
        clearConsole();
        displayMenu();
        wonInitiative(creature_list);
        System.out.println();
        System.out.print("Press Enter to continue...");
        in.nextLine();
        return creature_list;
    }

    private void wonInitiative(ArrayList<Creature> creature_list) {
        int list_length = creature_list.size();
        Creature[] creatures = new Creature[list_length];
        for(int i = 0; i < list_length; i++) {
            creatures[i] = creature_list.get(i);
        }
        creature_list.clear();

        InitiativeUtility<Creature> initUtil = new InitiativeUtility<>(creatures);
        Integer[] indices = initUtil.rollInitiative();

        System.out.println();
        System.out.println("Final Order");
        for (int i = 0; i < creatures.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, creatures[indices[i]].getName());
            creature_list.add(i, creatures[indices[i]]);
        }
    }
}
