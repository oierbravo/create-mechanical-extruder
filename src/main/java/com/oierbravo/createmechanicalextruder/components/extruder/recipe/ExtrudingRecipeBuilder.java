package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.oierbravo.mechanical_lemon_lib.foundation.recipe.BaseRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ExtrudingRecipeBuilder extends BaseRecipeBuilder<ExtrudingRecipe, ExtrudingRecipe.ExtrudingRecipeParams> {

    public ExtrudingRecipeBuilder( ResourceLocation id) {
        super(id);
        params = new ExtrudingRecipe.ExtrudingRecipeParams(id);

    }

  //  protected List<RecipeRequirement> recipeRequirements;

//    protected List<ICondition> recipeConditions;


    /*public ExtrudingRecipeBuilder(ResourceLocation recipeId) {
        params = new ExtrudingRecipeBuilder.ExtrudingRecipeParams(recipeId);
        recipeRequirements = new ArrayList<>();
        recipeConditions = new ArrayList<>();
    }*/
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
    public ExtrudingRecipeBuilder withCatalyst(Block catalyst) {
        return withCatalyst(BlockPredicate.Builder.block().of(catalyst).build());
    }

    public ExtrudingRecipeBuilder withCatalyst(BlockPredicate catalyst) {
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
        return new ExtrudingRecipe(this.params);
    }

    public ExtrudingRecipeBuilder withSingleItemOutput(NonNullList<ProcessingOutput> results) {
        if(results.stream().findFirst().isPresent())
            params.result = results.stream().findFirst().get();
        return this;
    }

    /*@Override
    public @NotNull ExtrudingRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }*/
   /* public ExtrudingRecipeBuilder withRequirement(RecipeRequirement requirement){
        params.recipeRequirements.add(requirement);
        return this;
    }

    public ExtrudingRecipeBuilder withBiomeRequirement(BiomeRequirement biomeRequirement) {
        return withRequirement(biomeRequirement);
    }
    */
    /*public static class ExtrudingRecipeParams {
        protected ResourceLocation id;
        protected NonNullList<Ingredient> itemIngredients;
        protected ProcessingOutput result;
        protected NonNullList<FluidIngredient> fluidIngredients;
        protected ItemStack catalyst;

        protected int requiredBonks;

        protected BiomeRequirement biome;

        public ArrayList<RecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            assert id != null;
            this.id = id;
            itemIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            fluidIngredients = NonNullList.create();
            catalyst = ItemStack.EMPTY;
            requiredBonks = 1;
            biome = BiomeRequirement.EMPTY;
            recipeRequirements = new ArrayList<>();
        }

    }*/
    /*protected static class FinishedExtrudingRecipe implements FinishedRecipe{

        protected ResourceLocation id;
        protected ExtrudingRecipe recipe;
        private List<ICondition> recipeConditions;


        protected FinishedExtrudingRecipe(ExtrudingRecipe pRecipe , List<ICondition> pRecipeConditions){
            this.recipe = pRecipe;
            this.id = pRecipe.getId();
            this.recipeConditions = pRecipeConditions;
        }
        @Override
        public void serializeRecipeData(JsonObject pJson) {
            ExtrudingRecipe.Serializer.INSTANCE.toJson(pJson, recipe);

            if (recipeConditions.isEmpty())
                return;

            JsonArray conds = new JsonArray();
            recipeConditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            pJson.add("conditions", conds);
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
    }*/
}
