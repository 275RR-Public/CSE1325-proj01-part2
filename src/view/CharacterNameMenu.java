package view;

import java.text.ParseException;
import java.util.Scanner;

import util.ErrorHandler;

public class CharacterNameMenu extends Menu {

    public CharacterNameMenu(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Creation ***");
        System.out.println();
        System.out.println("Only alphabetic with capitalization.");
        System.out.print("Enter Character's name: ");
    }
    
    public String show() {
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            System.out.println();
            try {
                return ErrorHandler.validateName(user_input);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                displayError();
            }
        }
    }
}
