package com.oierbravo.createmechanicalextruder.compat.jei.animations;

import com.oierbravo.createmechanicalextruder.components.extruder.andesite.ExtruderBlock;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AnimatedExtruder extends AbstractAnimatedExtruder<ExtruderBlock> {
    @Override
    BlockEntry<ExtruderBlock> getBlock() {
        return ModBlocks.MECHANICAL_EXTRUDER;
    }

    @Override
    PartialModel getPolePartial() {
        return ModPartials.MECHANICAL_EXTRUDER_POLE;
    }
    @Override
    Float getPoleOffset() {
        return 0.44f;
    }
}
