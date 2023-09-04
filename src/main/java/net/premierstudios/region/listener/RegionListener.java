package net.premierstudios.region.listener;

import net.premierstudios.region.RegionManager;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.model.create.CreateRegion;
import net.premierstudios.region.model.edit.EditRegion;
import net.premierstudios.region.util.ItemUtil;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;

public class RegionListener implements Listener {

    private final RegionManager regionManager = RegionPlugin.getInstance().getRegionManager();

    public RegionListener(RegionPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // PLAYER
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        regionManager.getCreateRegionHashMap().remove(player.getName());
        regionManager.getEditRegionHashMap().remove(player.getName());
    }


    // CREATE & EDIT LISTENER
    @Deprecated
    @EventHandler
    public void onPlayerInteractCreate(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = player.getItemInHand();

        if (itemStack.getType() == Material.AIR) return;
        if (!ItemUtil.hasNBT(itemStack, "region-item")) return;

        CreateRegion createRegion = regionManager.getCreateRegionHashMap().get(player.getName());
        EditRegion editRegion = regionManager.getEditRegionHashMap().get(player.getName());

        if (createRegion != null) {
            event.setCancelled(true);

            Block block = event.getClickedBlock();

            if (block != null) {
                if (event.getAction().isLeftClick()) {
                    createRegion.positionDefined(player, block.getLocation(), "FIRST");
                }
                if (event.getAction().isRightClick()) {
                    createRegion.positionDefined(player, block.getLocation(), "SECOND");
                }
            }
        }

        if (editRegion != null) {
            event.setCancelled(true);

            Block block = event.getClickedBlock();

            if (block != null) {
                if (event.getAction().isLeftClick()) {
                    editRegion.positionDefined(player, block.getLocation(), "FIRST");
                }
                if (event.getAction().isRightClick()) {
                    editRegion.positionDefined(player, block.getLocation(), "SECOND");
                }
            }
        }

    }


    // REGIONS LISTENERS
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        for (Region region : regionManager.getRegionHashMap().values()) {
            if (region.getCuboid().hasBlockInside(block)) {
                if (!region.getUsers().contains(player.getName()) && !player.hasPermission("region.bypass")) {
                    PaperUtil.sendMessage(player, "&cYou are not allowed to interact in this area!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        for (Region region : regionManager.getRegionHashMap().values()) {
            if (region.getCuboid().hasBlockInside(block)) {
                if (!region.getUsers().contains(player.getName()) && !player.hasPermission("region.bypass")) {
                    PaperUtil.sendMessage(player, "&cYou are not allowed to interact in this area!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        for (Region region : regionManager.getRegionHashMap().values()) {
            if (region.getCuboid().hasPlayerInside(player)) {
                if (!region.getUsers().contains(player.getName()) && !player.hasPermission("region.bypass")) {
                    PaperUtil.sendMessage(player, "&cYou are not allowed to interact in this area!");
                    player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        for (Region region : regionManager.getRegionHashMap().values()) {
            if (region.getCuboid().hasPlayerInside(player)) {
                if (!region.getUsers().contains(player.getName()) && !player.hasPermission("region.bypass")) {
                    PaperUtil.sendMessage(player, "&cYou are not allowed to interact in this area!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @Deprecated
    @EventHandler
    public void onPlayerPickUPItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        for (Region region : regionManager.getRegionHashMap().values()) {
            if (region.getCuboid().hasPlayerInside(player)) {
                if (!region.getUsers().contains(player.getName()) && !player.hasPermission("region.bypass")) {
                    PaperUtil.sendMessage(player, "&cYou are not allowed to interact in this area!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
