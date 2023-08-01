package util;

import java.text.ParseException;

import javax.swing.JButton;

public class ErrorHandler {
    
    public static boolean valid_name;
    public static boolean valid_avatar;

    public static void validateForm(JButton submit_button) {
        if(valid_name && valid_avatar)
            submit_button.setEnabled(true);
        else
            submit_button.setEnabled(false);
    }
    /**
     * Validates input during Creature name creation
     * @param s the name to create
     * @throws ParseException for null, >24 char, no capital letter, not alphabetic
     */
    public static String validateName (String s) throws ParseException {
        if(s == null || s.length() < 2)
            throw new ParseException("Too Short.", 0);
        if(s.length() > 24)
            throw new ParseException("Too Long.", 0);
        if(!Character.isUpperCase(s.charAt(0)))
            throw new ParseException("Not Capitalized.", 0);
        for(int i = 0; i < s.length(); i++) {
            if (!Character.isAlphabetic(s.charAt(i))) {
                throw new ParseException("No Numbers, Specials.", 0);
            }
        }
        return s;
    }

    public static boolean validateStats(Object str, Object dex, Object con) throws NumberFormatException {
        try {
            int int_str = Integer.parseInt(str.toString());
            int int_dex = Integer.parseInt(dex.toString());
            int int_con = Integer.parseInt(con.toString());
            if(int_str > 10 || int_dex > 10 || int_con > 10)
                throw new NumberFormatException();
            if(int_str + int_dex + int_con <= 15)
                return true;
        } catch (NumberFormatException e) {
            var ex = new NumberFormatException("validateStats: cant parse to number");
            ex.initCause(e);
            throw ex;
        }
        return false;
    }
}
