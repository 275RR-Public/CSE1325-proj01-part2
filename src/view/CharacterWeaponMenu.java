package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import core.Weapon;

public class CharacterWeaponMenu extends Menu {
    
    private ArrayList<Weapon> wpn_list = new ArrayList<Weapon>();
    
    public CharacterWeaponMenu(Scanner in) {
        Menu.in = in;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** Character Creation ***");
        System.out.println();
        int i = 1;
        for(Weapon wpn : wpn_list) {
            System.out.printf("%d. %-15sDamage: %-10sBonus To-Hit: %-5dRange: %-5d\n",
                                i, wpn.getName(), wpn.getDiceType(), wpn.getBonus(), wpn.getRange());
            i++;
        }
        System.out.println();
        System.out.print("Select Weapon: ");
    }
    
    public Weapon show() throws FileNotFoundException {
        loadWeapons();
        while(true) {
            clearConsole();
            displayMenu();
            
            String user_input = in.nextLine();
            if(verifyChoice(user_input, wpn_list.size())) {
                System.out.println();
                int input = Integer.parseInt(user_input);   //input 1 indexed
                return wpn_list.get(input - 1);             //arraylist 0 indexed
            }
            else {
                displayError();
            }
        }
    }

    private void loadWeapons() throws FileNotFoundException {
        
        File file = Paths.get("res/weapons.csv").toFile();
        Scanner freader = new Scanner(file);
        if(freader.hasNextLine()) freader.nextLine(); //skip header line
        while(freader.hasNextLine()) {
            String line = freader.nextLine();
            String[] wpn = line.split(",");
            String wpn_name = wpn[0];
            String wpn_dice = wpn[1];
            int wpn_bonus = Integer.parseInt(wpn[2]);
            int wpn_range = Integer.parseInt(wpn[3]);
            wpn_list.add(new Weapon(wpn_name, wpn_dice, wpn_bonus, wpn_range));
        }
        freader.close();
    }
}
