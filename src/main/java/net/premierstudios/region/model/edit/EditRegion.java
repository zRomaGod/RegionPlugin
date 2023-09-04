package net.premierstudios.region.model.edit;

import lombok.Getter;
import lombok.Setter;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.util.Cuboid;
import net.premierstudios.region.util.PaperUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class EditRegion {

    private Region region;
    private Location FIRST_POSITION;
    private Location SECOND_POSITION;

    public EditRegion(Region region) {
        this.region = region;
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

        if (this.FIRST_POSITION != null && this.SECOND_POSITION != null) this.edit(player);
    }

    private void edit(Player player) {
        Cuboid cuboid = new Cuboid(this.FIRST_POSITION, this.SECOND_POSITION);
        region.setCuboid(cuboid);

        region.insertOrUpdate();
        region.edit(player);
    }



}
