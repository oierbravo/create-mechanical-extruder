package com.oierbravo.createmechanicalextruder.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.oierbravo.createmechanicalextruder.components.extruder.BiomeCondition;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.rhino.NativeObject;
import net.minecraft.world.level.biome.Biome;

public interface ExtrudingRecipeSchema {
    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst").defaultOptional().allowEmpty();
    RecipeKey<Integer> REQUIRED_BONKS = NumberComponent.INT.key("requiredBonks").optional(1);

    //TagKeyComponent<TagKey<Biome>> BIOME
    //RecipeKey<BiomeCondition> BIOME = BiomeConditionComponent.BIOME_CONDITION.key("biome").allowEmpty().defaultOptional();
    public class ExtrudingRecipe extends RecipeJS {
        public RecipeJS withCatalyst(InputItem item) {
            return setValue(CATALYST, item);
        }

    }
    /*public class BiomeConditionComponent implements RecipeComponent<BiomeCondition> {
        public static final RecipeComponent<BiomeCondition> BIOME_CONDITION= new BiomeConditionComponent();

        public ComponentRole role() {
            return ComponentRole.OTHER;
        }

        @Override
        public Class<?> componentClass() {
            return BiomeCondition.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, BiomeCondition value) {
            return value.serialize();
        }

        private BiomeCondition fromNativeObject(NativeObject nativeObject ){
            if(nativeObject.containsKey("tag"))
                return BiomeCondition.fromString("#" + nativeObject.get("tag"));
            return BiomeCondition.fromString(nativeObject.get("name").toString());
        }
        @Override
        public BiomeCondition read(RecipeJS recipe, Object from) {
            if (from instanceof BiomeCondition bc) {
                return bc;

            } else if (from instanceof Biome b) {
                return BiomeCondition.fromBiome(b);
            } else if (from instanceof JsonObject je) {
                return BiomeCondition.deserialize(je);
            } else if(from instanceof NativeObject no){
                return fromNativeObject(no);

            } else {
                return BiomeCondition.fromString(String.valueOf(from));
            }
        }
    }*/
    RecipeSchema SCHEMA = new RecipeSchema(ExtrudingRecipe.class, ExtrudingRecipe::new, RESULT, INGREDIENTS, CATALYST, REQUIRED_BONKS);

}
