package com.oierbravo.createmechanicalextruder.components.extruder;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ExtruderConfigs extends ConfigBase {
    private static final int VERSION = 1;

    public final ConfigInt cycleTime = i(200, "cycleTime", ExtruderConfigs.Comments.cycleTime);

    private static class Comments {
        static String cycleTime = "Duration of the extrudding cycle, in ticks.";
    }

    @Override
    public @NotNull String getName() {
        return "mechanical_extruder.v" + VERSION;
    }

}
