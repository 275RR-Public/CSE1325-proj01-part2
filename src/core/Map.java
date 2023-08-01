package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import util.GameUtility;

public class Map {
    
    private static Scanner in;
    private static int map_cols = 25; //cols map to pos_x
    private static int map_rows = 25; //rows map to pos_y
    private static int[][] map = new int[map_rows][map_cols];

    // colored text in terminal
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Map(Scanner in, List<Creature> creature_list) {
        Map.in = in;
        // init Creature positions
        initRandomPositions(creature_list);
        // init Map with Creatures
        for(Creature creature : creature_list) {
            int x = creature.getPosition().getX();
            int y = creature.getPosition().getY();
            if(creature instanceof Player) {
                map[y][x] = 1;  
            }
            if(creature instanceof Monster) {
                map[y][x] = 2;
            }
        }
    }

    /* public static void showMiniMap(String current_name, String next_name, Position current_pos, Position next_pos) {
        //https://stackoverflow.com/questions/6006618/how-to-draw-a-rectangle-in-console-application
        String s = "╔════25╗" + "\tMap Locations:\n";
        s +=       "║     25" + "\t" + current_name + " " + current_pos + "\n";
        s +=       "1      ║" + "\t" + next_name + " " + next_pos + "\n";
        s +=       "╚═1════╝" + "\n";
        System.out.print(s);
    } */

    public static void drawMap(Player cp, ArrayList<Creature> sorted_list) {
        System.out.println();

        //build map legend
        List<StringBuilder> build_list = new ArrayList<>();
        for(int i = 0; i < map_rows; i++) {
            build_list.add(new StringBuilder(""));
        }
        build_list.set(0, new StringBuilder("Map Legend:"));
        build_list.set(1, new StringBuilder("P = Player"));
        build_list.set(2, new StringBuilder("M = Monster"));
        build_list.set(3, new StringBuilder("++ = Grid Helper"));
        build_list.set(4, new StringBuilder(""));
        build_list.set(5, new StringBuilder("Map Locations (in order of play):"));
        for(int i = 0; i < sorted_list.size(); i++) {
            Creature creature = sorted_list.get(i);
            if(creature instanceof Player && creature.getName().equals(cp.getName()))
                build_list.set(6 + i, new StringBuilder("> " + creature.getPosition() + " " + creature.getName()));
            else
                build_list.set(6 + i, new StringBuilder("  " + creature.getPosition() + " " + creature.getName()));
        }


        //print col numbers
        System.out.print("  ");
        for(int col = 0; col < map_cols; col++) {
            if(col < 10) System.out.print("0" + col);
            else System.out.print(col);
            System.out.print(" ");
        }
        System.out.println();

        //start map rows
        for(int row = 0; row < map_rows; row++) {
            //print row number
            if(row < 10) System.out.print("0" + row);
            if(row >= 10) System.out.print(row);
            //in row, start map cols
            for(int col = 0; col < map_cols; col++) {
                if(map[row][col] == 1) {
                    System.out.print(ANSI_GREEN + " P" + ANSI_RESET);
                }
                else if(map[row][col] == 2) {
                    System.out.print(ANSI_RED + " M" + ANSI_RESET);
                }
                else {
                    if(col % 5 == 4 && row % 5 == 4)
                        System.out.print("++");
                    else
                        System.out.print("  ");
                }
                System.out.print(" ");
            }
            //display a line from the map legend
            System.out.println("\t" + build_list.get(row).toString());
        }
    }

    public static boolean update(Position current, Position move_to, Creature c) {
        if(inBounds(move_to) && !isOccupied(move_to)) {
            int x = current.getX();
            int y = current.getY();
            int creature = map[y][x];
            map[y][x] = 0;
            map[move_to.getY()][move_to.getX()] = creature;
            return true;
        }
        else {
            if(c instanceof Player)
                displayError();
            return false;
        }
    }

    public static boolean inAttackRange(Creature attacker, Creature target) {
        int attacker_dist_target = attacker.getPosition().distanceTo(target.getPosition());
        // Monsters are melee (Range=1)
        if(attacker instanceof Monster) {
            if(attacker_dist_target <= 1) {
                return true;
            }
        }
        // Players can be melee or ranged
        if(attacker instanceof Player) {
            Player player = (Player) attacker;
            if(player.getWeapon().isRanged()) {
                //Ranged (get Weapon's range)
                if(attacker_dist_target <= player.getWeapon().getRange()) {
                    return true;
                }
            }
            else {
                //Melee (Range=1)
                if(attacker_dist_target <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canDisarm(Creature attacker, Creature target) {
        int attacker_dist_target = attacker.getPosition().distanceTo(target.getPosition());
        if(target instanceof Player) {
            //both melee or ranged must be within 1 space
            if(attacker_dist_target <= 1) {
                return true;
            }
        }
        return false;
    }

    private static void displayError() {
        System.out.println("Move is out of bounds or space is occupied.");
        System.out.print("Press Enter to try again...");
        in.nextLine();
    }
    
    private static boolean isOccupied(Position position) {
        if(map[position.getY()][position.getX()] != 0) {
            return true;
        }
        return false;
    }

    private static boolean inBounds(Position position) {
        int pos_x = position.getX();
        int pos_y = position.getY();
        if(!(pos_x == GameUtility.inRange(pos_x, 0, (map_cols - 1)))) {
            return false;
        }
        if(!(pos_y == GameUtility.inRange(pos_y, 0, (map_rows - 1)))) {
            return false;
        }
        return true;
    }

    private static void initRandomPositions(List<Creature> creature_list) {
        Random rand = new Random();
        for(Creature creature : creature_list) {
            while(true) {
                int x = rand.nextInt(map_cols);
                int y = rand.nextInt(map_rows);
                Position pos = new Position(x, y);
                if(inBounds(pos) && !isOccupied(pos)) {
                    creature.setPosition(pos);
                    break;
                }
            }  
        }
    }
}
