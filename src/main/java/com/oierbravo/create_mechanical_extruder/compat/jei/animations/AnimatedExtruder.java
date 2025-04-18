package com.oierbravo.create_mechanical_extruder.compat.jei.animations;

import com.oierbravo.create_mechanical_extruder.components.extruder.andesite.ExtruderBlock;
import com.oierbravo.create_mechanical_extruder.register.ModBlocks;
import com.oierbravo.create_mechanical_extruder.register.ModPartials;
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
