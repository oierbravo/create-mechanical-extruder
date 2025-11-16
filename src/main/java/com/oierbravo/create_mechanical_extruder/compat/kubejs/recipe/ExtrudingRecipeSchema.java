package com.oierbravo.create_mechanical_extruder.compat.kubejs.recipe;

import com.oierbravo.mechanicals.compat.kubejs.components.*;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.util.BlockPredicateUtils;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.IntBounds;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface ExtrudingRecipeSchema {
    RecipeKey<ProcessingOutput> RESULT = ProcessingOutputComponent.PROCESSING_OUTPUT.key("result",ComponentRole.OUTPUT).noFunctions();
    RecipeKey<Couple<BlockPredicate>> BLOCK_INGREDIENTS = CoupleBlockPredicateComponent.COUPLE_BLOCK_PREDICATE.key("blockIngredients", ComponentRole.INPUT).noFunctions();
    RecipeKey<BlockPredicate> CATALYST = BlockPredicateComponent.BLOCK_PREDICATE.key("catalyst",ComponentRole.OTHER).optional(BlockPredicateUtils.Matcher.EMPTY);
    RecipeKey<Integer> REQUIRED_BONKS = NumberComponent.INT.key("requiredBonks", ComponentRole.OTHER).optional(1).alwaysWrite();
    RecipeKey<Boolean> ADVANCED = BooleanComponent.BOOLEAN.key("advanced", ComponentRole.OTHER).optional(false);
    RecipeKey<Couple<Boolean>> CONSUME_BLOCKS = CoupleBooleanComponent.COUPLE_BOOLEAN.key("consumeBlocks", ComponentRole.OTHER).optional(Couple.create(false,false));
    RecipeKey<List<IRecipeRequirement>> RECIPE_REQUIREMENTS = RecipeRequirementsComponent.REQUIREMENT.instance().asListOrSelf().withBounds(IntBounds.OPTIONAL).key("requirements", ComponentRole.OTHER).optional(List.of());

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, BLOCK_INGREDIENTS, CATALYST, REQUIRED_BONKS, ADVANCED, CONSUME_BLOCKS, RECIPE_REQUIREMENTS).factory(ExtrudingKubeRecipe.FACTORY);;

}
