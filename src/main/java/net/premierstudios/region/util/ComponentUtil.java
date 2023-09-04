package net.premierstudios.region.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentUtil {

    private static final LegacyComponentSerializer LEGACY_OLD = LegacyComponentSerializer.builder().character('§').build();

    public static String serializeToLegacyOld(Component component) {
        return LEGACY_OLD.serialize(component);
    }


}
