package com.oierbravo.createmechanicalextruder.foundation.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipeBuilder;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.*;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.BiomeTags;
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

        create("granite", Items.GRANITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
                .save(recipeOutput);

        create("diorite", Items.DIORITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
                .save(recipeOutput);

        create("andesite", Items.ANDESITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
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

        create("netherack", Items.NETHERRACK)
                .withBlockIngredient(Blocks.BLUE_ICE)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(BiomeTagRequirement.of(BiomeTags.IS_NETHER))
                .save(recipeOutput);

        create("end_stone", Items.END_STONE)
                .withBlockIngredient(Blocks.BLUE_ICE)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(BiomeTagRequirement.of(BiomeTags.IS_END))
                .save(recipeOutput);

        /*create("andesite", Items.ANDESITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(10))
                .save(recipeOutput);*/

        create("deepslate", Items.DEEPSLATE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MaxYRequirement.of(0))
                .withRequirement(MaxSpeedRequirement.of(32f))
                .save(recipeOutput);

        create("obsidian", Items.OBSIDIAN)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MaxSpeedRequirement.of(8f))
                .save(recipeOutput);

        create("snow_block", Items.SNOW_BLOCK)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.WATER)
                .withRequirement(MinYRequirement.of(150))
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
