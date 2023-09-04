package net.premierstudios.region;

import lombok.Getter;
import net.premierstudios.region.model.create.CreateRegion;
import net.premierstudios.region.model.Region;
import net.premierstudios.region.model.edit.EditRegion;
import java.util.HashMap;

@Getter
public class RegionManager {

    private final HashMap<String, CreateRegion> createRegionHashMap = new HashMap<>();
    private final HashMap<String, EditRegion> editRegionHashMap = new HashMap<>();
    private final HashMap<String, Region> regionHashMap = new HashMap<>();

    public Region getRegion(String regionName) {
        return this.regionHashMap.getOrDefault(regionName, null);
    }

}
