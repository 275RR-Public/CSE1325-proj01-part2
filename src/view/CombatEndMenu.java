package view;

import java.util.Scanner;

import core.Player;

public class CombatEndMenu extends Menu {
    private int round;
    private Player current_player;
    private int act_pts;
    private int move_pts;
    
    public CombatEndMenu(Scanner in, int round, Player current_player, int act_pts, int move_pts) {
        Menu.in = in;
        this.round = round;
        this.current_player = current_player;
        this.act_pts = act_pts;
        this.move_pts = move_pts;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** GAME MENU ***");
        System.out.println("Round " + round + ": Combat for " + current_player.getName());
        System.out.println();
        System.out.println("Actions remaining: " + act_pts);
        System.out.println("Movement remaining: " + move_pts);
        System.out.println();
        System.out.println("Actions or Movement remaining!");
        System.out.println("Are you sure you want to end your turn?");
        System.out.println("1. Yes, end Turn");
        System.out.println("2. No, use Remaining Points");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public boolean show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 2)) {
                System.out.println();
                int choice = Integer.parseInt(user_input);
                if(choice == 1)
                    return false;
                return true;
            }
            else {
                displayError();
            }
        }
    }
}
