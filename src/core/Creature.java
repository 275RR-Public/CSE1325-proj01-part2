package core;

// https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import util.GameUtility;

/**
 * the Creature class represents all living entities
 */
public abstract class Creature implements Comparable<Creature>, Combatant {

    //Properties
    private String name = "";
    private Position position = new Position(0, 0);
    //creature has inv but only player has wpn in inv
    private List<Item> inventory = new ArrayList<>();

    private int hp = 20;            //health points
    private int ac = 10;            //armor class
    private int str = 0;            //strength      [0,10]
    private int dex = 0;            //dexterity     [0,10]
    private int con = 0;            //constitution  [0,10]

    private String creation_date = LocalDateTime.now().toString();

//Constructors
    /**
     * Creation of the Creature with stats
     * @param name the name of the creature
     * @param hp the health points ({@code Default:20})
     * @param ac the armor class ({@code Default:10})
     * @param str strength ({@code Default:0}) ({@code Range:0-10})
     * @param dex dexterity ({@code Default:0}) ({@code Range:0-10})
     * @param con constitution ({@code Default:0}) ({@code Range:0-10})
     */
    public Creature(String name, int hp, int ac, int str, int dex, int con) {
        setName(name);
        setHP(hp);
        setAC(ac);
        setSTR(str);
        setDEX(dex);
        setCON(con);
    }
    
//Overrides
    /**
     * equality check by Creature's name
     * @param o the Creature to check for equality
     * @return boolean representing equality
     */
    @Override
    public boolean equals(Object o) {
        return o != null
            && getClass() == o.getClass()
            && name.equals(((Creature) o).getName());
    }
    /**
     * compareTo compares Creature's by HP
     * @param creature the Creature to compareTo
     * @return int representing ordering
     */
    @Override
    public int compareTo(Creature creature) {
        return this.getHP() - creature.getHP();
    }
    /**
     * Combatant interface allows Creatures to roll initiative
     */
    @Override
    public abstract int rollInitiative();
    /**
     * Combatant interface requires a name
     */
    @Override
    public String getName() {
        return name;
    }
    
//Setters and Getters
    /**
     * Sets Creature's name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    /**
     * Sets Creature's hp.
     * @param the hp to set
     */
    public void setHP(int hp) {
        this.hp = Math.max(hp, 0);
    }
    /**
     * Sets Creature's ac.
     * @param the ac to set
     */
    public void setAC(int ac) {
        this.ac = Math.max(ac, 0);
    }
    /**
     * Sets Creature's str.
     * @param the str to set
     */
    public void setSTR(int str) {
        this.str = GameUtility.inRange(str, 0, 10);
    }
    /**
     * Sets Creature's dex.
     * @param the dex to set
     */
    public void setDEX(int dex) {
        this.dex = GameUtility.inRange(dex, 0, 10);
    }
    /**
     * Sets Creature's con.
     * @param the con to set
     */
    public void setCON(int con) {
        this.con = GameUtility.inRange(con, 0, 10);
    }
    public Position getPosition() {
        return position;
    }
    public List<Item> getInventory() {
        return inventory;
    }
     /**
     * Gets Creature's hp.
     * @return the hp of the Creature
     */
    public int getHP() {
        return hp;
    }
     /**
     * Gets Creature's ac.
     * @return the ac of the Creature
     */
    public int getAC() {
        return ac;
    }
     /**
     * Gets Creature's str.
     * @return the str of the Creature
     */
    public int getSTR() {
        return str;
    }
     /**
     * Gets Creature's dex.
     * @return the dex of the Creature
     */
    public int getDEX() {
        return dex;
    }
     /**
     * Gets Creature's con.
     * @return the con of the Creature
     */
    public int getCON() {
        return con;
    }
    
//Methods
    /**
     * Creature's can attack other Creatures
     * @param target the Creature to attack
     */
    public abstract void attack(Creature target);

    public boolean isDead() {
        return hp + getMod(con) <= 0;
    }
    
    /**
     * Creature's can takeDamage from attacks
     * @param damage the amount of hp damage to take
     */
    public void takeDamage(int damage) {
        Math.max(damage, 0);
        setHP(hp - damage);
        System.out.println(name + " took " + damage + " damage and has " + hp + " hp left. (Effective Hp: " + (hp + getMod(con)) + ")");
    }

    // Modifies stats based on distance from center of range
    public int getMod(int stat) {
        return stat - 5;
    }
    public boolean move(Position move_to) {
        boolean map_updated = Map.update(position, move_to, this);
        if(map_updated) {
           position.update(move_to.getX(), move_to.getY());
           System.out.println(getName() + " moved to " + position);
           return true;
        }
        return false;
    }
}
