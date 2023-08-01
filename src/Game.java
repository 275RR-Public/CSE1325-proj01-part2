// src pgk. javadoc -d docs -subpackages src:src.core:src.test:src.util
// no src pkg. javadoc -d ../docs Game.java -subpackages core:util:test
// vs code fix - files in the correct directory, press F1 and type in "Java: Clean Java Language Server Workspace"
// bash delete help.    view.   find . -type f -name "*.class" -print
//                      del.    find . -type f -name "*.class" -delete
// windows(and older java): javac -encoding UTF-8 Game.java
// windows:enviroment var:system:create new: JAVA_TOOL_OPTIONS=-D"file.encoding=UTF8"

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Creature;
import ctrl.CreationController;
import ctrl.GameController;
import ctrl.LoadController;
import ctrl.SaveController;
import view.MainMenu;
import view.TitleScreenMenu;

/**
 * The {@code Game} class is the entry point and main loop for the game.
 */
public class Game {
    
    /** 
     * @param args NO command line arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        Scanner in = new Scanner(System.in);
        List<Creature> creature_list = new ArrayList<>();
        
        new TitleScreenMenu(in).show();

        while(true) {
            int user_selection = new MainMenu(in, creature_list).show();
            switch (user_selection) {
                case 1: //start game
                    new GameController(in, creature_list).show();
                    break;
                case 2: //create char
                    new CreationController(in, creature_list).show();
                    break;
                case 3: //load char
                    new LoadController(in, creature_list).show();
                    break;
                case 4: //save char
                    new SaveController(in, creature_list).show();
                    break;
                case 5: //exit
                    System.out.println("Quitting...");
                    in.close();
                    System.exit(0);
                    break;
            }
        }
    }
}
