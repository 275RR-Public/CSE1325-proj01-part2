//https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
//https://stackoverflow.com/questions/8979914/audio-clip-wont-loop-continuously

package ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.Clip;

import core.Creature;
import core.Map;
import core.Monster;
import util.CsvReadException;
import view.GameMode;
import view.MonsterMenu;

public class GameController {
    
    private Scanner in;
    private List<Creature> creature_list;
    private Clip clip;
    private Path monster_path = new File("res").toPath().resolve("monsters.csv");

    public GameController(Scanner in, List<Creature> creature_list) {
        this.in = in;
        this.creature_list = creature_list;
    }
    
    public void show() {
        if(creature_list == null || creature_list.size() < 2) {
            System.out.println("Must create/load 2 Characters before starting new game.");
            System.out.print("Press Enter to try again...");
            in.nextLine();
            return;
        }
        
        while(true) {
            int user_selection = new GameMode(in).show();
            switch (user_selection) {
                case 1: //play PvPvE
                    int num_monsters = new MonsterMenu(in).show();
                    try {
                        loadMonsters(num_monsters);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.print("Press Enter to continue...");
                        in.nextLine();
                        if(clip != null) clip.close();
                        return;
                    }
                    new Map(in, creature_list);
                    new PvPvEController(in, creature_list, clip).show();
                    break;
                case 2: //play PvP
                    new Map(in, creature_list);
                    new PvPController(in, creature_list, clip).show();
                    break;
                case 3: //return to main menu
                    if(clip != null) clip.close();
                    return;
            }
        }
    }

    private void loadMonsters(int num_monsters) throws IOException {
        
        ArrayList<Creature> temp_list = new ArrayList<>();
        
        //load monsters from file
        try (InputStream file_stream = new FileInputStream(monster_path.toFile())) {
            InputStreamReader stream_reader = new InputStreamReader(file_stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(stream_reader);
            String line = null;
            if((line = reader.readLine()) != null) //skip header line
            while((line = reader.readLine()) != null) {
                temp_list.add(Monster.loadFromCsv(line));
            }
        } catch (IOException e) {
            final String msg = "loadMonsters: File doesn't exist.";
            throw new IOException(msg, e);
        } catch (CsvReadException e) {
            final String msg = "loadMonsters: CSV Parse issue.";
            throw new IOException(msg, e);
        }

        //randomly select monsters
        for(int i = 0; i < num_monsters; i++) {

            Boolean prev_pick = false;
            int index;

            do {
                index = (int)(Math.random() * temp_list.size());
                for(var c : creature_list) {
                    prev_pick = c.getName().equals(temp_list.get(index).getName());
                }
            } while (prev_pick);

            creature_list.add(temp_list.get(index));
        }
    }
}
