package view;

import java.util.Scanner;

public class CharacterManualStatsMenu extends Menu {

    private int[] stats = new int[3];   //STR, DEX, CON
    private int points = 10;            //starting stat points
    private boolean loop = true;
    
    public CharacterManualStatsMenu(Scanner in) {
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
        System.out.println("1. Add STR");
        System.out.println("2. Add DEX");
        System.out.println("3. Add CON");
        System.out.println("4. Reset");
        System.out.println("5. Finish");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public int[] show() {
        while(loop) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 5)) {
                int input = Integer.parseInt(user_input);
                System.out.println();
                if(points == 0 && input < 4 ) {
                    System.out.println();
                    System.out.println("0 points left. Reset or Finish.");
                    System.out.print("Press Enter to try again...");
                    in.nextLine();
                }
                else {
                    loop = updateStats(input);
                }
            }
            else {
                displayError();
            }
        }
        return stats;
    }

    private boolean updateStats(int choice) {
        switch (choice) {
            case 1: //add str
                stats[0] += 1;
                points -= 1;
                break;
            case 2: //add dex
                stats[1] += 1;
                points -= 1;
                break;
            case 3: //add con
                stats[2] += 1;
                points -= 1;
                break;
            case 4: //reset
                stats[0] = 0;
                stats[1] = 0;
                stats[2] = 0;
                points = 10;
                break;
            case 5: //finish
                return false;
            default:
                break;
        }
        return true;
    }
}
