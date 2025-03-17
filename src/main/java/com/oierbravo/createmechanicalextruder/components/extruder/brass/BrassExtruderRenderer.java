package com.oierbravo.createmechanicalextruder.components.extruder.brass;

import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderRenderer;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
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
