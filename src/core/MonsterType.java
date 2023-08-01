package core;

public enum MonsterType {
    HUMANOID,
    FIEND,
    DRAGON;

    @Override
    public String toString() {
        switch(this) {
            case HUMANOID: return "Humanoid";
            case FIEND: return "Fiend";
            case DRAGON: return "Dragon";
            default: throw new IllegalArgumentException();
        }
    }
}
