package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.oierbravo.mechanicals.foundation.recipe.BaseRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ExtrudingRecipeBuilder extends BaseRecipeBuilder<ExtrudingRecipe, ExtrudingRecipe.ExtrudingRecipeParams, ExtrudingRecipeBuilder> {

    public ExtrudingRecipeBuilder( ResourceLocation id) {
        super(id);
        params = new ExtrudingRecipe.ExtrudingRecipeParams(id);

    }

   /* public ExtrudingRecipeBuilder withBlockIngredient(Block... blockIngredients) {
        params.blockPredicateIngredients.add(BlockPredicate.Builder.block().of(blockIngredients).build());
        return this;
    }*/

    /*public ExtrudingRecipeBuilder withBlockIngredient(BlockPredicate blockIngredient) {
        params.blockPredicateIngredients.add(blockIngredient);
        return this;
    }*/
    public ExtrudingRecipeBuilder withSingleBlockIngredient(Block ingredient){
        return withBlockIngredients(ingredient, ingredient);
    }
    public ExtrudingRecipeBuilder withBlockIngredients(Block firstBlockIngredients, Block secondBlockIngredient) {
        return withBlockIngredients(
                Couple.create(
                            BlockPredicate.Builder.block().of(firstBlockIngredients).build(),
                            BlockPredicate.Builder.block().of(secondBlockIngredient).build()
                        )
        );
    }
    public ExtrudingRecipeBuilder withBlockIngredients(BlockPredicate firstBlockIngredients, BlockPredicate secondBlockIngredient) {
        return withBlockIngredients(Couple.create(firstBlockIngredients,secondBlockIngredient));
    }
    public ExtrudingRecipeBuilder withBlockIngredients(Couple<BlockPredicate> blockIngredients) {
        params.blockPredicateIngredients = blockIngredients;
        return this;
    }
    /*public ExtrudingRecipeBuilder withBlockIngredients(BlockPredicate... blockIngredients) {
        return withBlockIngredients(NonNullList.of(BlockPredicate.Builder.block().build(), blockIngredients));
    }*/
    /*public ExtrudingRecipeBuilder withBlockIngredients(NonNullList<BlockPredicate> blockIngredients) {
        params.blockPredicateIngredients = blockIngredients;
        return this;
    }*/
    /*public ExtrudingRecipeBuilder withBlockIngredient(ResourceLocation resourceLocation) {
        return withBlockIngredient(BuiltInRegistries.BLOCK.get(resourceLocation));
    }
    public ExtrudingRecipeBuilder withBlockIngredient(String resourceLocationString) {
        return withBlockIngredient(ResourceLocation.parse(resourceLocationString));
    }*/

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
    /*public ExtrudingRecipeBuilder withFluidIngredients(FluidIngredient... ingredients) {
        return withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, ingredients));
    }
*/
    /*public ExtrudingRecipeBuilder withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
        params.fluidIngredients = ingredients;
        return this;
    }*/
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

    /*public ExtrudingRecipeBuilder withBlockIngredient(TagKey<Fluid> tag) {
        BlockPredicate.Builder.block().of(tag);
    }*/
}
