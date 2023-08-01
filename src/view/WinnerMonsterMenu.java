package view;

import java.util.Scanner;

import core.Player;

public class WinnerMonsterMenu extends Menu {
    
    private int round;
    private Player cp;
    private Player np;
    
    public WinnerMonsterMenu(Scanner in, int round, Player current_player, Player next_player) {
        Menu.in = in;
        this.round = round;
        this.cp = current_player;
        this.np = next_player;
    }

    @Override
    protected void displayMenu() {
        System.out.println();
        System.out.println("*** The Monsters are Victorious!" + " ***");
        System.out.println("They won in " + round + " rounds.");
    }
    
    public void show() {
        clearConsole();
        displayMenu();
        displayStats();
        System.out.println();
        System.out.print("Press Enter to continue... ");
        in.nextLine();
    }
    
    private void displayStats() {
        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s%-40s%-8s%s\n", "Name:", cp.getName(), "Name:", np.getName()));
        sb.append(String.format("%-8s%-40s%-8s%s\n", "Weapon:", cp.getWeapon(), "Weapon:", np.getWeapon()));
        sb.append("HP:\t" + cp.getHP() + "\tModHP:\t" + Math.max(cp.getHP() + cp.getMod(cp.getCON()), 0));
        sb.append("\t\t\tHP:\t" + np.getHP() + "\tModHP:\t" + Math.max(np.getHP() + np.getMod(np.getCON()), 0) + "\n");
        sb.append("AC:\t" + cp.getAC() + "\tModAC:\t" + Math.max(cp.getAC() + cp.getMod(cp.getDEX()), 0));
        sb.append("\t\t\tAC:\t" + np.getAC() + "\tModAC:\t" + Math.max(np.getAC() + np.getMod(np.getDEX()), 0) + "\n");
        sb.append("STR:\t" + cp.getSTR() + "\tModSTR:\t" + cp.getMod(cp.getSTR()));
        sb.append("\t\t\tSTR:\t" + np.getSTR() + "\tModSTR:\t" + np.getMod(np.getSTR()) + "\n");
        sb.append("DEX:\t" + cp.getDEX() + "\tModDEX:\t" + cp.getMod(cp.getDEX()));
        sb.append("\t\t\tDEX:\t" + np.getDEX() + "\tModDEX:\t" + np.getMod(np.getDEX()) + "\n");
        sb.append("CON:\t" + cp.getCON() + "\tModCON:\t" + cp.getMod(cp.getCON()));
        sb.append("\t\t\tCON:\t" + np.getCON() + "\tModCON:\t" + np.getMod(np.getCON()) + "\n");
        System.out.println(sb.toString());
    }
}
