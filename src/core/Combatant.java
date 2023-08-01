package core;
/**
 * Combatant interface represents any entity that rolls for initiative
 */
public interface Combatant {
    public int rollInitiative();
    public String getName();
}
