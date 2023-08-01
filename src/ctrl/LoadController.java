package ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Creature;
import core.Player;
import util.CsvReadException;
import view.LoadMenu;

public class LoadController {
    private Scanner in;
    private List<Creature> creature_list;
    private ArrayList<Player> loaded_player_list = new ArrayList<>();

    private List<String> file_names = new ArrayList<>();
    private Path ext_folder_path = new File("saved").toPath().resolve("players");

    public LoadController(Scanner in, List<Creature> creature_list) {
        this.in = in;
        this.creature_list = creature_list;
    }
    
    public void show() {
        
        // guard for char load after 2 char already exist
        if(creature_list != null && creature_list.size() > 1) {
            System.out.println("Characters already created/loaded.");
            System.out.print("Press Enter to continue...");
            in.nextLine();
            return;
        }

        try {
            loadFiles();
        } catch (IOException e) {
            System.out.println("No Characters in directory to load.");
            System.out.print("Press Enter to continue...");
            in.nextLine();
            return;
        }

        try {
            loadPlayers();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.print("Press Enter to continue...");
            in.nextLine();
            return;
        }
        
        new LoadMenu(in, creature_list, loaded_player_list).show();
    }

    private void loadFiles() throws IOException {
        // Get all files in data directory
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(ext_folder_path)) {
            for (Path path : directoryStream) {
                file_names.add(path.getFileName().toString());
            }
        }
    }
    
    private void loadPlayers() throws IOException {
        for(String file : file_names) {
            try (InputStream file_stream = new FileInputStream(ext_folder_path.resolve(file).toFile())) {
                InputStreamReader stream_reader = new InputStreamReader(file_stream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(stream_reader);
                String line = null;
                while((line = reader.readLine()) != null) {
                    loaded_player_list.add(Player.loadFromCsv(line));
                }
            } catch (IOException e) {
                final String msg = "loadPlayers: File doesn't exist.";
                throw new IOException(msg, e);
            } catch (CsvReadException e) {
                final String msg = "loadPlayers: CSV Parse issue.";
                throw new IOException(msg, e);
            }
        }
    }
}