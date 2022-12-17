package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;

public class ExtrudingRecipeBuilder {
    protected ExtrudingRecipeBuilder.ExtrudingRecipeParams params;
    protected List<ICondition> recipeConditions;
    public ExtrudingRecipeBuilder(ResourceLocation recipeId) {
        params = new ExtrudingRecipeBuilder.ExtrudingRecipeParams(recipeId);
        recipeConditions = new ArrayList<>();


    }
    public ExtrudingRecipeBuilder withItemIngredients(Ingredient... itemIngredients) {
        return withItemIngredients(NonNullList.of(Ingredient.EMPTY, itemIngredients));
    }

    public ExtrudingRecipeBuilder withItemIngredients(NonNullList<Ingredient> itemIngredients) {
        params.itemIngredients = itemIngredients;
        return this;
    }
    public ExtrudingRecipeBuilder withSingleItemOutput(ItemStack output) {
        params.result = new ProcessingOutput(output, 1.0F);
        return this;
    }
    public ExtrudingRecipeBuilder withSingleItemOutput(ProcessingOutput output) {
        params.result = output;
        return this;
    }
    public ExtrudingRecipeBuilder withCatalyst(ItemStack catalyst) {
        params.catalyst = catalyst;
        return this;
    }

    public ExtrudingRecipeBuilder withFluidIngredients(FluidIngredient... ingredients) {
        return withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, ingredients));
    }

    public ExtrudingRecipeBuilder withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
        params.fluidIngredients = ingredients;
        return this;
    }
    public ExtrudingRecipeBuilder requiredBonks(int requiredBonks) {
        params.requiredBonks = requiredBonks;
        return this;
    }
    public ExtrudingRecipe build(){
        return new ExtrudingRecipe(params);
    }



    public static class ExtrudingRecipeParams {

        protected ResourceLocation id;
        protected NonNullList<Ingredient> itemIngredients;
        protected ProcessingOutput result;
        protected NonNullList<FluidIngredient> fluidIngredients;
        protected ItemStack catalyst;

        protected int requiredBonks;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            this.id = id;
            itemIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            fluidIngredients = NonNullList.create();
            catalyst = ItemStack.EMPTY;
            requiredBonks = 1;
        }

    }
}
