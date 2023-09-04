package net.premierstudios.region.repository;

import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.database.HikariDataBase;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.repository.adpter.RegionAdapter;
import net.premierstudios.region.util.Cuboid;
import org.bukkit.Location;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RegionRepository {

    private final HikariDataBase hikariDataBase = RegionPlugin.getInstance().getHikariDataBase();
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS regions (" +
            "id INTEGER NOT NULL AUTO_INCREMENT, " +
            "region_name CHAR(36) NOT NULL UNIQUE, " +
            "region_first_position TEXT NOT NULL, " +
            "region_second_position TEXT NOT NULL, " +
            "region_users TEXT NOT NULL, " +
            "PRIMARY KEY (id));";
    public static final String REMOVE_TABLE = "DELETE FROM regions WHERE region_name = ?;";
    public static final String SELECT_ALL_QUERY = "SELECT * FROM regions;";
    public static final String UPDATE_QUERY = "INSERT INTO regions " +
            "(region_name, region_first_position, region_second_position, region_users) VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE region_first_position = ?, region_second_position = ?, region_users = ?;";

    public void createTable() {
        try (final Connection connection = hikariDataBase.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTable(String name) {
        Runnable runnable = () -> {
            try (Connection connection = hikariDataBase.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(REMOVE_TABLE)) {
                    statement.setString(1, name);
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        RegionPlugin.getInstance().getHikariDataBase().executeAsync(runnable);
    }

    public void loadAll() {
        try (Connection connection = hikariDataBase.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("region_name");

                        Location FIRST_LOCATION = RegionAdapter.deserializeLocation(resultSet.getString("region_first_position"));
                        Location SECOND_LOCATION = RegionAdapter.deserializeLocation(resultSet.getString("region_second_position"));

                        List<String> users = RegionAdapter.deserializeUsers(resultSet.getString("region_users"));

                        Cuboid cuboid = new Cuboid(FIRST_LOCATION, SECOND_LOCATION);

                        Region region = new Region(name, cuboid, users);
                        RegionPlugin.getInstance().getRegionManager().getRegionHashMap().put(region.getName(), region);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Region region) {
        Runnable runnable = () -> {
            try (Connection connection = hikariDataBase.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
                    statement.setString(1, region.getName());

                    Location FIRST_LOCATION = new Location(region.getCuboid().getWorld(), region.getCuboid().getXmax(), region.getCuboid().getYmax(), region.getCuboid().getZmax());
                    Location SECOND_LOCATION = new Location(region.getCuboid().getWorld(), region.getCuboid().getXmin(), region.getCuboid().getYmin(), region.getCuboid().getZmin());

                    statement.setString(2, RegionAdapter.serializeLocation(FIRST_LOCATION));
                    statement.setString(3, RegionAdapter.serializeLocation(SECOND_LOCATION));
                    statement.setString(4, RegionAdapter.serializeUsers(region.getUsers()));


                    statement.setString(5, RegionAdapter.serializeLocation(FIRST_LOCATION));
                    statement.setString(6, RegionAdapter.serializeLocation(SECOND_LOCATION));
                    statement.setString(7, RegionAdapter.serializeUsers(region.getUsers()));

                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        RegionPlugin.getInstance().getHikariDataBase().executeAsync(runnable);
    }

}
