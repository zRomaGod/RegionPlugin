package net.premierstudios.region;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import net.premierstudios.region.command.RegionCommand;
import net.premierstudios.region.database.HikariDataBase;
import net.premierstudios.region.listener.RegionListener;
import net.premierstudios.region.repository.RegionRepository;
import net.premierstudios.region.util.chatresponse.ChatResponseUtil;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

@Getter
@Setter
public class RegionPlugin extends JavaPlugin {

    @Getter private static RegionPlugin instance;

    private InventoryManager inventoryManager;

    private RegionManager regionManager;

    FileConfiguration configuration = this.getConfig();

    private HikariDataBase hikariDataBase;
    private RegionRepository regionRepository;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        HikariDataBase.prepareDatabase();

        inventoryManager = new InventoryManager(this);
        inventoryManager.invoke();

        regionManager = new RegionManager();
        regionRepository.loadAll();

        ChatResponseUtil.load(this);

        Objects.requireNonNull(getCommand("region")).setExecutor(new RegionCommand());
        new RegionListener(this);

        logger("&aPlugin started successfully!");
    }

    @Override
    public void onDisable() {

        logger("&aPlugin turned off successfully!");
    }

    public static void logger(String message) {
        PaperUtil.sendMessage(Bukkit.getConsoleSender(), "[region-plugin] " + message);
    }

}
