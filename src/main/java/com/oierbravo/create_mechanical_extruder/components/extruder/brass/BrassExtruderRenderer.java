package com.oierbravo.create_mechanical_extruder.components.extruder.brass;

import com.oierbravo.create_mechanical_extruder.components.extruder.AbstractExtruderRenderer;
import com.oierbravo.create_mechanical_extruder.register.ModPartials;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BrassExtruderRenderer extends AbstractExtruderRenderer<BrassExtruderBlockEntity> {
    public BrassExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected PartialModel getPoleModel() {
        return ModPartials.MECHANICAL_BRASS_EXTRUDER_POLE;
    }
}
