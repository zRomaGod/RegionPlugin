package net.premierstudios.region.util;

public class StringUtil {

    public static boolean multiEqualsIgnoreCase(String input, String... others) {
        for (String other : others) {
            if (input.equalsIgnoreCase(other)) {
                return true;
            }
        }
        return false;
    }

}
