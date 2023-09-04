package net.premierstudios.region.command;

import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.inventory.region.RegionInventory;
import net.premierstudios.region.inventory.region.RegionsInventory;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.model.create.CreateRegion;
import net.premierstudios.region.util.ItemUtil;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RegionCommand implements CommandExecutor {

    @Override
    @Deprecated
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (args.length == 0) {
                if (!player.hasPermission("region.menu")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                if (RegionPlugin.getInstance().getRegionManager().getRegionHashMap().size() == 0) {
                    PaperUtil.sendMessage(player, "&cThere is no region created at the moment!");
                    return false;
                }

                new RegionsInventory().open(player);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                return false;
            }

            if (args[0].equalsIgnoreCase("create")) {
                if (!player.hasPermission("region.create")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                if (args.length == 1) {
                    PaperUtil.sendMessage(player, "&cUse it as follows: /region create <name>");
                    return false;
                }

                String name = args[1];

                if (name.length() > 20) {
                    PaperUtil.sendMessage(player, "&cThe name has too many characters");
                    return false;
                }

                if (RegionPlugin.getInstance().getRegionManager().getCreateRegionHashMap().containsKey(player.getName())) {
                    PaperUtil.sendMessage(player, "&cYou're already in the process of creating a region, finish it first!");
                    return false;
                }

                CreateRegion createRegion = new CreateRegion(name);
                RegionPlugin.getInstance().getRegionManager().getCreateRegionHashMap().put(player.getName(), createRegion);

                PaperUtil.sendMessage(player, "&eYou started the process of creating a region...");
            }

            if (args[0].equalsIgnoreCase("wand")) {
                if (!player.hasPermission("region.wand")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                List<String> lore = new ArrayList<>();
                lore.add("§eRigh Click: §7First position!");
                lore.add("§eLeft Click: §7Second position!");

                ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§cRegion Wand");
                itemMeta.setLore(lore);

                itemStack.setItemMeta(itemMeta);

                player.getInventory().addItem(ItemUtil.setNBT(itemStack, "region-item", "true"));
                PaperUtil.sendMessage(player, "&aYou have successfully received the item!");
                return false;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (!player.hasPermission("region.add")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                if (args.length < 3) {
                    PaperUtil.sendMessage(player, "&cUse it as follows: /region add <region> <username>");
                    return false;
                }

                String regionName = args[1];
                String playerName = args[2];

                Region region = RegionPlugin.getInstance().getRegionManager().getRegion(regionName);

                if (region == null) {
                    PaperUtil.sendMessage(player, "&cNo region with this name was found.");
                    return false;
                }

                if (region.getUsers().contains(playerName)) {
                    PaperUtil.sendMessage(player, "&cThis player has already been added to the region!");
                    return false;
                }

                region.getUsers().add(playerName);
                region.insertOrUpdate();
                PaperUtil.sendMessage(player, "&aThe player has been successfully added to this region!");
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (!player.hasPermission("region.remove")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                if (args.length < 3) {
                    PaperUtil.sendMessage(player, "&cUse it as follows: /region remove <region> <username>");
                    return false;
                }

                String regionName = args[1];
                String playerName = args[2];

                Region region = RegionPlugin.getInstance().getRegionManager().getRegion(regionName);

                if (region == null) {
                    PaperUtil.sendMessage(player, "&cNo region with this name was found.");
                    return false;
                }

                if (!region.getUsers().contains(playerName)) {
                    PaperUtil.sendMessage(player, "&cThis player is not added in this region!!");
                    return false;
                }

                region.getUsers().remove(playerName);
                region.insertOrUpdate();
                PaperUtil.sendMessage(player, "&aThe reader has been successfully removed from this region!");
            }

            if (args[0].equalsIgnoreCase("whitelist")) {
                if (!player.hasPermission("region.whitelist")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                if (args.length == 1) {
                    PaperUtil.sendMessage(player, "&cUse it as follows: /region whitelist <region>");
                    return false;
                }

                String regionName = args[1];

                Region region = RegionPlugin.getInstance().getRegionManager().getRegion(regionName);

                if (region == null) {
                    PaperUtil.sendMessage(player, "&cNo region with this name was found!");
                    return false;
                }

                if (region.getUsers().size() > 0) {
                    PaperUtil.sendMessage(player, "&6Check out the players added in this region below:");
                    for (String user : region.getUsers()) {
                        PaperUtil.sendMessage(player, " &e- " + user);
                    }
                    PaperUtil.sendMessage(player, "");
                }
                else {
                    PaperUtil.sendMessage(player, "&cThere are no users added to this region at the moment!!");
                }
            }

            if (args.length == 1) {
                if (!player.hasPermission("region.menu")) {
                    PaperUtil.sendMessage(player, "&cYou don't have permission to execute this command!");
                }

                String regionName = args[0];

                Region region = RegionPlugin.getInstance().getRegionManager().getRegion(regionName);

                if (region == null) {
                    PaperUtil.sendMessage(player, "&cNo region with this name was found.");
                    return false;
                }

                new RegionInventory(region).open(player);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }

        }
        return false;
    }


}
