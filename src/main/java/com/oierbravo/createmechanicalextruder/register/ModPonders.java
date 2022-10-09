package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.ponder.PonderScenes;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;

public class ModPonders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateMechanicalExtruder.MODID);

    public static void register() {
        //PonderRegistry..(CreateAddition.MODID);

        HELPER.addStoryBoard(ModBlocks.MECHANICAL_EXTRUDER, "mechanical_extruder_basic", PonderScenes::extruderBasic, PonderTag.KINETIC_APPLIANCES);

        PonderRegistry.TAGS.forTag(PonderTag.KINETIC_APPLIANCES)
                .add(ModBlocks.MECHANICAL_EXTRUDER);
    }
}
