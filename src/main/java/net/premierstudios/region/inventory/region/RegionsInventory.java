package net.premierstudios.region.inventory.region;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import io.github.rysefoxx.inventory.plugin.pattern.SlotIteratorPattern;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.inventory.CustomInventory;
import net.premierstudios.region.model.Region;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class RegionsInventory extends CustomInventory {

    @Deprecated
    public RegionsInventory() {
        this.inventory = RyseInventory.builder()
                .title("All Regions")
                .rows(5)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {

                        Pagination pagination = contents.pagination();
                        pagination.iterator(SlotIterator.builder().withPattern(SlotIteratorPattern.builder().define("XXXXXXXXX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XXXXXXXXX", "XXXXXXXXX").attach('O').buildPattern()).build());
                        pagination.setItemsPerPage(21);

                        for (Region region : RegionPlugin.getInstance().getRegionManager().getRegionHashMap().values()) {
                            List<String> lore = new ArrayList<>();
                            lore.add(" §fMembers: §7" + region.getUsers().size() + " players");
                            lore.add(" §fFirst Position: §7" + region.getFormattedMaxLocation());
                            lore.add(" §fSecond Position: §7" + region.getFormattedMinLocation());
                            lore.add("");
                            lore.add("§bClick to edit this region.");

                            ItemStack itemStack = new ItemStack(Material.CHEST);

                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName("§b" + region.getName());
                            itemMeta.setLore(lore);

                            itemStack.setItemMeta(itemMeta);

                            IntelligentItem intelligentItem = IntelligentItem.of(itemStack, inventoryClickEvent -> {
                                new RegionInventory(region).open(player);
                                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            });

                            pagination.addItem(intelligentItem);
                        }

                        if (!pagination.isFirst()) {
                            ItemStack itemStack = new ItemStack(Material.ARROW);

                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName("§aBack page");
                            itemStack.setItemMeta(itemMeta);

                            IntelligentItem intelligentItem = IntelligentItem.of(itemStack, event -> {
                                if (event.isLeftClick()) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                    pagination.inventory().open(player, pagination.previous().page());
                                }
                            });
                            contents.set(36, intelligentItem);
                        }

                        if (!pagination.isLast()) {
                            ItemStack itemStack = new ItemStack(Material.ARROW);

                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName("§aPrevious page");
                            itemStack.setItemMeta(itemMeta);

                            IntelligentItem intelligentItem = IntelligentItem.of(itemStack, event -> {
                                if (event.isLeftClick()) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                    pagination.inventory().open(player, pagination.next().page());
                                }
                            });
                            contents.set(44, intelligentItem);
                        }

                    }
                })
                .disableUpdateTask()
                .build(RegionPlugin.getInstance());
    }

}
