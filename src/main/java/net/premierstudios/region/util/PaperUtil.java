package net.premierstudios.region.util;

import org.bukkit.command.CommandSender;

public class PaperUtil {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message.replace("&", "§"));
    }

}
