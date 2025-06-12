package com.oierbravo.create_mechanical_extruder.components.extruder;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ExtruderConfigs extends ConfigBase {
    private static final int VERSION = 1;

    public final ConfigInt cycleTime = i(200, "cycleTime", ExtruderConfigs.Comments.cycleTime);
    public final ConfigInt brassOutputMultiplier = i(8, "brassOutputMultiplier", ExtruderConfigs.Comments.brassOutputMultiplier);

    private static class Comments {
        static String cycleTime = "Duration of the extrudding cycle, in ticks.";
        static String brassOutputMultiplier = "Output multiplier for brass extruder";
    }

    @Override
    public @NotNull String getName() {
        return "mechanical_extruder.v" + VERSION;
    }

}
