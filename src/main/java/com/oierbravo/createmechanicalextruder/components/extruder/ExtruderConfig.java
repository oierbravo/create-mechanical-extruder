package com.oierbravo.createmechanicalextruder.components.extruder;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtruderConfig {
    public static ForgeConfigSpec.DoubleValue STRESS_IMPACT;
    public static ForgeConfigSpec.DoubleValue MINIMUM_SPEED;
    public static ForgeConfigSpec.IntValue CYCLE;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Settings for the mechanical extruder").push("mechanical_extruder");
        STRESS_IMPACT = COMMON_BUILDER
                .comment("Stress impact")
                .defineInRange("stressImpact", 4.0, 0.0, 64.0);
        MINIMUM_SPEED = COMMON_BUILDER
                .comment("Minimum required speed")
                .defineInRange("minimumSpeed", 0.0, 0.0, 254);
        CYCLE = COMMON_BUILDER
                .comment("Minimum required speed")
                .defineInRange("minimumSpeed", 200, 0, 1000);
        COMMON_BUILDER.pop();
    }
}
