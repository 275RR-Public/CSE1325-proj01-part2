package core;

public class Position {
    private int x = 0;
    private int y = 0;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x; 
    }
    public void setY(int y) {
        this.y = y; 
    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int distanceTo(Position position) {
        return (int) Math.sqrt(Math.pow(position.getX() - x, 2) + Math.pow(position.getY() - y, 2));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
