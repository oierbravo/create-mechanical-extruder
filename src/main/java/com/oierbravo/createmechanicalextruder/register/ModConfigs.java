package com.oierbravo.createmechanicalextruder.register;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

//From https://github.com/McJty/TutorialV3/blob/1.19/src/main/java/com/example/tutorialv3/setup/Config.java
public class ModConfigs {
    public static ForgeConfigSpec COMMON;
    public static void register() {
        registerServerConfigs();
        registerCommonConfigs();
        registerClientConfigs();
    }
    private static void registerClientConfigs() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ExtruderConfig.registerCommonConfig(COMMON_BUILDER);
        COMMON = COMMON_BUILDER.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON);
        ModConfigs.loadConfig(COMMON, FMLPaths.CONFIGDIR.get().resolve(CreateMechanicalExtruder.MODID + "-common.toml"));

    }

    private static void registerServerConfigs() {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());

    }
    //from: https://github.com/mrh0/createaddition/blob/1.19.2/src/main/java/com/mrh0/createaddition/config/Config.java
    public static void loadConfig(ForgeConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
}