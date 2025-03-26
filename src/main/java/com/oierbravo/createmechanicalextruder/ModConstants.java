package com.oierbravo.createmechanicalextruder;

import net.minecraft.resources.ResourceLocation;

public class ModConstants {
    public static final String MODID = "create_mechanical_extruder";
    public static final String DISPLAY_NAME = "Create Mechanical Extruder";
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
