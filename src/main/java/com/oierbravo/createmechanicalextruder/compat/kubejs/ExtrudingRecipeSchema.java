package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ExtrudingRecipeSchema {
    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    //RecipeKey<Block> CATALYST = BlockComponent.INPUT.key("catalyst").optional(Blocks.AIR).allowEmpty();
    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst").defaultOptional().allowEmpty();
    RecipeKey<Integer> REQUIRED_BONKS = NumberComponent.INT.key("requiredBonks").optional(1);

    public class ExtrudingRecipe extends RecipeJS{
        public RecipeJS withCatalyst(InputItem item) {
            return setValue(CATALYST, item);
        }

    }
    RecipeSchema SCHEMA = new RecipeSchema(ExtrudingRecipe.class, ExtrudingRecipe::new, RESULT, INGREDIENTS, CATALYST, REQUIRED_BONKS);

}
