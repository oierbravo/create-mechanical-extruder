package com.oierbravo.create_mechanical_extruder.components.extruder.andesite;

import com.oierbravo.create_mechanical_extruder.components.extruder.AbstractExtruderVisual;
import com.oierbravo.create_mechanical_extruder.register.ModPartials;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ExtruderVisual extends AbstractExtruderVisual<ExtruderBlockEntity> {
    public ExtruderVisual(VisualizationContext context, ExtruderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
    }

    @Override
    protected PartialModel getPoleModel() {
        return ModPartials.MECHANICAL_EXTRUDER_POLE;
    }
}
