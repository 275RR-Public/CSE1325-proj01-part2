package core;
import java.io.IOException;
// dont return null - https://www.code4it.dev/cleancodetips/exceptions-instead-of-null/#:~:text=and%20everyone%20else.-,Clean%20Code%20Tip%3A%20throw%20exceptions%20instead%20of%20returning,when%20there%20is%20no%20fallback&text=When%20you%20don't%20have,exception%20instead%20of%20returning%20null.
// exception perf - https://gunnarpeipman.com/cost-of-exceptions/
import java.text.ParseException;
import java.util.Objects;

import util.CsvReadException;
import util.ErrorHandler;
import util.GameUtility;

/**
 * the Player class extends Creature and represents a playable character
 */
public class Player extends Creature {
    
//Properties
    private Weapon equipped_weapon;
    private int disarmed = 0;       //0 not disarmed, num is rounds disarmed

//Constructors
    /**
     * Creation of the Player Creature with stats
     * @param name the name of the player
     * @param weapon the player's weapon
     * @param hp the health points ({@code Default:20})
     * @param ac the armor class ({@code Default:10})
     * @param str strength ({@code Default:0}) ({@code Range:0-10})
     * @param dex dexterity ({@code Default:0}) ({@code Range:0-10})
     * @param con constitution ({@code Default:0}) ({@code Range:0-10})
     * @throws ParseException from name validation
     * @throws IOException
     */
    public Player(String name, Weapon equipped_weapon, int hp, int ac, int str, int dex, int con) {
        super(name, hp, ac, str, dex, con);
        this.equipped_weapon = Objects.requireNonNullElse(equipped_weapon, new Weapon("Long Sword", "2d6", 1,1));
        if(equipped_weapon.isRanged()) getInventory().add(new Ammo("Arrow", 20, 1));
    }

//Factory Constructor
    /**
     * Creation of the Player Creature from a formatted .csv file
     * @param line the formatted line of Player data
     * @return the newly created Player
     * @throws CsvReadException when reading in .csv files
     * @throws ParseException from name validation
     * @throws IOException
     */
    public static Player loadFromCsv(String line) throws CsvReadException {
        Player player = null;
        try  {
            String[] element = line.trim().split(",");
            if(element.length != 10) {
                throw new CsvReadException("Invalid number of CSV arguments.");
            }
            String name = ErrorHandler.validateName(element[0]);

            int bonus = Integer.parseInt(element[3]);
            int range = Integer.parseInt(element[4]);
            Weapon wpn = new Weapon(element[1], element[2], bonus, range);

            int hp = Integer.parseInt(element[5]);
            int ac = Integer.parseInt(element[6]);
            int str = Integer.parseInt(element[7]);
            int dex = Integer.parseInt(element[8]);
            int con = Integer.parseInt(element[9]);

            player = new Player(name,wpn,hp,ac,str,dex,con);
            
        } catch (NumberFormatException e) {
            throw new CsvReadException("Invalid Number.");
        } catch (ParseException e) {
            throw new CsvReadException("Invalid Name.");
        }
        return player;   
    }

    /**
     * Pretty print some properties of the {@code Player} class
     * @return custom {@code String} representation of {@code Player} class 
     */
    @Override
    public String toString() {
        int hp = getHP();
        int ac = getAC();
        int str = getSTR();
        int dex = getDEX();
        int con = getCON();
        Ammo ammo = null;
        StringBuilder sb = new StringBuilder();
        sb.append("Name:\t" + getName() + "\n");
        sb.append("HP:\t" + hp + "\tModHP:\t" + Math.max(hp + getMod(con),0) + "\n");
        sb.append("AC:\t" + ac + "\tModAC:\t" + Math.max(ac + getMod(dex),0) + "\n");
        sb.append("STR:\t" + str + "\tModSTR:\t" + getMod(str) + "\n");
        sb.append("DEX:\t" + dex + "\tModDEX:\t" + getMod(dex) + "\n");
        sb.append("CON:\t" + con + "\tModCON:\t" + getMod(con) + "\n");
        if(equipped_weapon.isRanged()) {
            for(var item : getInventory()) {
                if(item instanceof Ammo) {
                    ammo = (Ammo) item;
                    sb.append("Weapon:\t" + equipped_weapon + "\tAmmo Left: " + ammo.getQuantity() + "\n");
                }
            }
        }
        else {
            sb.append("Weapon:\t" + equipped_weapon + "\n");  
        }
        return sb.toString();
    }
    
    public Weapon getWeapon() {
        return equipped_weapon;
    }
    public int getDisarmed() {
        return disarmed;
    }
    public void setWeapon(Weapon weapon){
        Objects.requireNonNull(weapon);
        this.equipped_weapon = weapon;
    }
    public void setDisarmed(int rounds) {
        this.disarmed = Math.max(rounds, 0);
    }

//Methods
    private int rollHit() {
        int roll = GameUtility.rollDice("d20");
        int dex_mod = super.getMod(this.getDEX());
        int roll_hit = Math.max(roll + dex_mod + equipped_weapon.getBonus(), 0);
        System.out.print(" (" + roll_hit + " to hit) ");
        return roll_hit;
    }
    private int rollDisarm() {
        int roll = GameUtility.rollDice("d20");
        int str_mod = super.getMod(this.getSTR());
        int roll_disarm = Math.max(roll + str_mod, 0);
        System.out.println(getName() + " rolled " + roll_disarm);
        return roll_disarm;
    }
    public void disarm(Player target) {
        if(rollDisarm() > target.rollDisarm()) {
            System.out.println(getName() + " DISARMS " + target.getName() + " for 2 rounds!");
            target.setDisarmed(2);
        }
        else {
            System.out.println(getName() + " couldn't overpower " + target.getName() + " and FAILS to disarm!");
        }
    }
    
    /**
     * Implements attacking for Players
     * @param target the Creature to attack
     */
    @Override
    public void attack(Creature target) {
        System.out.print(getName() + " attacks " + target.getName() + " with " + equipped_weapon.getName());
        int target_mod_ac = Math.max(target.getAC() + target.getMod(target.getAC()),0);
        if(rollHit() >= target_mod_ac) {
            int str_mod = getMod(getSTR());
            int roll_dmg = Math.max(str_mod + equipped_weapon.rollDamage(), 0);
            System.out.println("...HITS!");
            target.takeDamage(roll_dmg);
        }
        else {
            System.out.println("...MISSES!");
        }
        //Decrement ammo if ranged
        if(equipped_weapon.isRanged()) {
            for(var item : getInventory()) {
                if(item instanceof Ammo) {
                    Ammo ammo = (Ammo) item;
                    ammo.setQuantity(ammo.getQuantity() - 1);
                    System.out.printf("%s has %d %ss left.\n", getName(), ammo.getQuantity(), ammo.getName());
                }
            }
        }
    }

    /**
     * Implements the Combatant interface for initiative
     * @return the result of the roll
     */
    @Override
    public int rollInitiative() {
        int roll = GameUtility.rollDice("d20");
        return Math.max(roll, 0);
    }
}
