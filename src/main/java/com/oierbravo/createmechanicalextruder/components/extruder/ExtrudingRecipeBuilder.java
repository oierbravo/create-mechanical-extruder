package com.oierbravo.createmechanicalextruder.components.extruder;

import com.google.gson.JsonObject;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    public ExtrudingRecipeBuilder withCatalyst(Item catalyst) {
        params.catalyst = new ItemStack(catalyst, 1);
        return this;
    }
    public ExtrudingRecipeBuilder withCatalyst(ItemLike catalyst) {
        params.catalyst = new ItemStack(catalyst, 1);
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

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer){
        pFinishedRecipeConsumer.accept(buildFinishedRecipe());
    }

    private FinishedRecipe buildFinishedRecipe() {
        return new FinishedExtrudingRecipe(build());
    }
    public ExtrudingRecipeBuilder withCondition(RecipeCondition condition){
        params.recipeConditions.add(condition);
        return this;
    }

    public ExtrudingRecipeBuilder withBiomeCondition(BiomeRecipeCondition biomeCondition) {
        return withCondition(biomeCondition);
    }

    public ExtrudingRecipeBuilder withConditions(List<RecipeCondition> recipeConditions) {
        recipeConditions.forEach(this::withCondition);
        return this;
    }


    public static class ExtrudingRecipeParams {
        protected ResourceLocation id;
        protected NonNullList<Ingredient> itemIngredients;
        protected ProcessingOutput result;
        protected NonNullList<FluidIngredient> fluidIngredients;
        protected ItemStack catalyst;

        protected int requiredBonks;

        protected BiomeRecipeCondition biome;

        public ArrayList<RecipeCondition> recipeConditions;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            this.id = id;
            itemIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            fluidIngredients = NonNullList.create();
            catalyst = ItemStack.EMPTY;
            requiredBonks = 1;
            biome = BiomeRecipeCondition.EMPTY;
            recipeConditions = new ArrayList<>();
        }

    }
    protected static class FinishedExtrudingRecipe implements FinishedRecipe{
        protected ResourceLocation id;
        protected ExtrudingRecipe recipe;

        protected FinishedExtrudingRecipe(ExtrudingRecipe pRecipe){
            this.recipe = pRecipe;
            this.id = pRecipe.getId();
        }
        @Override
        public void serializeRecipeData(JsonObject pJson) {
            ExtrudingRecipe.Serializer.INSTANCE.toJson(pJson, recipe);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.EXTRUDING_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
