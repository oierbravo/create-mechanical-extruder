package com.oierbravo.createmechanicalextruder.components.extruder.andesite;

import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderRenderer;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ExtruderRenderer extends AbstractExtruderRenderer<ExtruderBlockEntity> {
    public ExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected PartialModel getPoleModel() {
        return ModPartials.MECHANICAL_EXTRUDER_POLE;
    }
}
