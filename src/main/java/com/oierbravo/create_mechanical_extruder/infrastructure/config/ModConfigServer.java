package com.oierbravo.create_mechanical_extruder.infrastructure.config;

import com.oierbravo.create_mechanical_extruder.components.extruder.ExtruderConfigs;
import net.createmod.catnip.config.ConfigBase;

public class ModConfigServer extends ConfigBase {
    public final ExtruderConfigs mechanicalExtruder = nested(0, ExtruderConfigs::new, "Mechanical Extruder Configs");
    public final ModStress stressValues = nested(1, ModStress::new, "Stress values");

    @Override
    public String getName() {
        return "server";
    }
}
