package com.oierbravo.create_mechanical_extruder.components.extruder.brass;

import com.oierbravo.create_mechanical_extruder.components.extruder.AbstractExtruderVisual;
import com.oierbravo.create_mechanical_extruder.register.ModPartials;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class BrassExtruderVisual extends AbstractExtruderVisual<BrassExtruderBlockEntity> {
    public BrassExtruderVisual(VisualizationContext context, BrassExtruderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
    }

    @Override
    protected PartialModel getPoleModel() {
        return ModPartials.MECHANICAL_BRASS_EXTRUDER_POLE;
    }
}
