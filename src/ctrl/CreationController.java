package ctrl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import core.Creature;
import core.Player;
import core.Weapon;
import view.CharacterConfirmMenu;
import view.CharacterManualStatsMenu;
import view.CharacterNameMenu;
import view.CharacterRandomStatsMenu;
import view.CharacterStatsMenu;
import view.CharacterWeaponMenu;

public class CreationController {
    
    private List<Creature> creature_list;
    private Scanner in;

    public CreationController(Scanner in, List<Creature> creature_list) {
        this.creature_list = creature_list;
        this.in = in;
    }
    
    public void show() throws FileNotFoundException {
        
        // guard for char create after 2 char already exist
        if(creature_list != null && creature_list.size() > 1) {
            System.out.println("Characters already created/loaded.");
            System.out.print("Press Enter to continue...");
            in.nextLine();
            return;
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();

        //create a character
        while(true) {
            String name;
            int[] stats;
            Weapon wpn;
            name = new CharacterNameMenu(in).show();
            int choice = new CharacterStatsMenu(in).show();
            if(choice == 1) {
                stats = new CharacterManualStatsMenu(in).show();
            }
            else {
                stats = new CharacterRandomStatsMenu(in).show();
            }
            wpn = new CharacterWeaponMenu(in).show();
            Player player = new Player(name, wpn, 20, 10, stats[0], stats[1], stats[2]);
            choice = new CharacterConfirmMenu(in, player).show();
            if(choice == 2) {   //confirm character
                creature_list.add(player);
                return;
            }
            // else reset character
        }
    }
}
