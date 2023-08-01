//https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
//https://stackoverflow.com/questions/8979914/audio-clip-wont-loop-continuously

package ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

import core.Creature;
import core.Map;
import core.Monster;
import core.Player;
import core.Weapon;
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

        playBGM();
        
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
    
    private void playBGM() {
        try {
            //written to help with Linux audio
            File soundFile = new File("res/Unreal-Super-HeroIII-compressed.wav");
            AudioInputStream soundIn = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = soundIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            this.clip = (Clip)AudioSystem.getLine(info);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(soundIn);
            setVolume(.05f); //5%
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //clip.start();
        } catch (Exception e) {
            System.out.println("Unable to play background music.");
            System.out.print("Press Enter to continue...");
            in.nextLine();
        }
    }
    
    private void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
