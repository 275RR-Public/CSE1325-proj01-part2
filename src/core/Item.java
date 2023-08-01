package core;

import java.util.Objects;

public class Item {
    
    protected String name;
    private int quantity;

    public Item(String name, int quantity) {
        setName(name);
        setQuantity(quantity);
    }

    @Override
    public String toString() {
        return name + " (" + quantity + ")";
    }

    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }
    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 0);
    }
}
