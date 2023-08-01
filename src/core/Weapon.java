package core;

import java.util.Objects;

import util.CsvReadException;
import util.GameUtility;

/**
 * The {@code Weapon} class represents the weapon controlled by a Player.
 */
public class Weapon extends Item {

    //Properties
    private String dice_type = "";
    private int bonus_to_hit = 0;
    private int range = 1;
    private boolean ranged = false;

//Constructors
    /**
     * Default Weapon creation
     * @param name the weapon's name
     * @param dice_type the number and type of dice (eg 2d6)
     * @param bonus_to_hit constant modifier to weapon's chance to hit
     */
    public Weapon(String name, String dice_type, int bonus_to_hit, int range) {
        super(name, 1);
        Objects.requireNonNull(dice_type);
        this.dice_type = dice_type;
        this.bonus_to_hit = Math.max(bonus_to_hit, 0);
        this.range = Math.max(range, 1);
        this.ranged = (range > 1);
    }

//Factory Constructor
    /**
     * Creation of the Weapon from a formatted .csv file
     * @param line the formatted line of Weapon data
     * @return the newly created Weapon
     * @throws CsvReadException when reading in .csv files
     */
    public static Weapon loadFromCsv(String line) throws CsvReadException {
        Weapon weapon = null;
        try  {
            String[] element = line.trim().split(",");
            if(element.length != 4) {
                throw new CsvReadException("Invalid number of CSV arguments.");
            }
            int bonus = Integer.parseInt(element[2]);
            int range = Integer.parseInt(element[3]);
            weapon = new Weapon(element[0], element[1], bonus, range);
        } catch (NumberFormatException e) {
            throw new CsvReadException("Invalid Number.");
        }
        return weapon;   
    }

//Object overrides
    /**
     * Pretty print some properties of the {@code Weapon} class
     * @return custom {@code String} representation of {@code Weapon} class 
     */
    @Override
    public String toString() {
        return super.name + " (" + dice_type + "+" + bonus_to_hit + ")(Range: " + range + ")"; 
    }

//Getters and Setters
    /**
     * Gets Weapon's quantity and type of dice (eg. 2d6).
     * @return String of the dice type 
     */
    public String getDiceType() {
        return dice_type;
    }
    /**
     * Gets Weapon's bonus_to_hit.
     * @return constant modifier to weapon's chance to hit
     */
    public int getBonus() {
        return bonus_to_hit;
    }
    public int getRange() {
        return range;
    }
    public boolean isRanged() {
        return ranged;
    }
    
//Methods
    /**
     * Simulates rolling for the weapon's damage on hit
     * @return the total calculated damage of the roll
     */
    public int rollDamage() {
        return GameUtility.rollDice(dice_type);
    }
}
