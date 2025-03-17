package com.oierbravo.createmechanicalextruder.infrastructure.data;

import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class CraftingRecipeGen extends RecipeProvider {
    public CraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        MechanicalCraftingRecipeBuilder.shapedRecipe(ModBlocks.MECHANICAL_BRASS_EXTRUDER.get())
                .key('G', Ingredient.of(AllBlocks.METAL_GIRDER))
                .key('T', Ingredient.of(AllBlocks.FRAMED_GLASS_TRAPDOOR))
                .key('C', Ingredient.of(AllBlocks.BRASS_CASING))
                .key('P', Ingredient.of(AllTags.commonItemTag("plates/brass")))
                .patternLine(" G ")
                .patternLine("PGP")
                .patternLine("TCT")
                .patternLine("PTP")
                .build(recipeOutput);


    }
    @Override
    public final String getName() {
        return "Mechanical Extruder's crafting recipes.";
    }
}
