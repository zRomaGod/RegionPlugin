package net.premierstudios.region.util.chatresponse;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.premierstudios.region.util.ComponentUtil;
import net.premierstudios.region.util.PaperUtil;
import net.premierstudios.region.util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatResponseListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        ChatResponse chatResponse = ChatResponseUtil.getPlayers().get(player);
        if (chatResponse != null) {
            event.setCancelled(true);
            ChatResponseUtil.getPlayers().remove(player);

            String[] strings = new String[]{"cancel"};

            String message = ComponentUtil.serializeToLegacyOld(event.message());
            if (StringUtil.multiEqualsIgnoreCase(message, strings)) {
                PaperUtil.sendMessage(player, chatResponse.getCancelMessage());
                return;
            }

            chatResponse.getConsumer().accept(message);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ChatResponse chatResponse = ChatResponseUtil.getPlayers().get(player);
        if (chatResponse != null) {
            event.setCancelled(true);
            ChatResponseUtil.getPlayers().remove(player);
            PaperUtil.sendMessage(player, chatResponse.getCancelMessage());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ChatResponseUtil.getPlayers().remove(player);
    }
    
}
