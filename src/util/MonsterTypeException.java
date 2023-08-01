package util;
//https://stackoverflow.com/questions/6271417/java-get-the-current-class-name

/**
 * Exception handler when setting MonsterTypes
 */
public class MonsterTypeException extends Exception {

    private String data;

    /**
     * Stores a message when thrown
     * @param e message to store
     */
    public MonsterTypeException(String e) {
        data = e;
    }

    /**
     * Pretty prints this exception
     * @return formatted classname and message
     */
    @Override
    public String toString() {
        return this.getClass().getName() + ", " + data;
    }
}