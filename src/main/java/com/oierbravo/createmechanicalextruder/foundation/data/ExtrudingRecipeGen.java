package com.oierbravo.createmechanicalextruder.foundation.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipeBuilder;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Consumer;

public class ExtrudingRecipeGen extends RecipeProvider {
    public ExtrudingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        create("cobblestone", Items.COBBLESTONE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .save(pWriter);

        create("stone", Items.STONE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .save(pWriter);

        create("basalt", Items.BASALT)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000)
                )
                .withItemIngredients(Ingredient.of(Blocks.BLUE_ICE))
                .withCatalyst(Blocks.SOUL_SOIL)
                .save(pWriter);

        create("limestone", AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get())
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(AllFluids.HONEY.get(), 1000)
                )
                .save(pWriter);

        create("scoria", AllPaletteStoneTypes.SCORIA.getBaseBlock().get())
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(AllFluids.CHOCOLATE.get(),1000)
                )
                .save(pWriter);

        /*create("netherrak", Items.NETHERRACK)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.LAVA,1000))
                .withItemIngredients(Ingredient.of(Items.BLUE_ICE))
                .withBiomeRequirement(BiomeRequirement.of(Biomes.IS_NETHER))
                .save(pWriter);

        create("end_stone", Items.END_STONE)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.LAVA,1000))
                .withItemIngredients(Ingredient.of(Items.BLUE_ICE))
                .withBiomeRequirement(BiomeRequirement.of(BiomeTags.IS_END))
                .save(pWriter);*/

        /*create("andesite", Items.ANDESITE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .withRequirement(MinHeightRequirement.of(-50))
                .withRequirement(MaxHeightRequirement.of(-10))
                .save(pWriter);

        create("deepslate", Items.DEEPSLATE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .withRequirement(MaxHeightRequirement.of(64))
                .withRequirement(SpeedRequirement.of(128))
                .save(pWriter);*/


    }

    private ExtrudingRecipeBuilder create(String id, Block output){
        return new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource("extruding/" + id))
                .withSingleItemOutput(new ProcessingOutput(new ItemStack(output),1));
    }

    private ExtrudingRecipeBuilder create(String id, Item output){
        return new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource("extruding/" + id))
                .withSingleItemOutput(new ProcessingOutput(new ItemStack(output),1));
    }

    @Override
    public final String getName() {
        return "Mechanical extruders's extruding recipes.";
    }

}
