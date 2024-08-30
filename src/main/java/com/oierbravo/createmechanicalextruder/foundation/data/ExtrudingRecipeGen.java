package com.oierbravo.createmechanicalextruder.foundation.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.BiomeRecipeCondition;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtrudingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
                .withBiomeCondition(BiomeRecipeCondition.of(BiomeTags.IS_OVERWORLD))
                .save(pWriter);

        create("stone", Items.STONE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .withBiomeCondition(BiomeRecipeCondition.of(BiomeTags.IS_OVERWORLD))
                .save(pWriter);

        create("basalt", Items.BASALT)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.LAVA,1000))
                .withItemIngredients(Ingredient.of(Items.BLUE_ICE))
                .withCatalyst(Items.SOUL_SOIL)
                .withCondition(BiomeRecipeCondition.of(BiomeTags.IS_NETHER))
                .save(pWriter);

        create("netherrack", Items.NETHERRACK)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.WATER,1000))
                .withCondition(BiomeRecipeCondition.of(BiomeTags.IS_NETHER))
                .withCatalyst(Items.OBSIDIAN)
                .save(pWriter);

        create("end_stone", Items.END_STONE)
                .withFluidIngredients(
                        FluidIngredient.fromFluid(Fluids.LAVA,1000),
                        FluidIngredient.fromFluid(Fluids.WATER,1000)
                )
                .withCondition(BiomeRecipeCondition.of(BiomeTags.IS_END))
                .save(pWriter);

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
