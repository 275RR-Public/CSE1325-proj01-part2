package core;

public class Ammo extends Item {
    
    private int damage = 0;

    public Ammo(String name, int quantity, int damage) {
        super(name, quantity);
        setDamage(damage);
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = Math.max(damage, 0);
    }
    
}
