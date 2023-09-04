package net.premierstudios.region.inventory.region;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.inventory.CustomInventory;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.model.edit.EditRegion;
import net.premierstudios.region.util.PaperUtil;
import net.premierstudios.region.util.chatresponse.ChatResponse;
import net.premierstudios.region.util.chatresponse.ChatResponseUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RegionInventory extends CustomInventory {

    @Deprecated
    public RegionInventory(Region region) {
        this.inventory = RyseInventory.builder()
                .title("Region Settings")
                .rows(3)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {

                        // CHANGE NAME
                        ItemStack itemStackName = new ItemStack(Material.NAME_TAG);
                        ItemMeta itemMetaName = itemStackName.getItemMeta();
                        itemMetaName.setDisplayName("§bRedefine Name");

                        List<String> loreName = new ArrayList<>();
                        loreName.add(" §fName: §7" + region.getName());
                        loreName.add("");
                        loreName.add("§bClick to redefine the name.");

                        itemMetaName.setLore(loreName);

                        itemStackName.setItemMeta(itemMetaName);

                        contents.set(11, IntelligentItem.of(itemStackName, event -> {
                            player.closeInventory();
                            ChatResponse chatResponse = new ChatResponse(name -> {

                                if (RegionPlugin.getInstance().getRegionManager().getRegionHashMap().containsKey(name)) {
                                    PaperUtil.sendMessage(player, "&cOPS! That region name already exists!");
                                    return;
                                }

                                region.rename(name);
                                PaperUtil.sendMessage(player, "&aSUCCESS! This region now has a new name!");

                            }, "&cOperation successfully canceled!");

                            PaperUtil.sendMessage(player, "");
                            PaperUtil.sendMessage(player, "&aEnter the new name of the region you want");
                            PaperUtil.sendMessage(player, "&7If you want to cancel, type 'cancel'.");
                            PaperUtil.sendMessage(player, "");

                            ChatResponseUtil.create(player, chatResponse);
                        }));


                        // MEMBERS
                        ItemStack itemStackMembers = new ItemStack(Material.SPRUCE_SIGN);
                        ItemMeta itemMetaMembers = itemStackMembers.getItemMeta();
                        itemMetaMembers.setDisplayName("§eMembers");

                        List<String> loreMembers = new ArrayList<>();
                        loreMembers.add(" §fMembers: §7" + region.getUsers().size() + " players");
                        loreMembers.add("");
                        loreMembers.add("§eLeft Click: §7Add player in whitelist.");
                        loreMembers.add("§eRight Click: §7Remove player in whitelist.");
                        loreMembers.add("§eRight + Shit Click: §7List all players in whitelist.");

                        itemMetaMembers.setLore(loreMembers);

                        itemStackMembers.setItemMeta(itemMetaMembers);

                        contents.set(13, IntelligentItem.of(itemStackMembers, event -> {
                            player.closeInventory();

                            if (event.isRightClick() && event.isShiftClick()) {
                                player.chat("/region whitelist " + region.getName());
                                return;
                            }

                            if (event.isLeftClick()) {
                                ChatResponse chatResponse = new ChatResponse(target -> {
                                    Bukkit.getScheduler().runTask(RegionPlugin.getInstance(), () -> player.chat("/region add {region} {player}".replace("{player}", target).replace("{region}", region.getName())));
                                }, "&cOperation successfully canceled!");

                                PaperUtil.sendMessage(player, "");
                                PaperUtil.sendMessage(player, "&aEnter the player you want to add");
                                PaperUtil.sendMessage(player, "&7If you want to cancel, type 'cancel'.");
                                PaperUtil.sendMessage(player, "");

                                ChatResponseUtil.create(player, chatResponse);
                            }

                            if (event.isRightClick()) {

                                if (region.getUsers().size() == 0) {
                                    PaperUtil.sendMessage(player, "&cThis region has no people added.");
                                    return;
                                }

                                ChatResponse chatResponse = new ChatResponse(target -> {
                                    Bukkit.getScheduler().runTask(RegionPlugin.getInstance(), () -> player.chat("/region remove {region} {player}".replace("{player}", target).replace("{region}", region.getName())));
                                }, "&cOperation successfully canceled!");

                                PaperUtil.sendMessage(player, "");
                                PaperUtil.sendMessage(player, "&aEnter the player you want to remove");
                                PaperUtil.sendMessage(player, "&7If you want to cancel, type 'cancel'.");
                                PaperUtil.sendMessage(player, "");

                                ChatResponseUtil.create(player, chatResponse);
                            }

                        }));


                        // REDEFINE LOCATION
                        ItemStack itemStackLocation = new ItemStack(Material.REPEATER);
                        ItemMeta itemMetaLocation = itemStackLocation.getItemMeta();
                        itemMetaLocation.setDisplayName("§6Redefine Locations");

                        List<String> loreLocation = new ArrayList<>();
                        loreLocation.add(" §fFirst Position: §7" + region.getFormattedMaxLocation());
                        loreLocation.add(" §fSecond Position: §7" + region.getFormattedMinLocation());
                        loreLocation.add("");
                        loreLocation.add("§6Click to redefine location.");

                        itemMetaLocation.setLore(loreLocation);

                        itemStackLocation.setItemMeta(itemMetaLocation);

                        contents.set(15, IntelligentItem.of(itemStackLocation, event -> {
                            player.closeInventory();

                            PaperUtil.sendMessage(player, "&eStarting the region editing process....");

                            EditRegion editRegion = new EditRegion(region);
                            RegionPlugin.getInstance().getRegionManager().getEditRegionHashMap().put(player.getName(), editRegion);
                        }));
                    }
                })

                .disableUpdateTask()
                .build(RegionPlugin.getInstance());
    }

}
