package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface ExtrudingRecipeSchema {
    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst").defaultOptional().allowEmpty();
    RecipeKey<Integer> REQUIRED_BONKS = NumberComponent.INT.key("requiredBonks").optional(1);
    RecipeKey<Float> MIN_SPEED = NumberComponent.FLOAT.key("min_speed").defaultOptional().allowEmpty();
    RecipeKey<Integer> MIN_HEIGHT = NumberComponent.INT.key("min_height").defaultOptional().allowEmpty();
    RecipeKey<Integer> MAX_HEIGHT = NumberComponent.INT.key("max_height").defaultOptional().allowEmpty();
    RecipeKey<TagKey<Biome>> BIOME = TagKeyComponent.BIOME.key("biome").defaultOptional().allowEmpty();

    //TagKeyComponent<TagKey<Biome>> BIOME
    //RecipeKey<BiomeCondition> BIOME = BiomeConditionComponent.BIOME_CONDITION.key("biome").allowEmpty().defaultOptional();
    public class ExtrudingRecipe extends RecipeJS {
        public RecipeJS withCatalyst(InputItem item) {
            return setValue(CATALYST, item);
        }

        public RecipeJS minHeight(int value) {
            return setValue(MIN_HEIGHT, value);
        }
        public RecipeJS maxHeight(int value) {
            return setValue(MAX_HEIGHT, value);
        }
        public RecipeJS minSpeed(float value) {
            return setValue(MIN_SPEED, value);
        }
        /*public RecipeJS biome(TagKey<Biome> value) {
            return setValue(BIOME, value);
        }*/

    }

    RecipeSchema SCHEMA = new RecipeSchema(ExtrudingRecipe.class, ExtrudingRecipe::new, RESULT, INGREDIENTS, CATALYST, REQUIRED_BONKS, MIN_SPEED, MIN_HEIGHT, MAX_HEIGHT, BIOME);

}
