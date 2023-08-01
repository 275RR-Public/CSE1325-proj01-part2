// https://www.javaguides.net/2018/08/how-to-check-if-string-contains-only-letters-or-digits.html
package util;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code GameUtility} class contains static functions that aren't game objects.
 */
public class GameUtility {
    
    /**
     * Simulates rolling dice (eg d6, 2d6, 2d6+3).
     * example: {@code 2d6+3} equals 2 dice of type six-sided with 3 modifier
     * @param user_input dice type is required, optional quantity, and optional modifier
     * @return total from all dice rolls and modifiers
     */
    public static int rollDice (String user_input) {
         // defaults
        int num = 1;
        int constant = 0;
        
        if(user_input == null) {
            return -1;
        }

        // regex to parse user input
        // built using https://regex101.com/r/HZf7YH/2
        // pattern: [positive int]d<positive int>[+positive int]
        String pattern = "^([1-9]+[0-9]*)?d([1-9]+[0-9]*)(\\+([1-9]+[0-9]*))?$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(user_input);

        if(!m.find()) {
            //System.out.println("Usage: RollDice( [NUM]d<DICE>[+CONSTANT] )");
            //System.out.println("Example: RollDice(\"2d6+4\") or RollDice(\"d4\")");
            return -1;
        }

        // user provided data
        String user_num = m.group(1);
        String user_const = m.group(4);
        String user_dice = m.group(2);
        
        // update defaults if user supplied optional data
        if(user_num != null) {
            num = Integer.parseInt(user_num); 
        }
        if(user_const != null) {
            constant = Integer.parseInt(user_const); 
        }
        int dice = Integer.parseInt(user_dice);
        
        // calculate and return total.
        // total = all dice rolls + constant
        Random rnd = new Random();
        int total = 0;
        
        for(int i = 0; i < num; i++) {
            total += rnd.nextInt(dice) + 1;
        }
        total += constant;
        
        return total;
    }

    /**
     * Tests if a String is alphnumeric
     * @param s the String to test
     * @return boolean result of test
     */
    public static boolean isAlphaNumeric (String s) {
        if(s == null || s.length() == 0) {
            return false;
        }
        for(int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clamps given int to a range
     * @param number the int to clamp
     * @param min_range the minimum of the range
     * @param max_range the maximum of the range
     * @returns number if in range else min or max
     */
    public static int inRange (int number, int min_range, int max_range) {
        if(number <= min_range) {
            return min_range;
        }
        if(number >= max_range) {
            return max_range;
        }
        return number;
    }

    // Overloaded method to allow for default values
    public static int[] randomStats(int[] stats, int points) {
        return randomStats(stats, Math.max(points,0), new Integer[stats.length], 0);
    }
    
    // Recursively assigns random values to randomly picked stat from a pool of points
    // much more readable and scalable than the original code
    public static int[] randomStats(int[] stats, int points, Integer[] prev_picks, int curr_index) {

        // Base case: Return if points used up
        if(points <= 0) 
            return stats;

        Random r = new Random();
    
        // Get a random stat, excluding previous picks
        int statIndex;
        do {
            statIndex = r.nextInt(stats.length);
        } while(Arrays.asList(prev_picks).contains(statIndex));
    
        // Base case: Assign remaining points to final stat
        if(curr_index == stats.length - 1) { 
            stats[statIndex] = points;
            return stats;
        }
    
        // Track previous picks
        prev_picks[curr_index] = statIndex;
    
        // Roll random value for stat and deduct from points
        int roll = r.nextInt(points) + 1;
        stats[statIndex] = roll;
        points -= roll;
    
        return randomStats(stats, points, prev_picks, curr_index + 1);
    }

    /* private void randomStats() {
        //TODO refactor as recursive
        Random random = new Random();
        int first_pick = -99;                               //dummy value until assigned
        for(int stat = 2; stat > 0; stat--) {               //3 stats [str,dex,con]
            int pick_stat = random.nextInt(stat + 1);       //roll to pick a stat
            int roll_stat = random.nextInt(points) + 1;     //roll for the stat's value
            if(first_pick == 0)
                pick_stat += 1;                             //if previous pick was str, skip str in array
            if(first_pick == 1 && pick_stat == 1)
                pick_stat += 1;                             //if previous pick was dex, skip dex in array
            stats[pick_stat] = roll_stat;                   //assign stat
            points -= roll_stat;                            //remaining points minus stat's value
            if(points == 0) {
                return;
            }
            if(stat == 1) {                                 //if one stat left
                stats[3 - first_pick - pick_stat] = points; //add remaining points to last stat
                return;
            }
            first_pick = pick_stat;                         //record which stat picked first
        }
    } */
}
