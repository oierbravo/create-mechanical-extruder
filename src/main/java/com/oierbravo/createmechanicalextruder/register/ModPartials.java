package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.ModConstants;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartials {
    public static final PartialModel MECHANICAL_EXTRUDER_POLE = block("mechanical_extruder/pole");
    public static final PartialModel MECHANICAL_BRASS_EXTRUDER_POLE = block("mechanical_brass_extruder/pole");
    private static PartialModel block(String path) {
        return PartialModel.of(ModConstants.asResource("block/" + path));
    }
    public static void init() {
        // init static fields
    }
}
