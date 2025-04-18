package com.oierbravo.create_mechanical_extruder.compat.jei.animations;

import com.oierbravo.create_mechanical_extruder.components.extruder.brass.BrassExtruderBlock;
import com.oierbravo.create_mechanical_extruder.register.ModBlocks;
import com.oierbravo.create_mechanical_extruder.register.ModPartials;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AnimatedBrassExtruder extends AbstractAnimatedExtruder<BrassExtruderBlock> {
    @Override
    BlockEntry<BrassExtruderBlock> getBlock() {
        return ModBlocks.MECHANICAL_BRASS_EXTRUDER;
    }

    @Override
    PartialModel getPolePartial() {
        return ModPartials.MECHANICAL_BRASS_EXTRUDER_POLE;
    }

    @Override
    Float getPoleOffset() {
        return 0.29f;
    }
}
