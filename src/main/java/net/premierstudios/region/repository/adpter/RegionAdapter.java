package net.premierstudios.region.repository.adpter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
public class RegionAdapter {

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + "; " + location.getX() + "; " + location.getY() + "; " + location.getZ() + "; " + location.getYaw() + "; " + location.getPitch();
    }

    public static Location deserializeLocation(String serialized) {
        String[] divPoints = serialized.split("; ");
        Location deserialized = new Location(Bukkit.getWorld(divPoints[0]), parseDouble(divPoints[1]), parseDouble(divPoints[2]), parseDouble(divPoints[3]));
        deserialized.setYaw(parseFloat(divPoints[4]));
        deserialized.setPitch(parseFloat(divPoints[5]));
        return deserialized;
    }

    public static String serializeUsers(List<String> users) {
        StringBuilder serializedData = new StringBuilder();

        for (String user : users) {
            if (serializedData.length() > 0) {
                serializedData.append("; ");
            }
            serializedData.append(user);
        }

        return serializedData.toString();
    }

    public static List<String> deserializeUsers(String serialize) {
        List<String> users = new ArrayList<>();

        if (serialize != null && !serialize.isEmpty()) {
            String[] userArray = serialize.split("; ");

            users.addAll(Arrays.asList(userArray));
        }

        return users;
    }

}
