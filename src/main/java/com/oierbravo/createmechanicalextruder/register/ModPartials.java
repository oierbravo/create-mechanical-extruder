package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartials {
    public static final PartialModel MECHANICAL_EXTRUDER_POLE = block("mechanical_extruder/pole");
    private static PartialModel block(String path) {
        return PartialModel.of(CreateMechanicalExtruder.asResource("block/" + path));
    }
    public static void load() {
        // init static fields
    }
}
