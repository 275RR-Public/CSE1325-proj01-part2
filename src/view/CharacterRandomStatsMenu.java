package view;

import java.util.Scanner;

import util.GameUtility;

public class CharacterRandomStatsMenu extends Menu {
   
    int[] stats = new int[3];   //STR, DEX, CON
    private int points = 10;    //starting stat points
    
    public CharacterRandomStatsMenu(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Creation ***");
        System.out.println();
        System.out.println("STR: " + stats[0]);
        System.out.println("DEX: " + stats[1]);
        System.out.println("CON: " + stats[2]);
        System.out.println("Remaining: " + points);
        System.out.println();
        System.out.println("1. Re-Roll");
        System.out.println("2. Finish");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public int[] show() {
        while(true) {
            clearConsole();
            GameUtility.randomStats(stats, points);
            this.points = 0;
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 2)) {
                int input = Integer.parseInt(user_input);
                System.out.println();
                if(input == 2){
                    return stats;
                }
                resetStats();
            }
            else {
                displayError();
                resetStats();
            }
        }
    }

    private void resetStats() {
        points = 10;
        stats[0] = 0;
        stats[1] = 0;
        stats[2] = 0;
    }
}
