package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderInstance;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderRenderer;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
public class ModBlockEntities {
    public static final BlockEntityEntry<ExtruderBlockEntity> MECHANICAL_EXTRUDER = CreateMechanicalExtruder.registrate()
            .blockEntity("mechanical_extruder", ExtruderBlockEntity::new)
            .instance(() -> ExtruderInstance::new)
            .validBlocks(ModBlocks.MECHANICAL_EXTRUDER)
            .renderer(() -> ExtruderRenderer::new)
            .register();

    public static void register() {}
}