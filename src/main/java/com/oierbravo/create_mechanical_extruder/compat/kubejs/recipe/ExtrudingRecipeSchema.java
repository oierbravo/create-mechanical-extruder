package com.oierbravo.create_mechanical_extruder.compat.kubejs.recipe;

import com.oierbravo.mechanicals.compat.kubejs.components.BlockPredicateComponent;
import com.oierbravo.mechanicals.compat.kubejs.components.CoupleBooleanComponent;
import com.oierbravo.mechanicals.compat.kubejs.components.ProcessingOutputComponent;
import com.oierbravo.mechanicals.compat.kubejs.components.RecipeRequirementsComponent;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.util.BlockPredicateUtils;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface ExtrudingRecipeSchema {
    RecipeKey<ProcessingOutput> RESULT = ProcessingOutputComponent.OUTPUT.key("result",ComponentRole.OUTPUT).noFunctions();
    RecipeKey<Couple<BlockPredicate>> BLOCK_INGREDIENTS = BlockPredicateComponent.BLOCK_PREDICATE_COUPLE.key("blockIngredients", ComponentRole.INPUT).noFunctions();
    RecipeKey<BlockPredicate> CATALYST = BlockPredicateComponent.BLOCK_PREDICATE.key("catalyst",ComponentRole.OTHER).optional(BlockPredicateUtils.Matcher.EMPTY).allowEmpty();
    RecipeKey<Integer> REQUIRED_BONKS = NumberComponent.INT.key("requiredBonks", ComponentRole.OTHER).optional(1).allowEmpty();
    RecipeKey<Boolean> ADVANCED = BooleanComponent.BOOLEAN.key("advanced", ComponentRole.OTHER).optional(false).allowEmpty();
    RecipeKey<Couple<Boolean>> CONSUME_BLOCKS = CoupleBooleanComponent.BOOLEAN.key("consumeBlocks", ComponentRole.OTHER).optional(Couple.create(false,false)).allowEmpty();
    RecipeKey<List<IRecipeRequirement>> RECIPE_REQUIREMENTS = RecipeRequirementsComponent.RECIPE_REQUIREMENT.asList().key("requirements", ComponentRole.OTHER).optional(List.of()).allowEmpty();

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, BLOCK_INGREDIENTS, CATALYST, REQUIRED_BONKS, ADVANCED, CONSUME_BLOCKS, RECIPE_REQUIREMENTS).factory(ExtrudingKubeRecipe.FACTORY);;

}
