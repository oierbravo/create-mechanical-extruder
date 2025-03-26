package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.ModConstants;
import com.oierbravo.createmechanicalextruder.components.extruder.andesite.ExtruderBlock;
import com.oierbravo.createmechanicalextruder.components.extruder.brass.BrassExtruderBlock;
import com.oierbravo.createmechanicalextruder.infrastructure.config.ModStress;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import static com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {
    public static final BlockEntry<ExtruderBlock> MECHANICAL_EXTRUDER = REGISTRATE.block("mechanical_extruder", ExtruderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(ModStress.setImpact(4.0))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .define('S', AllBlocks.SHAFT)
                    .define('A', AllBlocks.ANDESITE_CASING)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .pattern(" S ")
                    .pattern("GAG")
                    .pattern(" G ")
                    .unlockedBy("has_andesite_casing", RegistrateRecipeProvider.has(AllTags.AllItemTags.CASING.tag))
                    .save(p, ModConstants.asResource("crafting/" + c.getName())))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BrassExtruderBlock> MECHANICAL_BRASS_EXTRUDER = REGISTRATE.block("mechanical_brass_extruder", BrassExtruderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(ModStress.setImpact(16.0))
            .item()
            .transform(customItemModel())

            .register();

    public static void register() {}

}
