package util;
// read files on systems or in .jar
// https://stackoverflow.com/questions/15749192/how-do-i-load-a-file-from-resource-folder
// https://mkyong.com/java/java-read-a-file-from-resources-folder/
// https://stackoverflow.com/questions/69224931/file-separator-and-work-differently-in-a-runnable-jar
// linux issue reading files in jar for audio streams
// https://stackoverflow.com/questions/20580025/java-io-ioexception-mark-reset-not-supported-java-audio-input-stream-buffered
// audio info
// https://www.baeldung.com/java-play-sound
// File to Path and Path to File
// https://mkyong.com/java/java-convert-file-to-path/
// resource to file copy
// https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar/44077426#44077426


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import core.Player;
import core.Weapon;

public class LoadSave {
    public static File ext_img_folder;
    public static File ext_data_folder;
    public static File ext_bgm_folder;

    public static Path ext_img_folder_path;
    public static Path ext_data_folder_path;
    public static Path ext_bgm_folder_path;

    // colored text in terminal
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final String root_path = getRootPath();
    // RESOURCES ARE NOT PATHS - ALWAYS USE "/"
    private static final String data_res = "res/data/";
    private static final String bgm_res = "res/bgm/";
    private static final String img_res = "res/img/";

    private static List<Weapon> weapons = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static Clip clip;
    
    // create dir for program img, save, bgm files
    // must be external dir so both normal and jar exe work
    public static void createGameDirs() {
        System.out.println();
        try {
            File new_dir = new File("GameFiles");
            new_dir.mkdir();

            File img_dir = new File(new_dir.toPath().resolve("img").toString());
            if(img_dir.mkdir())
                System.out.println("Directory created: " + img_dir.getPath());
            else System.out.println("Directory exists: " + img_dir.getPath());
            LoadSave.ext_img_folder = img_dir.getCanonicalFile();
            LoadSave.ext_img_folder_path = img_dir.toPath();

            File data_dir = new File(new_dir.toPath().resolve("data").toString());
            if(data_dir.mkdir())
                System.out.println("Directory created: " + data_dir.getPath());
            else System.out.println("Directory exists: " + data_dir.getPath());
            LoadSave.ext_data_folder = data_dir.getCanonicalFile();
            LoadSave.ext_data_folder_path = data_dir.toPath();

            File bgm_dir = new File(new_dir.toPath().resolve("bgm").toString());
            if(bgm_dir.mkdir())
                System.out.println("Directory created: " + bgm_dir.getPath());
            else System.out.println("Directory exists: " + bgm_dir.getPath());
            LoadSave.ext_bgm_folder = bgm_dir.getCanonicalFile();
            LoadSave.ext_bgm_folder_path = bgm_dir.toPath();

        } catch (SecurityException e) {
            System.out.println("createConfDir: dir - Permission Denied.");
        } catch (IOException e) {
            System.out.println("createConfDir: dir path parse issue.");
        }
    }

    // copy ALL program resources to external dir (Jar/nonJar)
    // Jar(jar:file:)   bin/res/data/weapons.csv
    // nonJar(file:)    res/data/weapons.csv
    // Resources in Jar are not files in hierarchy. They are zipped.
    // jar:file:/FULL/PATH/TO/jarName.jar!/PACKAGE/HIERARCHY/TO/CLASS/className.class
    public static void createGameFiles() {
        System.out.println();
        int img_fails = createFiles(img_res, ext_img_folder_path, false);
        int data_fails = createFiles(data_res, ext_data_folder_path, true);
        int bgm_fails = createFiles(bgm_res, ext_bgm_folder_path, false);
        if(img_fails != 0)
            System.out.println(ANSI_RED + "Images failed to copy: " + ANSI_RESET + img_fails);
        if(data_fails != 0)
            System.out.println(ANSI_RED + "Data files failed to copy: " + ANSI_RESET + data_fails);
        if(bgm_fails != 0)
            System.out.println(ANSI_RED + "BGM files failed to copy: " + ANSI_RESET + bgm_fails);
        if(img_fails == 0 && data_fails == 0 && bgm_fails == 0)
            System.out.println(ANSI_GREEN + "All files copied successfully or already exist." + ANSI_RESET);
    }

    // helper for createGameFiles method (Jar/nonJar)
    // uses copy and getResource methods below
    private static int createFiles(String resource, Path dest_folder, Boolean overwrite) {
        int counter = 0;
        List<Path> paths = getResourcePaths(resource);
        for(var path : paths){
            String filename = path.getFileName().toString();
            InputStream source = getContext().getResourceAsStream(resource + filename);
            Path destination = dest_folder.resolve(filename);
            //skip if file already exists
            if(Files.notExists(destination))
                if(!copy(source, destination, overwrite))
                    counter++;
        }
        return counter;
    }

    // copy resource file to external dir (Jar/nonJar)
    public static boolean copy(InputStream source , Path destination, Boolean overwrite) {
        boolean success = false;
        System.out.println("Copying ----> " + source + "\n\tto -> " + destination);

        try {
            if(overwrite)
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            else
                Files.copy(source, destination);
            success = true;
        } catch (IOException ex) {
            System.out.println("copy: IO could not copy");
        } catch (SecurityException e) {
            System.out.println("copy: file - Permission Denied.");
        } catch (NullPointerException e) {
            System.out.println("copy: source is null");
        }
        return success;
    }
    
