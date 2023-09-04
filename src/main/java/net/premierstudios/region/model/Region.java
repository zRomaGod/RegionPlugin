package net.premierstudios.region.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.util.Cuboid;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.entity.Player;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Region {

    private String name;
    private Cuboid cuboid;
    private List<String> users;

    public String getFormattedMaxLocation() {
        return "X: " + cuboid.getXmax() + " | Y: " + cuboid.getYmax() + " | Z: " + cuboid.getZmax();
    }

    public String getFormattedMinLocation() {
        return "X: " + cuboid.getXmin() + " | Y: " + cuboid.getYmin() + " | Z: " + cuboid.getZmin();
    }

    public void rename(String name) {
        RegionPlugin.getInstance().getRegionRepository().deleteTable(this.name);
        RegionPlugin.getInstance().getRegionManager().getRegionHashMap().remove(this.name);

        this.setName(name);
        this.insertOrUpdate();
        RegionPlugin.getInstance().getRegionManager().getRegionHashMap().put(name, this);
    }

    public void edit(Player player) {
        RegionPlugin.getInstance().getRegionManager().getEditRegionHashMap().remove(player.getName());
        PaperUtil.sendMessage(player, "&aSuccess! You have successfully edited the region locations!");
    }

    public void insertOrUpdate() {
        RegionPlugin.getInstance().getRegionRepository().update(this);
    }
}
