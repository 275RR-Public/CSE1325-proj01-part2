package ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.Clip;

import core.Creature;
import core.Map;
import core.Player;
import view.CombatAttackMenu;
import view.CombatDisarmMenu;
import view.CombatInitiativeMenu;
import view.CombatMenu;
import view.CombatMoveMenu;
import view.CombatStatsMenu;
import view.WinnerScreenMenu;

public class PvPController {
    
    private ArrayList<Player> sorted_player_list = new ArrayList<>();
    private ArrayList<Creature> creature_list = new ArrayList<>();
    private Scanner in;
    private Clip clip;
    private int round = 1;
    private int act_pts = 1;
    private int move_pts = 5;
    private boolean combat_loop;

    public PvPController(Scanner in, List<Creature> creature_list, Clip clip) {
        this.in = in;
        this.creature_list = (ArrayList<Creature>) creature_list;
        this.clip = clip;
    }
    
    public int show() {
    
        while(true) {
            ArrayList<Creature> sorted_list = new CombatInitiativeMenu(in, creature_list, round).show();
            for(var creature : sorted_list) {
                sorted_player_list.add((Player) creature);
            };
            for(Player current_player : sorted_player_list) {  //combat in order of initiation
                Player next_player = getOtherPlayer(current_player, sorted_player_list);
                combat_loop = true;
                while(combat_loop) {
                    int choice = new CombatMenu(in, round, current_player, sorted_list, act_pts, move_pts).show();
                    switch (choice) {
                        case 1: //attack
                            new CombatAttackMenu(in, round, current_player, sorted_list).show();
                            if(next_player.isDead()) { //game over
                                new WinnerScreenMenu(in, round, current_player, next_player).show();
                                in.close();
                                if(clip != null) clip.close();
                                System.exit(0);
                            }
                            act_pts--;
                            break;
                        case 2: //disarm
                            new CombatDisarmMenu(in, round, current_player, sorted_list).show();
                            act_pts--;
                            break;
                        case 3: //move
                            int moves = new CombatMoveMenu(in, round, current_player, sorted_list, move_pts).show();
                            move_pts -= moves;
                            break;
                        case 4: //view stats
                            new CombatStatsMenu(in, round, current_player, next_player).show();
                            break;
                        case 5: //end turn
                            combat_loop = false;
                            //confirmation was annoying
                            /* if(act_pts == 0 && move_pts == 0) {
                                combat_loop = false;
                            }
                            else {
                                combat_loop = new CombatEndMenu(in, round, current_player, act_pts, move_pts).show();
                            } */
                            break;
                        default:
                            break;
                    }
                }
                //turn resets
                act_pts = 1;
                move_pts = 5;
                int disarm_rounds = current_player.getDisarmed();
                if(disarm_rounds > 0) {
                    current_player.setDisarmed(disarm_rounds - 1);
                }
            }
            //round resets
            round++;
        }
    }

    private Player getOtherPlayer(Player current_player, ArrayList<Player> sorted_list) {
        int current_index = sorted_list.indexOf(current_player);
        int next_index = (current_index + 1) % 2;
        return sorted_list.get(next_index);
    }
}
