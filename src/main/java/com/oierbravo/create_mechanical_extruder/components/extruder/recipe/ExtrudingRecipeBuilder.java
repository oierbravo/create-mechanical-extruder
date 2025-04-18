package com.oierbravo.create_mechanical_extruder.components.extruder.recipe;

import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ExtrudingRecipeBuilder extends AbstractMechanicalRecipeBuilder<ExtrudingRecipe, ExtrudingRecipe.ExtrudingRecipeParams, ExtrudingRecipeBuilder> {

    public ExtrudingRecipeBuilder() {
        params = new ExtrudingRecipe.ExtrudingRecipeParams(id);

    }
    @Override
    public ExtrudingRecipeBuilder create() {
        params = new ExtrudingRecipe.ExtrudingRecipeParams(id);
        return this;
    }

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

    public ExtrudingRecipeBuilder requiredBonks(int requiredBonks) {
        params.requiredBonks = requiredBonks;
        return this;
    }
    public ExtrudingRecipeBuilder isAdvanced(boolean value) {
        params.isAdvanced = value;
        return this;
    }

    public ExtrudingRecipeBuilder consumeBlocks(Couple<Boolean> pConsumeBlocks){
        params.consumeBlocks = pConsumeBlocks;
        return this;
    }
    public ExtrudingRecipeBuilder consumeBlocksFirstBlock(){
        params.consumeBlocks.setFirst(true);
        return this;
    }
    public ExtrudingRecipeBuilder consumeBlocksSecondBlock(){
        params.consumeBlocks.setSecond(true);
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

}
