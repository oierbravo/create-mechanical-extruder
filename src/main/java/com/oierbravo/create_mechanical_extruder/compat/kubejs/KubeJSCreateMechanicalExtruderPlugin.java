package com.oierbravo.create_mechanical_extruder.compat.kubejs;

import com.oierbravo.create_mechanical_extruder.ModConstants;
import com.oierbravo.create_mechanical_extruder.compat.kubejs.recipe.ExtrudingKubeRecipe;
import com.oierbravo.create_mechanical_extruder.compat.kubejs.recipe.ExtrudingRecipeSchema;
import com.oierbravo.create_mechanical_extruder.components.extruder.recipe.ExtrudingRecipe;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class KubeJSCreateMechanicalExtruderPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeFactories(RecipeFactoryRegistry registry) {
        registry.register(ExtrudingKubeRecipe.FACTORY);
    }
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ModConstants.asResource(ExtrudingRecipe.Type.ID), ExtrudingRecipeSchema.SCHEMA);
    }
}