package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderInstance;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderRenderer;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderTileEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
public class ModTiles {
    public static final BlockEntityEntry<ExtruderTileEntity> MECHANICAL_EXTRUDER = CreateMechanicalExtruder.registrate()
            .tileEntity("mechanical_extruder", ExtruderTileEntity::new)
            .instance(() ->ExtruderInstance::new)
            .validBlocks(ModBlocks.MECHANICAL_EXTRUDER)
            .renderer(() -> ExtruderRenderer::new)
            .register();

    public static void register() {}
}