package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderBlock;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {


    private static final CreateRegistrate REGISTRATE = CreateMechanicalExtruder.registrate()
            .creativeModeTab(() -> Create.BASE_CREATIVE_TAB);


    public static void register() {

        Create.REGISTRATE.addToSection(MECHANICAL_EXTRUDER, AllSections.KINETICS);
    }
    public static final BlockEntry<ExtruderBlock> MECHANICAL_EXTRUDER = REGISTRATE.block("mechanical_extruder", ExtruderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
}