    //gets all paths from resource folder (Jar/nonJar)
    public static List<Path> getResourcePaths(String folder) {
        List<Path> result = null;
        URI uri = null;
        Path dirPath;
        try {
            uri = getContext().getResource(folder).toURI();
            dirPath = Paths.get(uri);
            System.out.println("*** RESOURCES NOT JAR ***");
            result = Files.walk(dirPath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        } catch (FileSystemNotFoundException e) {
            // If this is thrown, then we are running the JAR directly
            System.out.println("*** RESOURCES IN JAR ***");
            try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                result = Files.walk(fs.getPath(folder))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
            } catch (IOException ex) {
                System.out.println("getResourcePaths: IN JAR: parse issue");
            }
        } catch (URISyntaxException e) {
            System.out.println("getResourcePaths: URI parse issue");
        } catch (IOException e) {
            System.out.println("getResourcePaths: NOT JAR: parse issue");  
        }
        return result;
    }

    // get root path of the running program (Jar/nonJar)
    public static String getRootPath() {
        String jarPath = null;
        try {
            jarPath = LoadSave.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            System.out.println("getRootPath: URI issue.");
            e.printStackTrace();
        }
        return jarPath;
    }

    /* // test if running in jar or non jar
    public static boolean checkRunningInJar() {
        String name = LoadSave.class.getName() + ".class";
        String protocol = LoadSave.class.getResource(name).getProtocol();
        if(Objects.equals(protocol, "jar")){
            return true;
        } else if(Objects.equals(protocol, "file")) {
            return false;
        }
        return false;
    } */

    // get current thread context of a resource (Jar/nonJar)
    public static ClassLoader getContext() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    // save external players file (Jar are read-only)
    // String name, String avatar_name, Weapon weapon(name,dice,bonus), int hp, int ac, int str, int dex, int con
    public static void savePlayer(Player player) throws IOException {
        String player_data =    player.getName() +","+
                                //player.getAvatarName() +","+
                                player.getWeapon().getName() +","+
                                player.getWeapon().getDiceType() +","+
                                player.getWeapon().getBonus() +","+
                                player.getHP() +","+
                                player.getAC() +","+
                                player.getSTR() +","+
                                player.getDEX() +","+
                                player.getCON();
        try(PrintWriter writer = new PrintWriter(ext_data_folder_path.resolve("player.csv").toString(), StandardCharsets.UTF_8)) {
            writer.println(player_data);
            System.out.println(ANSI_GREEN + "Player data successfully saved to file." + ANSI_RESET);
        } catch (IOException e) {
            final String msg = "savePlayer: Could not save to player file.";
            System.out.println(ANSI_RED + msg + ANSI_RESET);
            throw new IOException(msg, e);
        }
    }

    public static List<Player> loadPlayers() throws IOException {
        try (InputStream file_stream = new FileInputStream(ext_data_folder_path.resolve("player.csv").toFile())) {
            InputStreamReader stream_reader = new InputStreamReader(file_stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(stream_reader);
            String line = null;
            while((line = reader.readLine()) != null) {
                players.add(Player.loadFromCsv(line));
            }
            System.out.println(ANSI_GREEN + "Player data successfully loaded from file." + ANSI_RESET);
        } catch (IOException e) {
            final String msg = "loadPlayers: File doesn't exist.";
            System.out.println(msg);
            throw new IOException(msg, e);
        } catch (CsvReadException e) {
            final String msg = "loadPlayers: Csv Parse issue.";
            System.out.println(msg);
            throw new IOException(msg, e);
        } /* catch (ParseException e) {
            final String msg = "loadPlayers: Malformed Player name.";
            System.out.println(msg);
            throw new IOException(msg, e);
        } */
        return players;
    }
    
    // load external weapons file
    public static List<Weapon> loadWeapons() {
        /* Path folder_path = Paths.get(data_res);
        List<String> file_names = new ArrayList<>();

        // Get all files in data directory
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folder_path)) {
            for (Path path : directoryStream) {
                file_names.add(path.toString());
            }
        } catch (IOException e) {
            System.out.println("Error getting files in directory");
        } */

        // Load each data_file from data_file.csv
        //for(String file : file_names) {
        try (InputStream file_stream = new FileInputStream(ext_data_folder_path.resolve("weapons.csv").toFile())) {
            InputStreamReader stream_reader = new InputStreamReader(file_stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(stream_reader);
            String line = null;
            while((line = reader.readLine()) != null) {
                weapons.add(Weapon.loadFromCsv(line));
            }
        } catch (IOException e) {
            System.out.println("loadWeapons: Couldn't read or find file.");
        } catch (CsvReadException e) {
            System.out.println(e);
        }
        //}
        return weapons;
    }

    public static Optional<BufferedImage> loadImage(String img_name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getContext().getResourceAsStream(img_res+img_name));
        } catch (IOException e) {
            System.out.println("loadImage: Couldn't read or find image.");
        }
        return Optional.ofNullable(img);
    }
    
    public static void playMusic() {
        URL url = getContext().getResource(bgm_res+"ZWT.wav");
        try (AudioInputStream audio_stream = AudioSystem.getAudioInputStream(url)) {
            AudioFormat format = audio_stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(audio_stream);
            setVolume(.10f); //[0-1]
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //clip.start();
        } catch (IOException e) {
            System.out.println("playMusic: Couldn't read or find file.");
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("playMusic: Can't play file type.");
        } catch (LineUnavailableException e) {
            System.out.println("playMusic: Line unavailable.");
        }
    }

    private static float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    private static void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
