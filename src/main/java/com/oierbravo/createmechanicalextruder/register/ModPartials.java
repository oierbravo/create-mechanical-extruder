package com.oierbravo.createmechanicalextruder.register;

import com.jozufozu.flywheel.core.PartialModel;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;

public class ModPartials {
    public static final PartialModel MECHANICAL_EXTRUDER_POLE = block("mechanical_extruder/pole");
    private static PartialModel block(String path) {
        return new PartialModel(CreateMechanicalExtruder.asResource("block/" + path));
    }
    public static void load() {
        // init static fields
    }
}
