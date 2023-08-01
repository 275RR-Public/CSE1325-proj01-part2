// https://www.javatpoint.com/how-to-clear-screen-in-java
// https://stackoverflow.com/questions/23070340/scanner-nextline-not-waiting-for-input-on-loop

package view;

import java.util.List;
import java.util.Scanner;

import core.Creature;

public class MainMenu extends Menu {
    
    private List<Creature> creature_list;
    
    public MainMenu(Scanner in, List<Creature> creature_list) {
        Menu.in = in;
        this.creature_list = creature_list;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** MAIN MENU ***");
        System.out.println();
        System.out.println("Characters: " + showLoadedCharacters());
        System.out.println();
        System.out.println("1. Start Game");
        System.out.println("2. Create Character");
        System.out.println("3. Load Character");
        System.out.println("4. Save Character");
        System.out.println("5. Quit");
        System.out.println();
        System.out.print("Enter choice: ");
    }
    
    public int show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, 5)) {
                System.out.println();
                return Integer.parseInt(user_input);
            }
            else {
                displayError();
            }
        }
    }

    private String showLoadedCharacters() {
        if(creature_list == null || creature_list.size() == 0) {
            return "None";
        }
        else if(creature_list.size() == 1) {
            return creature_list.get(0).getName();
        }
        else {
            String s = "";
            for(var c : creature_list) {
                s += c.getName() + " ";
            }
            return s;
        }
    }
}
