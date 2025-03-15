package com.oierbravo.createmechanicalextruder.foundation.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipeBuilder;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MaxYRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MinYRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.SpeedRequirement;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ExtrudingRecipeGen extends RecipeProvider {
    public ExtrudingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }


    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        create("cobblestone", Items.COBBLESTONE)

                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .save(recipeOutput);

        create("stone", Items.STONE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .save(recipeOutput);

        create("basalt", Items.BASALT)
                .withBlockIngredient(Blocks.BLUE_ICE)
                .withBlockIngredient(Blocks.LAVA)
                .withCatalyst(Blocks.SOUL_SOIL)
                .save(recipeOutput);

        create("limestone", AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get())
                .withBlockIngredient("create:honey")
                .withBlockIngredient(Blocks.LAVA)
                .save(recipeOutput);

        create("scoria", AllPaletteStoneTypes.SCORIA.getBaseBlock().get())
                .withBlockIngredient(Blocks.LAVA)
                .withBlockIngredient("create:chocolate")
                .save(recipeOutput);

        /*create("end_stone", Items.END_STONE)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.LAVA,1000))
                .withItemIngredients(Ingredient.of(Items.BLUE_ICE))
                .withBiomeRequirement(BiomeRequirement.of(BiomeTags.IS_END))
                .save(pWriter);*/
        /*create("end_stone", Items.END_STONE)
                .withFluidIngredients(FluidIngredient.fromFluid(Fluids.LAVA,1000))
                .withItemIngredients(Ingredient.of(Items.BLUE_ICE))
                .withBiomeRequirement(BiomeRequirement.of(BiomeTags.IS_END))
                .save(pWriter);*/

        create("andesite", Items.ANDESITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(-50))
                .withRequirement(MaxYRequirement.of(-10))
                .save(recipeOutput);

        create("deepslate", Items.DEEPSLATE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MaxYRequirement.of(0))
                .withRequirement(SpeedRequirement.of(128f))
                .save(recipeOutput);


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
