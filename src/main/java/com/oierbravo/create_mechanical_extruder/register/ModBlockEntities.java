package com.oierbravo.create_mechanical_extruder.register;

import com.oierbravo.create_mechanical_extruder.CreateMechanicalExtruder;
import com.oierbravo.create_mechanical_extruder.components.extruder.andesite.ExtruderBlockEntity;
import com.oierbravo.create_mechanical_extruder.components.extruder.andesite.ExtruderRenderer;
import com.oierbravo.create_mechanical_extruder.components.extruder.andesite.ExtruderVisual;
import com.oierbravo.create_mechanical_extruder.components.extruder.brass.BrassExtruderBlockEntity;
import com.oierbravo.create_mechanical_extruder.components.extruder.brass.BrassExtruderRenderer;
import com.oierbravo.create_mechanical_extruder.components.extruder.brass.BrassExtruderVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;


public class ModBlockEntities {
    public static final BlockEntityEntry<ExtruderBlockEntity> MECHANICAL_EXTRUDER = CreateMechanicalExtruder.registrate()
            .blockEntity("mechanical_extruder", ExtruderBlockEntity::new)
            .visual(() -> ExtruderVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_EXTRUDER)
            .renderer(() -> ExtruderRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassExtruderBlockEntity> MECHANICAL_BRASS_EXTRUDER = CreateMechanicalExtruder.registrate()
            .blockEntity("mechanical_brass_extruder", BrassExtruderBlockEntity::new)
            .visual(() -> BrassExtruderVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_BRASS_EXTRUDER)
            .renderer(() -> BrassExtruderRenderer::new)
            .register();

    public static void register() {}
}