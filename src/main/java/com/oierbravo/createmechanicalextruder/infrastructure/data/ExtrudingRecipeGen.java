package com.oierbravo.createmechanicalextruder.infrastructure.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipeBuilder;
import com.oierbravo.mechanicals.foundation.recipe.requirements.BiomeTagRequirement;
import com.oierbravo.mechanicals.foundation.recipe.requirements.MaxSpeedRequirement;
import com.oierbravo.mechanicals.foundation.recipe.requirements.MaxYRequirement;
import com.oierbravo.mechanicals.foundation.recipe.requirements.MinYRequirement;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
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
                .withBlockIngredients(Blocks.WATER,Blocks.LAVA)
                .save(recipeOutput);

        create("granite", Items.GRANITE)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
                .save(recipeOutput);

        create("diorite", Items.DIORITE)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
                .save(recipeOutput);

        create("andesite", Items.ANDESITE)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(60))
                .save(recipeOutput);

        create("basalt", Items.BASALT)
                .withBlockIngredients(Blocks.BLUE_ICE,Blocks.LAVA)
                .withCatalyst(Blocks.SOUL_SOIL)
                .save(recipeOutput);

        create("limestone", AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get())
                .withBlockIngredients(block("create:honey"),Blocks.LAVA)
                .save(recipeOutput);

        create("scoria", AllPaletteStoneTypes.SCORIA.getBaseBlock().get())
                .withBlockIngredients(Blocks.LAVA, block("create:chocolate"))
                .save(recipeOutput);

        create("netherack", Items.NETHERRACK)
                .withBlockIngredients(Blocks.BLUE_ICE, Blocks.LAVA)
                .withRequirement(BiomeTagRequirement.of(BiomeTags.IS_NETHER))
                .save(recipeOutput);

        create("end_stone", Items.END_STONE)
                .withBlockIngredients(Blocks.BLUE_ICE, Blocks.LAVA)
                .withRequirement(BiomeTagRequirement.of(BiomeTags.IS_END))
                .save(recipeOutput);

        /*create("andesite", Items.ANDESITE)
                .withBlockIngredient(Blocks.WATER)
                .withBlockIngredient(Blocks.LAVA)
                .withRequirement(MinYRequirement.of(0))
                .withRequirement(MaxYRequirement.of(10))
                .save(recipeOutput);*/

        create("deepslate", Items.DEEPSLATE)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .withRequirement(MaxYRequirement.of(0))
                .withRequirement(MaxSpeedRequirement.of(16f))
                .save(recipeOutput);

        create("obsidian", Items.OBSIDIAN)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .withRequirement(MaxSpeedRequirement.of(4f))
                .save(recipeOutput);

        create("snow_block", Items.SNOW_BLOCK)
                .withSingleBlockIngredient(Blocks.WATER)
                .withRequirement(MinYRequirement.of(150))
                .save(recipeOutput);

        createAdvanced("advanced_obsidian", Items.OBSIDIAN)
                .withBlockIngredients(Blocks.WATER, Blocks.LAVA)
                .consumeBlocksSecondBlock()
                .withCatalyst(Blocks.OBSIDIAN)
                .save(recipeOutput);

        createAdvanced("advanced_netherrack", Items.NETHERRACK)
                .withBlockIngredients(Blocks.BLUE_ICE, Blocks.LAVA)
                .consumeBlocksSecondBlock()
                .withCatalyst(Blocks.NETHERRACK)
                .save(recipeOutput);

        createAdvanced("advanced_end_stone", Items.END_STONE)
                .withBlockIngredients(Blocks.BLUE_ICE, Blocks.LAVA)
                .consumeBlocksSecondBlock()
                .withCatalyst(Blocks.END_STONE)
                .save(recipeOutput);


    }
    private Block block(String resourceLocationString){
        return block(ResourceLocation.parse(resourceLocationString));
    }
    private Block block(ResourceLocation resourceLocation){
        return BuiltInRegistries.BLOCK.get(resourceLocation);
    }
    private ExtrudingRecipeBuilder create(String id, Block output){
        return new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource("extruding/" + id))
                .withSingleItemOutput(new ProcessingOutput(new ItemStack(output),1));
    }

    private ExtrudingRecipeBuilder create(String id, Item output){
        return new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource("extruding/" + id))
                .withSingleItemOutput(new ProcessingOutput(new ItemStack(output),1));
    }

    private ExtrudingRecipeBuilder createAdvanced(String id, Item output){
        return new ExtrudingRecipeBuilder(CreateMechanicalExtruder.asResource("extruding/" + id))
                .withSingleItemOutput(new ProcessingOutput(new ItemStack(output),1))
                .isAdvanced(true);
    }



    @Override
    public final String getName() {
        return "Mechanical extruders's extruding recipes.";
    }

}
