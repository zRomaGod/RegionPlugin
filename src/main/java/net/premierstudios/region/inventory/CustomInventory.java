package net.premierstudios.region.inventory;

import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomInventory {

    protected RyseInventory inventory;
    protected final Map<Player, Long> clickDelay;

    protected CustomInventory() {
        this.clickDelay = new HashMap<>();
    }

    public void open(Player player) {
        try {
            inventory.open(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
