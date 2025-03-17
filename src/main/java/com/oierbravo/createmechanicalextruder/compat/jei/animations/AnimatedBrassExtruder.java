package com.oierbravo.createmechanicalextruder.compat.jei.animations;

import com.oierbravo.createmechanicalextruder.components.extruder.brass.BrassExtruderBlock;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AnimatedBrassExtruder extends AbstractAnimatedExtruder<BrassExtruderBlock> {
    @Override
    BlockEntry<BrassExtruderBlock> getBlock() {
        return ModBlocks.MECHANICAL_BRASS_EXTRUDER;
    }

    @Override
    PartialModel getPolePartial() {
        return ModPartials.MECHANICAL_EXTRUDER_POLE;
    }
}
