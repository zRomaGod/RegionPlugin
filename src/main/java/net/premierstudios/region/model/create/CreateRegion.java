package net.premierstudios.region.model.create;

import lombok.Getter;
import lombok.Setter;
import net.premierstudios.region.RegionPlugin;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.util.Cuboid;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
@Setter
public class CreateRegion {

    private String name;
    private Location FIRST_POSITION;
    private Location SECOND_POSITION;

    public CreateRegion(String name) {
        this.name = name;
        this.FIRST_POSITION = null;
        this.SECOND_POSITION = null;
    }

    public void positionDefined(Player player, Location location, String position) {
        if (position.equalsIgnoreCase("FIRST")) {
            this.setFIRST_POSITION(location);
            PaperUtil.sendMessage(player, "&eThe region's #1 POSITION has been set!");
        }

        if (position.equalsIgnoreCase("SECOND")) {
            this.setSECOND_POSITION(location);
            PaperUtil.sendMessage(player, "&eThe region's #2 POSITION has been set!");
        }

        if (this.FIRST_POSITION != null && this.SECOND_POSITION != null) this.create(player);
    }


    private void create(Player player) {
        Cuboid cuboid = new Cuboid(this.FIRST_POSITION, this.SECOND_POSITION);
        Region region = new Region(this.name, cuboid, new ArrayList<>());

        region.insertOrUpdate();
        RegionPlugin.getInstance().getRegionManager().getRegionHashMap().put(region.getName(), region);

        RegionPlugin.getInstance().getRegionManager().getCreateRegionHashMap().remove(player.getName());

        PaperUtil.sendMessage(player, "&aSuccess! You have successfully edited the region locations!");
    }


}
