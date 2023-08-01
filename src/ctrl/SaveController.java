package ctrl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Creature;
import core.Player;
import view.SaveMenu;

public class SaveController {
    private Scanner in;
    private ArrayList<Player> player_list = new ArrayList<>();
    private Path ext_folder_path;

    public SaveController(Scanner in, List<Creature> creature_list) {
        this.in = in;
        for(var creature : creature_list) {
            if(creature instanceof Player) {
                player_list.add((Player) creature);
            }
        };
    }
    
    public void show() {
        if(player_list == null || player_list.size() < 1) {
            System.out.println("There is no Characters to save.");
            System.out.print("Press Enter to try again...");
            in.nextLine();
            return;
        }
        
        int user_selection = new SaveMenu(in, player_list).show();
        try {
            savePlayer(user_selection); 
            System.out.println("Character saved: " + player_list.get(user_selection).getName() + ".csv");
            System.out.print("Press Enter to continue...");
            in.nextLine();
        } catch (IOException e) {
            System.out.println("Error saving character.");
            System.out.print("Press Enter to continue...");
            in.nextLine();
        }

    }

    private void savePlayer(int choice) throws IOException {
        createDirs();
        Player player = player_list.get(choice);
        String player_data =    player.getName() +","+
                                player.getWeapon().getName() +","+
                                player.getWeapon().getDiceType() +","+
                                player.getWeapon().getBonus() +","+
                                player.getWeapon().getRange() +","+
                                player.getHP() +","+
                                player.getAC() +","+
                                player.getSTR() +","+
                                player.getDEX() +","+
                                player.getCON();
        
        try(PrintWriter writer = new PrintWriter(ext_folder_path.resolve(player.getName()+".csv").toString(), StandardCharsets.UTF_8)) {
            writer.println(player_data);
        }
    }

    // create dir for save files
    private void createDirs() throws IOException, SecurityException {
        System.out.println();
        try {
            File new_dir = new File("saved");
            new_dir.mkdir();

            File save_dir = new File(new_dir.toPath().resolve("players").toString());
            if(save_dir.mkdir())
                System.out.println("Directory created: " + save_dir.getPath());
            else System.out.println("Directory exists: " + save_dir.getPath());
            ext_folder_path = save_dir.toPath();

        } finally {}
    }
}
