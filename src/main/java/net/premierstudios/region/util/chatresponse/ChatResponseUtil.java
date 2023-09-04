package net.premierstudios.region.util.chatresponse;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ChatResponseUtil {

    @Getter private static final Map<Player, ChatResponse> players = new HashMap<>();

    public static void load(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new ChatResponseListener(), plugin);
    }

    public static void create(Player player, ChatResponse chatResponse) {
        players.putIfAbsent(player, chatResponse);
    }

}
