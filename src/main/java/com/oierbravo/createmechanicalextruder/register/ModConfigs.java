package com.oierbravo.createmechanicalextruder.register;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

//From https://github.com/McJty/TutorialV3/blob/1.19/src/main/java/com/example/tutorialv3/setup/Config.java
public class ModConfigs {
    public static ConfigSpec COMMON;
    public static void register(ModContainer modContainer) {
        registerServerConfigs(modContainer);
        registerCommonConfigs(modContainer);
    }

    private static void registerCommonConfigs(ModContainer modContainer) {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        ExtruderConfig.registerCommonConfig(COMMON_BUILDER);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerServerConfigs(ModContainer modContainer) {
        ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, SERVER_BUILDER.build());

    }
}