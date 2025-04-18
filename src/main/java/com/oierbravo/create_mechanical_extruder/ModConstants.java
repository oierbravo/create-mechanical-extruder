package com.oierbravo.create_mechanical_extruder;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceLocation;

public class ModConstants {
    public static final String MODID = "create_mechanical_extruder";
    public static final String DISPLAY_NAME = "Create Mechanical Extruder";
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static class ModLang extends net.createmod.catnip.lang.Lang {
        public ModLang() {
            super();
        }
        public static LangBuilder builder() {
            return new LangBuilder(MODID);
        }
        public static LangBuilder translate(String langKey, Object... args) {
            return builder().translate(langKey, args);
        }
    }
}
