package net.premierstudios.region.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.repository.RegionRepository;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HikariDataBase {

    @Getter
    public HikariDataSource dataSource;

    public HikariDataBase(String ip, String database, String user, String password) throws Exception {
        openConnection(ip, database, user, password);
    }

    private void openConnection(String ip, String database, String user, String password) throws Exception {
        if (dataSource != null) return;

        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            hikariConfig.setJdbcUrl(String.format("jdbc:mariadb://%s/%s", ip, database));

            dataSource = new HikariDataSource(hikariConfig);

            Logger.getLogger("com.zaxxer.hikari").setLevel(Level.OFF);
        } catch (Exception e) {
            throw new Exception("Unable to start connection to MySQL Hikari database.", e);
        }
    }

    public void executeAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            return null;
        }
    }


    public static void prepareDatabase() {
        try {
            String ip = RegionPlugin.getInstance().getConfiguration().getString("Database.IP");
            String database = RegionPlugin.getInstance().getConfiguration().getString("Database.Database");
            String user = RegionPlugin.getInstance().getConfiguration().getString("Database.User");
            String password = RegionPlugin.getInstance().getConfiguration().getString("Database.Password");

            RegionPlugin.getInstance().setHikariDataBase(new HikariDataBase(ip, database, user, password));

            RegionPlugin.getInstance().setRegionRepository(new RegionRepository());
            RegionPlugin.getInstance().getRegionRepository().createTable();

            PaperUtil.sendMessage(Bukkit.getConsoleSender(), "&aDatabase connection successfully established.");
        } catch (Exception e) {
            PaperUtil.sendMessage(Bukkit.getConsoleSender(), "&cAn error occurred when initializing the connection to the database.");
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

}