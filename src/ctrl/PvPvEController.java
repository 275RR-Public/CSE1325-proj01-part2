package ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.Clip;

import core.Creature;
import core.Map;
import core.Monster;
import core.Player;
import core.Position;
import view.CombatAttackMenu;
import view.CombatDisarmMenu;
import view.CombatInitiativeMenu;
import view.CombatMenu;
import view.CombatMoveMenu;
import view.CombatStatsMenu;
import view.WinnerScreenMenu;
import view.WinnerMonsterMenu;

public class PvPvEController {
    
    private ArrayList<Player> sorted_player_list = new ArrayList<>();
    private ArrayList<Creature> creature_list = new ArrayList<>();
    private Player current_player;
    private Player next_player;
    private Scanner in;
    private Clip clip;
    private int round = 1;
    private int act_pts = 1;
    private int move_pts = 5;
    private boolean combat_loop;

    public PvPvEController(Scanner in, List<Creature> creature_list, Clip clip) {
        this.in = in;
        this.creature_list = (ArrayList<Creature>) creature_list;
        this.clip = clip;
    }
    
    public int show() {
        //TODO remove dead from map
        //TODO show monster moves?
        while(true) {
            ArrayList<Creature> sorted_list = new CombatInitiativeMenu(in, creature_list, round).show();
            sorted_player_list.clear();
            for(var creature : sorted_list) {
                if(creature instanceof Player)
                    sorted_player_list.add((Player) creature);
            };
            this.current_player = sorted_player_list.get(0);
            this.next_player = sorted_player_list.get(1);
            for(Creature current_creature : sorted_list) {  //combat in order of initiation
                if(current_creature instanceof Player) {
                    this.current_player = (Player) current_creature;
                    this.next_player = getOtherPlayer(current_player, sorted_player_list);
                }
                if(current_creature instanceof Monster) {
                    runMonsterAI(current_creature);
                    isGameOver(current_player, next_player);
                    continue;       //skip to next creature
                }
                combat_loop = true;
                while(combat_loop) {
                    int choice = new CombatMenu(in, round, current_player, sorted_list, act_pts, move_pts).show();
                    switch (choice) {
                        case 1: //attack
                            new CombatAttackMenu(in, round, current_player, sorted_list).show();
                            isGameOver(current_player, next_player);
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

    private void runMonsterAI(Creature current_creature) {
        int monster_moves = 5;
        
        //get target
        Player target = null;
        int dist_to_target;
        int closest_target = 9999;
        for(var player : sorted_player_list) {
            dist_to_target = current_creature.getPosition().distanceTo(player.getPosition());
            if(dist_to_target < closest_target) {
                closest_target = dist_to_target;
                target = player; 
            }
        }
        //move towards target
        while(closest_target > 1 && monster_moves > 0) {
            moveMonsterHandler(current_creature, target, closest_target);
            monster_moves--;
        }
        //attack target
        if(closest_target <= 1) {
            current_creature.attack(target);
        }
    }

    //TODO update to BFS traversal to find shortest route
    private void moveMonsterHandler(Creature current_creature, Player target, int current_dist_to_target) {
        
        Position current_pos = current_creature.getPosition();
        Position move_to = new Position(0, 0);

        Position up1 = new Position(current_pos.getX(), current_pos.getY() - 1);    //up1
        Position down1 = new Position(current_pos.getX(), current_pos.getY() + 1);  //down1
        Position left1 = new Position(current_pos.getX() - 1, current_pos.getY());  //left1
        Position right1 = new Position(current_pos.getX() + 1, current_pos.getY()); //right1
        
        //try to find best move and move towards target
        ArrayList<Position> possible_moves = new ArrayList<>();
        possible_moves.add(up1);
        possible_moves.add(down1);
        possible_moves.add(left1);
        possible_moves.add(right1);
        int shortest_route = 9999;
        for(var move : possible_moves) {
            int move_distance_to_target = move.distanceTo(target.getPosition());
            if(move_distance_to_target < current_dist_to_target && move_distance_to_target < shortest_route) {
                shortest_route = move_distance_to_target;
                move_to.update(move.getX(), move.getY());
            }
        }
        boolean moved = current_creature.move(move_to);
        if(moved) return;

        //if best move failed, try random move until successfully moved
        while(!moved) {
            Random rand = new Random();
            int random_move = rand.nextInt(4);
            switch (random_move) {
                case 0:
                    move_to.update(up1.getX(), up1.getY());
                    break;
                case 1: 
                    move_to.update(down1.getX(), down1.getY());
                    break;
                case 2: 
                    move_to.update(left1.getX(), left1.getY());
                    break;
                case 3:
                    move_to.update(right1.getX(), right1.getY());
                    break;
            }
            moved = current_creature.move(move_to);
        }
    }
    
    private void isGameOver(Player current_player, Player next_player) {
        boolean allMonstersDead = true;
        boolean allPlayersDead = true;
        
        for(var creature : creature_list) {
            if(creature.isDead()) {
                //creature_list.remove(creature);
                continue;
            }
            
            if(creature instanceof Player)
                allPlayersDead = false;
            else
                allMonstersDead = false;
        }

        if(allMonstersDead || allPlayersDead) { //game over
            if(allMonstersDead)
                new WinnerScreenMenu(in, round, current_player, next_player).show();
            else
                new WinnerMonsterMenu(in, round, current_player, next_player).show();
            in.close();
            if(clip != null) clip.close();
            System.exit(0);
        }
    }
    
    private Player getOtherPlayer(Player current_player, ArrayList<Player> sorted_list) {
        int current_index = sorted_list.indexOf(current_player);
        int next_index = (current_index + 1) % 2;
        return sorted_list.get(next_index);
    }
}