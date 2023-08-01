package core;
import java.io.IOException;
// dont return null - https://www.code4it.dev/cleancodetips/exceptions-instead-of-null/#:~:text=and%20everyone%20else.-,Clean%20Code%20Tip%3A%20throw%20exceptions%20instead%20of%20returning,when%20there%20is%20no%20fallback&text=When%20you%20don't%20have,exception%20instead%20of%20returning%20null.
// exception perf - https://gunnarpeipman.com/cost-of-exceptions/
import java.text.ParseException;
import java.util.Objects;

import util.CsvReadException;
import util.GameUtility;
import util.MonsterTypeException;

/**
 * the Monster class extends Creature and represents a playable character
 */
public class Monster extends Creature {

    private MonsterType TYPE;

//Constructors
    /**
     * Creation of the Monster Creature with stats
     * @param name the name of the Monster
     * @param weapon the Monster's weapon
     * @param hp the health points ({@code Default:20})
     * @param ac the armor class ({@code Default:10})
     * @param str strength ({@code Default:0}) ({@code Range:0-10})
     * @param dex dexterity ({@code Default:0}) ({@code Range:0-10})
     * @param con constitution ({@code Default:0}) ({@code Range:0-10})
     * @throws ParseException from name validation
     * @throws IOException
     * @throws MonsterTypeException
     */
    public Monster(String name, String type, int hp, int ac, int str, int dex, int con) throws MonsterTypeException {
        super(name, hp, ac, str, dex, con);
        try {
            this.TYPE = MonsterType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new MonsterTypeException("Invalid Monster Type.");
        }
    }

//Factory Constructor
    /**
     * Creation of the Monster Creature from a formatted .csv file
     * @param line the formatted line of Monster data
     * @return the newly created Monster
     * @throws CsvReadException when reading in .csv files
     * @throws ParseException from name validation
     * @throws IOException
     * @throws MonsterTypeException
     */
    public static Monster loadFromCsv(String line) throws CsvReadException {
        Monster monster = null;
        try  {
            String[] element = line.trim().split(",");
            if(element.length != 7) {
                throw new CsvReadException("Invalid number of CSV arguments.");
            }
            int hp = Integer.parseInt(element[2]);
            int ac = Integer.parseInt(element[3]);
            int str = Integer.parseInt(element[4]);
            int dex = Integer.parseInt(element[5]);
            int con = Integer.parseInt(element[6]);
            monster = new Monster(
                element[0],
                element[1],
                hp,
                ac,
                str,
                dex,
                con
            );
        } catch (NumberFormatException e) {
            throw new CsvReadException("Invalid Number.");
        } catch (MonsterTypeException e) {
            throw new CsvReadException("Invalid Monster Type.");
        }
        return monster;   
    }

    /**
     * Pretty print some properties of the {@code Monster} class
     * @return custom {@code String} representation of {@code Monster} class 
     */
    @Override
    public String toString() {
        int hp = getHP();
        int ac = getAC();
        int str = getSTR();
        int dex = getDEX();
        int con = getCON();
        StringBuilder sb = new StringBuilder();
        sb.append("Name:\t" + getName() + " (" + TYPE + ")" + "\n");
        sb.append("HP:\t" + hp + "\tModHP:\t" + Math.max(hp + getMod(con),0) + "\n");
        sb.append("AC:\t" + ac + "\tModAC:\t" + Math.max(ac + getMod(dex),0) + "\n");
        sb.append("STR:\t" + str + "\tModSTR:\t" + getMod(str) + "\n");
        sb.append("DEX:\t" + dex + "\tModDEX:\t" + getMod(dex) + "\n");
        sb.append("CON:\t" + con + "\tModCON:\t" + getMod(con) + "\n");
        return sb.toString();
    }

//Methods
    private int rollHit() {
        int roll = GameUtility.rollDice("d20");
        int dex_mod = super.getMod(super.getDEX());
        int roll_hit = Math.max(roll + dex_mod, 0);
        System.out.print(" (" + roll_hit + " to hit) ");
        return roll_hit;
    }
    
    /**
     * Implements attacking for Monsters
     * @param target the Creature to attack
     */
    @Override
    public void attack(Creature target) {
        System.out.print(getName() + " attacks " + target.getName());
        int target_mod_ac = Math.max(target.getAC() + target.getMod(target.getAC()),0);
        if(rollHit() >= target_mod_ac) {
            int str_mod = getMod(getSTR());
            int roll_dmg = Math.max(str_mod + GameUtility.rollDice("d6"), 0);
            System.out.println("...HITS!");
            target.takeDamage(roll_dmg);
        }
        else {
            System.out.println("...MISSES!");
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
